package app.morphe.patches.rustore.mine

import app.morphe.patcher.Fingerprint

/**
 * Matches `MineNavigationTabDelegateImpl$init$3.invokeSuspend()` — the
 * coroutine that reads the `featureMineRedesignV3Enabled` remote feature
 * toggle and updates the Mine tab state. When this toggle is true, the
 * app uses MineV2ViewModel/MineV2Screen; when false, it falls back to
 * the original MineViewModel/MineScreen.
 *
 * Class `g2` in `pi1` = MineNavigationTabDelegateImpl$init$3.
 * Method `invokeSuspend` = the coroutine body that reads Feature.Remote.a D3.
 *
 * The default state in `l2` (MineNavigationTabDelegateImpl) is:
 *   hh1.b(false, false, false)
 * where the third boolean is the redesign flag. By making this coroutine
 * return immediately with Unit, the state stays at the default (false),
 * forcing the app to use the V1 Mine screen.
 */
object MineNavigationTabDelegateInitFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lpi1/g2;" &&
                method.name == "invokeSuspend" &&
                method.returnType == "Ljava/lang/Object;"
    }
)
