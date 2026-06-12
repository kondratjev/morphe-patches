package app.morphe.patches.rustore.mine

import app.morphe.patcher.Fingerprint

/**
 * Matches `MainViewModel$1$5.invokeSuspend()` — the coroutine that reads
 * `featureMineRedesignV3Enabled` (Feature.Remote.a D3) and stores the
 * result into `MainViewState.f34480m` (featureMineRedesignEnabled).
 *
 * When `featureMineRedesignV3Enabled` is true, `f34480m` becomes true,
 * which causes `MineDestination` (`pi1/a1`) to render `MineV2Screen`
 * instead of `MineScreen`. By forcing this coroutine to return null
 * immediately, the D3 toggle is never evaluated and `f34480m` stays at
 * its default (false), forcing the app to use the original V1 Mine screen.
 *
 * Class `h0` in `e31` = MainViewModel$1$5.
 * Method `invokeSuspend` = coroutine body that reads two remote feature
 * toggles (I0 and D3) and atomically updates the MainViewState via
 * `f1.a(...)`.
 *
 * Note: the previous fingerprint targeted `pi1/g2`
 * (MineNavigationTabDelegateImpl$init$3), which only controls the
 * `hh1.b.f48422c` (TabState.redesignEnabled) badge flag — NOT the actual
 * V1/V2 screen selection. This fingerprint targets the correct code path.
 */
object MainViewStateMineRedesignFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Le31/h0;" &&
                method.name == "invokeSuspend" &&
                method.returnType == "Ljava/lang/Object;"
    }
)
