# Agent Notes

## Bump to a new app version

Use `droidsaw` CLI (takes a PATH, JSON to stdout). Prefer it over
`jadx`/`apktool` for fingerprint checks. Bump only when ALL pass —
obfuscators rename, move, reshape; a name alone is not a match.

1. Confirm target. XAPK = zip with `com.*.apk` + `manifest.json`.
   Unpack the base APK, `droidsaw info` → `version_name` /
   `version_code` / `package`. Never bump from an unversioned fallback
   (`<package>.xapk` without `@version`); APKPure lags Play — lagged
   means blocked, report it, don't force it.
2. Check every fingerprint the patch relies on — all `Fingerprints.kt`
   plus `dependsOn` chains. Signature: `definingClass` + `name` +
   `returnType` + `parameters` (+ `accessFlags` if set). Pin full
   signatures — names repeat inside one class (e.g. companion `a`).
   Required (`.method`) fingerprints fail LOUDLY — never silence with
   `methodOrNull` unless absence is genuinely tolerable. `custom`
   matchers must be UNIQUE in the new APK. For `methodOrNull`
   fallbacks list matched vs missing (one missing = old/new UI
   variant, all missing = dead patch).
3. Verify bodies, not names. Decompile the exact class (anchor the
   regex, else you get all inner classes), diff old-vs-new — download
   the OLD apk too (`apkeep -a <pkg>@<old>`). Same logic = bump only.
   Changed logic = update fingerprints / patch first.
4. Verify by layer:
   - DEX: referenced-but-not-implemented is fine only if another unit
     holds the definition.
   - Resources: every referenced id must exist in the NEW apk
     (`aapt2 dump resources` + `dump xmltree`).
   - Byte patterns (Hermes/JS): exactly once in the new bundle,
     same-length replacements. Check magic first — a JS pattern
     scores 0 on a Hermes bundle, which means a new Hermes patch or
     a REVERTED bump, never a blind one. String-pool ids shift every
     release — re-derive, never reuse.
5. Bump `patches/.../<app>/shared/Constants.kt` (source of truth):
   replace the superseded `AppTarget`, keep genuinely different
   variants. Update stale comments. Simulate replacements outside
   Kotlin first (single match each). Delete stale `build/libs/*.mpp`
   (the generator takes the FIRST jar), regenerate `patches-list.json`
   + `README.md` (`:patches:generatePatchesList`), confirm the
   `version` field didn't downgrade. Then
   `./gradlew :patches:compileKotlin -x test --offline`.

## Code comments (one scheme)

- Fingerprint KDoc: `/** Matches \`<Class>.<method>(<params>)\` — <behavior>. */`
  History in trailing parens:
  `(Was <old> in <ver>; <new> in <ver>, same logic.)`
- Patch `execute` steps: `// <Goal>.` plus at most one `// <why>` line.
  No mechanics, no versions — they rot. Mechanics live in the KDoc.
- Section dividers in `Fingerprints.kt`: `// <Area>` as-is.

## Batch update loop

User downloads APKs first (`scripts/download-all-apks.py` — with
`APKEEP_PLAY_EMAIL` + token env it pulls lag-free from Google Play),
agent updates apps one by one, then regenerates lists and compiles
once at the end.
