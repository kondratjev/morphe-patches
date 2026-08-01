package app.morphe.patches.rustore.auth

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE

@Suppress("unused")
val skipUpdateAuthPatch = bytecodePatch(
    name = "Skip update auth",
    description = "Removes the login requirement when updating apps.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        AuthSuggestShownFingerprint.method.addInstructions(
            0,
            "const/4 v0, 0x0\nreturn-object v0"
        )
    }
}
