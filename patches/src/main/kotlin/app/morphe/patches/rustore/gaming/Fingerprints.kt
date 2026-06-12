package app.morphe.patches.rustore.gaming

import app.morphe.patcher.Fingerprint

/**
 * Matches `MineV2ViewModel.openGameCenter()` — the method that navigates
 * from the V2 Mine screen to the Game Center Stats screen. It logs
 * "gameProfile.click" and navigates to `GameCenterStatsDestination`.
 *
 * Class `j8` in `pi1` = MineV2ViewModel.
 * Method `p0` = openGameCenter()V — the `o6` interface callback.
 */
object MineV2ViewModelOpenGameCenterFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lpi1/j8;" &&
                method.name == "p0" &&
                method.returnType == "V" &&
                method.parameters.isEmpty()
    }
)

/**
 * Matches `MineViewModel.openGameCenter()` — the method that navigates
 * from the V1 Mine screen to the Game Center Stats screen. It navigates
 * to `GameCenterStatsDestination`.
 *
 * Class `h9` in `pi1` = MineViewModel.
 * Method `p5` = openGameCenter()V — the `v` interface callback.
 */
object MineViewModelOpenGameCenterFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lpi1/h9;" &&
                method.name == "p5" &&
                method.returnType == "V" &&
                method.parameters.isEmpty()
    }
)
