package app.morphe.patches.rustore.gaming

import app.morphe.patcher.Fingerprint

/**
 * Matches `MineV2ViewModel.openGameCenter()` — navigates from the V2 Mine
 * screen to Game Center Stats. Logs "gameProfile.click".
 */
object MineV2ViewModelOpenGameCenterFingerprint : Fingerprint(
    definingClass = "Llo1/z8;",
    name = "C0",
    returnType = "V",
    parameters = emptyList(),
)

/**
 * Matches `GameCenterV2ButtonWidgetKt.GameCenterV2Button()` — V2 Mine
 * screen composable that renders the Game Center stats button.
 */
object GameCenterV2ButtonComposableFingerprint : Fingerprint(
    definingClass = "Ldh1/t0;",
    name = "d",
    returnType = "V",
)

/**
 * Matches `GameCenterButtonWidgetKt.GameCenterButton()` — V1 Mine
 * screen composable that renders the Game Center stats button.
 */
object GameCenterV1ButtonComposableFingerprint : Fingerprint(
    definingClass = "Ldh1/u;",
    name = "e",
    returnType = "V",
)
