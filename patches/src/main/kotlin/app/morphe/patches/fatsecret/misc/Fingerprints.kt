package app.morphe.patches.fatsecret.misc

import app.morphe.patcher.Fingerprint

/**
 * Matches `BaseActivity.G3(boolean)` — the method that shows the
 * force-update bottom sheet dialog. The `boolean` param controls
 * `is_cancelable`: true = skip button shown, false = unskippable.
 * Patching it to always be true makes the dialog skippable.
 * (Was `x3` in 11.8.1.1; R8 renamed it in 11.8.2.1, same logic.)
 */
object ShowUpdateDialogFingerprint : Fingerprint(
    definingClass = "Lcom/fatsecret/android/ui/activity/BaseActivity;",
    name = "G3",
    returnType = "V",
    parameters = listOf("Z"),
)
