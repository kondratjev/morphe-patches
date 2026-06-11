package app.morphe.patches.rustore.ads

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE

@Suppress("unused")
val disableAdsPatch = bytecodePatch(
    name = "Disable ads",
    description = "Disables all advertisements (banners, native ads, MyTarget, " +
            "SSP, VKR) by blocking the ad repository at the data source. " +
            "Other features like notifications and security settings " +
            "are NOT affected.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        RawAdvertisementRepoGetFingerprint.method.addInstructions(
            0,
            "const/4 v0, 0x0\nreturn-object v0"
        )
    }
}
