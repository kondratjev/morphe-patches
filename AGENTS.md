# Agent Notes

## Actualization (bump to a new app version)

Use `droidsaw` for every step. It reads APK/XAPK directly, no install needed.
Every subcommand writes JSON to stdout, progress to stderr. Prefer it over
`jadx`/`apktool` for fingerprint checks.

1. Confirm the target. XAPK = zip with `com.*.apk` + `manifest.json`.
   Unpack the base APK, then:
   - `droidsaw info <base.apk>` → `version_name` / `version_code` / `package`
2. Check every fingerprint the patch relies on:
   - `droidsaw dex methods <base.apk> --implementations -s "<name1>|<name2>|..."`
   - `--implementations` matters: without it you see the `method_ids`
     reference table, not definitions. A method referenced in `dex1` but
     implemented only in `dex3` is fine.
   - Missing method ≠ dead patch: if the patch uses `methodOrNull` with
     fallback branches, one missing variant is expected (old UI vs new UI).
3. Verify bodies, not just names:
   - `droidsaw decompile <base.apk> -s "^<Lpackage/ClassName>;$"` → exact class
     (anchor the regex, otherwise you get all inner classes).
   - Compare logic with the previous version: same return values, same keys.
     Same logic = bump only. Changed logic = update fingerprints / patch first.
4. Check shared dependencies (`dependsOn`, e.g. Pairip license patch):
   - `droidsaw dex methods <base.apk> --implementations -s "checkLicense|validateResponse|..."`
   - Core fingerprints present + optional ones via `methodOrNull` = compatible.
5. Bump:
   - `patches/.../<app>/shared/Constants.kt` is the source of truth.
     Replace the superseded `AppTarget` with the new version.
     Keep genuinely different variants (e.g. old UI) as separate targets.
   - Update stale version comments in the patch file.
   - Regenerate `patches-list.json` and `README.md`
     (`:patches:generatePatchesList`).
6. Verify: `./gradlew :patches:compileKotlin -x test --offline`
