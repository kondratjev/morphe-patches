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
        // featureMineRedesignV3Enabled to return immediately with Unit.
        //
        // The coroutine (g2) is launched during MineNavigationTabDelegateImpl
        // initialization. It reads Feature.Remote.a D3 and updates the
        // navigation state. By returning early:
        //   - The state stays at its default: hh1.b(false, false, false)
        //   - The third boolean (redesign flag) remains false
        //   - The app uses MineViewModel + MineScreen (V1) instead of
        //     MineV2ViewModel + MineV2Screen (V2/V3)
        //
        // Returns Unit.INSTANCE (bq0.g0.f15230a) to complete the
        // coroutine successfully without side effects.
        MineNavigationTabDelegateInitFingerprint.method.addInstructions(
            0,
            "sget-object v0, Lbq0/g0;->f15230a:Ljava/lang/Object;\n" +
                    "return-object v0"
        )
    }
}
