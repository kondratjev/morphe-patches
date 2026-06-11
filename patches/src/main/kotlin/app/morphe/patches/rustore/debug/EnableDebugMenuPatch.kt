package app.morphe.patches.rustore.debug

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE

@Suppress("unused")
val enableDebugMenuPatch = bytecodePatch(
    name = "Enable debug menu",
    description = "Enables the hidden in-app debug screen and other " +
            "developer features by forcing local feature toggles to true.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        FlipperRepoGetLocalFingerprint.method.returnEarly(true)
    }
}
