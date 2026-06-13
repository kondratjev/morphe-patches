package app.morphe.patches.rustore.mine

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE
import app.morphe.util.returnEarly

@Suppress("unused")
val disableMineRedesignPatch = bytecodePatch(
    name = "Disable Mine redesign",
    description = "Reverts the Mine screen to the classic layout, " +
            "disabling the redesigned V2/V3 interface.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        // Force the MainViewModel$1$5 coroutine (e31/h0) that reads
        // featureMineRedesignV3Enabled to return immediately with null.
        //
        // The coroutine (h0 = MainViewModel$1$5) is launched during
        // MainViewModel initialization. It reads Feature.Remote.a D3
        // (featureMineRedesignV3Enabled) and stores the result into
        // MainViewState.f34480m (featureMineRedesignEnabled) via an
        // atomic compareAndSet on the MutableStateFlow.
        //
        // By returning null early:
        //   - The D3 toggle is never evaluated
        //   - MainViewState.f34480m stays at its constructor default (false)
        //   - MineDestination (pi1/a1) always calls g5.g() → MineScreen (V1)
        //
        // We use const/4 v0, 0x0 (null) rather than sget-object for
        // kotlin.Unit because referencing obfuscated class names in
        // manually injected smali causes VerificationError. Returning
        // null is semantically equivalent to Unit for a launch {}
        // coroutine — the coroutine framework treats any non-
        // COROUTINE_SUSPENDED value as a successful result.
        MainViewStateMineRedesignFingerprint.method.returnEarly(null as Void?)
    }
}
