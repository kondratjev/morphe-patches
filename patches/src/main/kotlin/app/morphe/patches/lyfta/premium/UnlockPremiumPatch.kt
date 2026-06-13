package app.morphe.patches.lyfta.premium

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import app.morphe.patches.all.pairip.license.disableLicenseCheckPatch
import app.morphe.patches.lyfta.shared.Constants.COMPATIBILITY_LYFTA

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks all premium features.",
) {
    compatibleWith(COMPATIBILITY_LYFTA)

    dependsOn(disableLicenseCheckPatch)

    execute {
        GetSubscriptionTypeFingerprint.method.returnEarly("premium")
    }
}
