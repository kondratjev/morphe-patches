package app.morphe.patches.rustore.mine

import app.morphe.patcher.Fingerprint

/**
 * Matches `MainViewModel$1$5.invokeSuspend()` — coroutine that reads
 * `featureMineRedesignV3Enabled` and stores result into
 * `MainViewState.f34480m`. Forcing null return keeps the V1 Mine screen.
 */
object MainViewStateMineRedesignFingerprint : Fingerprint(
    definingClass = "Le31/h0;",
    name = "invokeSuspend",
    returnType = "Ljava/lang/Object;",
)
