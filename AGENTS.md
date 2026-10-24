# Agent Notes

## Actualization (bump to a new app version)

Use `droidsaw` for every step. It reads APK/XAPK directly, no install needed.
Every subcommand writes JSON to stdout, progress to stderr. Prefer it over
`jadx`/`apktool` for fingerprint checks.

Bump is done only when EVERY check below passes. Presence of a name is
not a match — obfuscators rename, move, and reshape code between releases.

1. Confirm the target. XAPK = zip with `com.*.apk` + `manifest.json`.
   Unpack the base APK, then `droidsaw info` → `version_name` /
   `version_code` / `package`. Never bump from an unversioned fallback
   artifact (`<package>.xapk` without `@version`).
2. Check every fingerprint the patch relies on — all `Fingerprints.kt`
   of the app, plus `dependsOn` chains. Exact signature:
   `definingClass` + `name` + `returnType` + `parameters`
   (+ `accessFlags` if set). Required (`.method`) fingerprints must fail
   LOUDLY on mismatch — never silence with `methodOrNull` unless absence
   is genuinely tolerable. For `custom = {...}` matchers the match must be
   UNIQUE in the new APK; for `methodOrNull` fallbacks list matched vs
   missing variants (one missing = old/new UI variant, all missing =
   dead patch).
3. Verify bodies, not just names. Decompile the exact class (anchor the
   regex, otherwise you get all inner classes) and diff old-vs-new APK:
   same return values, same keys, same field layout. Same logic = bump
   only. Changed logic = update fingerprints / patch first. Download the
   OLD apk too (`apkeep -a <pkg>@<old>`) — comparison needs both sides.
4. Verify by layer:
   - DEX: method bodies as above; a referenced-but-not-implemented entry
     is fine only if another DEX unit holds the definition.
   - Resources: every referenced id must exist in the NEW apk
     (`aapt2 dump resources` + `dump xmltree` for structure). A patch
     that hides/shows UI must target ids present in the new layout.
   - Byte patterns (Hermes/JS bundles): patterns are version-pinned and
     must occur exactly once in the new bundle; replacements must keep
     length. String-pool ids shift every release — re-derive them, never
     reuse old ones. If patterns can't be re-derived, REVERT the bump
     instead of shipping a dead target.
5. Bump: `patches/.../<app>/shared/Constants.kt` is the source of truth.
   Replace the superseded `AppTarget`, keep genuinely different variants
   as separate targets. Update stale comments. Then regenerate
   `patches-list.json` + `README.md` (`:patches:generatePatchesList`) and
   only then `./gradlew :patches:compileKotlin -x test --offline`.
   Simulate replacements outside Kotlin first where possible
   (apply each, assert single match) before editing patch sources.

## Code comments (one scheme)

- Fingerprint KDoc: `/** Matches \`<Class>.<method>(<params>)\` — <behavior>. */`
  History goes in parentheses at the end:
  `(Was <old> in <ver>; <new> in <ver>, same logic.)`
- Patch `execute` steps: `// <Goal>.` plus at most one `// <why>` line.
  No mechanics essays, no version numbers — they rot (a renamed method
  leaves a lying comment). Mechanics live in the fingerprint KDoc.
- Section dividers in `Fingerprints.kt`: `// <Area>` as-is.

## Batch update loop

User downloads APKs first (`scripts/download-all-apks.py`), agent updates
apps one by one with the flow above, then regenerates lists and compiles
once at the end.
