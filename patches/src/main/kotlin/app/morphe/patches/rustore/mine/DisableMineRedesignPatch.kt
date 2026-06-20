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
        // Force the MainViewModel$1$5 coroutine that reads
        // featureMineRedesignV3Enabled to return immediately with null.
        // This keeps MainViewState.f34480m at default (false),
        // so MineDestination always renders the V1 Mine screen.
        MainViewStateMineRedesignFingerprint.method.returnEarly(null as Void?)
    }
}
