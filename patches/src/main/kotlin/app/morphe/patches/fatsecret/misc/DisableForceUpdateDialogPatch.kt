package app.morphe.patches.fatsecret.misc

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.fatsecret.shared.Constants.COMPATIBILITY_FATSECRET

@Suppress("unused")
val disableForceUpdateDialogPatch = bytecodePatch(
    name = "Disable force update dialog",
    description = "Makes the 'App must update' dialog skippable.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_FATSECRET)

    execute {
        // BaseActivity.x3(boolean isCancelable): force p1 (isCancelable) = true.
        ShowUpdateDialogFingerprint.methodOrNull?.addInstructions(
            0,
            "const/4 p1, 0x1",
        )
    }
}
