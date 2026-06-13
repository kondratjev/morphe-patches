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
        // Hook A: Block navigation from MineV2ViewModel.
        // The p0() method logs "gameProfile.click" and navigates to
        // GameCenterStatsDestination. Making it a no-op prevents the
        // V2/V3 Mine screen from opening the gaming profile.
        MineV2ViewModelOpenGameCenterFingerprint.method.returnEarly()

        // Hook B: Block navigation from MineViewModel.
        // The p5() method navigates to GameCenterStatsDestination.
        // Making it a no-op prevents the V1 Mine screen from opening
        // the gaming profile.
        MineViewModelOpenGameCenterFingerprint.method.returnEarly()

        // Hook C: Hide the Game Center button in V2 Mine screen.
        // The `wb1.i0.d()` composable renders the Game Center stats
        // button in the V2 Mine screen Apps menu item (pi1/j7).
        // Making it return-void prevents the button from appearing
        // in the V2/V3 layout.
        GameCenterV2ButtonComposableFingerprint.method.returnEarly()

        // Hook D: Hide the Game Center button in V1 Mine screen.
        // The `wb1.o.e()` composable renders the Game Center stats
        // button in the V1 Mine screen toolbar/header (pi1/g5.j).
        // Making it return-void prevents the button from appearing
        // in the V1 layout.
        GameCenterV1ButtonComposableFingerprint.method.returnEarly()
    }
}
