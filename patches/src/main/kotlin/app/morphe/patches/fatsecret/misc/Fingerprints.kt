package app.morphe.patches.fatsecret.misc

import app.morphe.patcher.Fingerprint

/**
 * Matches `BaseActivity.x3(boolean)` — the method that shows the
 * force-update bottom sheet dialog. The `boolean` param controls
 * `is_cancelable`: true = skip button shown, false = unskippable.
 * Patching it to always be true makes the dialog skippable.
 */
object ShowUpdateDialogFingerprint : Fingerprint(
    definingClass = "Lcom/fatsecret/android/ui/activity/BaseActivity;",
    name = "x3",
    returnType = "V",
    parameters = listOf("Z"),
)
