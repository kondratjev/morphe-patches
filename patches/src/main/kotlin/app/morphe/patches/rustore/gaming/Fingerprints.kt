package app.morphe.patches.rustore.gaming

import app.morphe.patcher.Fingerprint

/**
 * Matches `MineV2ViewModel.openGameCenter()` — navigates from the V2 Mine
 * screen to Game Center Stats. Logs "gameProfile.click".
 */
object MineV2ViewModelOpenGameCenterFingerprint : Fingerprint(
    definingClass = "Lpi1/j8;",
    name = "p0",
    returnType = "V",
    parameters = emptyList(),
)

/**
 * Matches `MineViewModel.openGameCenter()` — navigates from the V1 Mine
 * screen to Game Center Stats.
 */
object MineViewModelOpenGameCenterFingerprint : Fingerprint(
    definingClass = "Lpi1/h9;",
    name = "p5",
    returnType = "V",
    parameters = emptyList(),
)

/**
 * Matches `GameCenterV2ButtonWidgetKt.GameCenterV2Button()` — V2 Mine
 * screen composable that renders the Game Center stats button.
 */
object GameCenterV2ButtonComposableFingerprint : Fingerprint(
    definingClass = "Lwb1/i0;",
    name = "d",
    returnType = "V",
)

/**
 * Matches `GameCenterButtonWidgetKt.GameCenterButton()` — V1 Mine
 * screen composable that renders the Game Center stats button.
 */
object GameCenterV1ButtonComposableFingerprint : Fingerprint(
    definingClass = "Lwb1/o;",
    name = "e",
    returnType = "V",
)
