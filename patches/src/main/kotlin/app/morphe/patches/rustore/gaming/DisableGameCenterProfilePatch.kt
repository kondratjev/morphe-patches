package app.morphe.patches.rustore.gaming

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE
import app.morphe.util.returnEarly

@Suppress("unused")
val disableGameCenterProfilePatch = bytecodePatch(
    name = "Disable gaming profile",
    description = "Removes the Game Profile section from the Mine screen, " +
            "including the button and the usage statistics screen.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        // Block navigation from MineV2ViewModel (V2/V3 Mine screen).
        MineV2ViewModelOpenGameCenterFingerprint.method.returnEarly()

        // Hide Game Center button in V2 Mine screen.
        GameCenterV2ButtonComposableFingerprint.method.returnEarly()

        // Hide Game Center button in V1 Mine screen.
        GameCenterV1ButtonComposableFingerprint.method.returnEarly()
    }
}
