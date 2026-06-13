package app.morphe.patches.rustore.auth

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE
import app.morphe.util.returnEarly

@Suppress("unused")
val skipUpdateAuthPatch = bytecodePatch(
    name = "Skip update auth",
    description = "Removes the login requirement when updating apps.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        AuthSuggestShownFingerprint.method.returnEarly(null as Void?)
    }
}
