package app.morphe.patches.rustore.gaming

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE

@Suppress("unused")
val disableGameCenterProfilePatch = bytecodePatch(
    name = "Disable gaming profile",
    description = "Disables the Game Center profile screen (game usage statistics) " +
            "by blocking navigation from the Mine screen. Both MineViewModel " +
            "(V1) and MineV2ViewModel (V2/V3) navigation to " +
            "GameCenterStatsDestination is disabled. The 'Game Profile' " +
            "menu item will no longer open the gaming statistics screen.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        // Hook A: Block navigation from MineV2ViewModel.
        // The p0() method logs "gameProfile.click" and navigates to
        // GameCenterStatsDestination. Making it a no-op prevents the
        // V2/V3 Mine screen from opening the gaming profile.
        MineV2ViewModelOpenGameCenterFingerprint.method.addInstructions(
            0,
            "return-void"
        )

        // Hook B: Block navigation from MineViewModel.
        // The p5() method navigates to GameCenterStatsDestination.
        // Making it a no-op prevents the V1 Mine screen from opening
        // the gaming profile.
        MineViewModelOpenGameCenterFingerprint.method.addInstructions(
            0,
            "return-void"
        )
    }
}
