package app.morphe.patches.rustore.ads

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE
import app.morphe.util.returnEarly

@Suppress("unused")
val disableAdsPatch = bytecodePatch(
    name = "Disable ads",
    description = "Removes all advertisements from the app.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        RawAdvertisementRepoGetFingerprint.method.returnEarly(null as Void?)
    }
}
