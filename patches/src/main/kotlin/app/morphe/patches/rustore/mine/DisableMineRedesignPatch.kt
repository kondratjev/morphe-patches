package app.morphe.patches.rustore.mine

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE

@Suppress("unused")
val disableMineRedesignPatch = bytecodePatch(
    name = "Disable Mine redesign",
    description = "Forces the app to use the original V1 Mine screen by " +
            "disabling the Mine V2/V3 redesign feature toggle. The " +
            "`featureMineRedesignV3Enabled` remote toggle is intercepted " +
            "before it can update the navigation tab state, so the app " +
            "always falls back to the classic MineViewModel/MineScreen " +
            "layout. The V2/V3 redesigned UI with the profile menu, " +
            "Connect integration, and restructured layout is suppressed.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        // Force the MineNavigationTabDelegateImpl coroutine that reads
        // featureMineRedesignV3Enabled to return immediately with null.
        //
        // The coroutine (g2 = MineNavigationTabDelegateImpl$init$3) is
        // launched during MainViewModel initialization. It reads the
        // Feature.Remote.a D3 toggle from FlipperRepository and updates
        // l2.f85139h (MutableStateFlow<MineNavigationTabState>) with
        // the redesign flag via compareAndSet. By returning null early:
        //   - The state stays at its default: hh1.b(false, false, false)
        //   - The third boolean (redesignEnabled) remains false
        //   - The app uses MineViewModel + MineScreen (V1) instead of
        //     MineV2ViewModel + MineV2Screen (V2/V3)
        //
        // We use const/4 v0, 0x0 (null) rather than sget-object for
        // kotlin.Unit because referencing obfuscated class names in
        // manually injected smali causes VerificationError at class
        // load time. Returning null is semantically equivalent to Unit
        // for a launch {} coroutine — the coroutine framework treats
        // any non-COROUTINE_SUSPENDED value as a successful result.
        MineNavigationTabDelegateInitFingerprint.method.addInstructions(
            0,
            "const/4 v0, 0x0\n" +
                    "return-object v0"
        )
    }
}
