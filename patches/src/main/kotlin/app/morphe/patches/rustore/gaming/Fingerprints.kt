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
    definingClass = "j8;",
    name = "p0",
    returnType = "V",
    parameters = emptyList(),
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
    definingClass = "h9;",
    name = "p5",
    returnType = "V",
    parameters = emptyList(),
)

/**
 * Matches `GameCenterV2ButtonWidgetKt.GameCenterV2Button()` — the V2
 * Mine screen composable that renders the Game Center stats button.
 *
 * Class `i0` in `wb1` = GameCenterV2ButtonWidgetKt.
 * Method `d` = GameCenterV2Button(Function0, Modifier, GameCenterButtonWidgetViewModel?, Composer, int).
 *
 * Called from `pi1/j7` (MineV2Screen Apps menu item) with:
 *   wb1.i0.d(onClick, modifier.withTestTag("GAME_CENTER_BUTTON_TEST_TAG"), null, composer, 48)
 *
 * Making this composable return immediately hides the Game Center button
 * from the V2 Mine screen without affecting any other UI.
 */
object GameCenterV2ButtonComposableFingerprint : Fingerprint(
    definingClass = "i0;",
    name = "d",
    returnType = "V",
)

/**
 * Matches `GameCenterButtonWidgetKt.GameCenterButton()` — the V1 Mine
 * screen composable that renders the Game Center stats button.
 *
 * Class `o` in `wb1` = GameCenterButtonWidgetKt.
 * Method `e` = GameCenterButton(Function0, Modifier, GameCenterButtonWidgetViewModel?, Composer, int).
 *
 * Called from `pi1/g5.j()` (MineScreen toolbar/header section) in two
 * variants:
 *   1. With apps button visible: wb1.o.e(onClick, LayoutWeightElement, null, composer, shift)
 *   2. Without apps button:     wb1.o.e(onClick, modifier, null, composer, shift)
 *
 * Making this composable return immediately hides the Game Center button
 * from the V1 Mine screen without affecting any other UI.
 */
object GameCenterV1ButtonComposableFingerprint : Fingerprint(
    definingClass = "o;",
    name = "e",
    returnType = "V",
)
