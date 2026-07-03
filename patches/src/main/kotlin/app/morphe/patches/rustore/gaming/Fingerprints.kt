package app.morphe.patches.rustore.gaming

import app.morphe.patcher.Fingerprint

/**
 * Matches `MineV2ViewModel.openGameCenter()` — navigates from the V2 Mine
 * screen to Game Center Stats. Logs "gameProfile.click".
 */
object MineV2ViewModelOpenGameCenterFingerprint : Fingerprint(
    definingClass = "Lvi1/k8;",
    name = "q0",
    returnType = "V",
    parameters = emptyList(),
)

/**
 * Matches `GameCenterV2ButtonWidgetKt.GameCenterV2Button()` — V2 Mine
 * screen composable that renders the Game Center stats button.
 */
object GameCenterV2ButtonComposableFingerprint : Fingerprint(
    definingClass = "Lcc1/o0;",
    name = "d",
    returnType = "V",
)

/**
 * Matches `GameCenterButtonWidgetKt.GameCenterButton()` — V1 Mine
 * screen composable that renders the Game Center stats button.
 */
object GameCenterV1ButtonComposableFingerprint : Fingerprint(
    definingClass = "Lcc1/q;",
    name = "e",
    returnType = "V",
)
