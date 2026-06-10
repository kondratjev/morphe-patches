package app.morphe.patches.lyfta.premium

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import app.morphe.patches.all.pairip.license.disableLicenseCheckPatch
import app.morphe.patches.lyfta.shared.Constants.COMPATIBILITY_LYFTA

val enablePremiumPatch = bytecodePatch(
    name = "Enable Premium",
    description = "Enables app features locked behind the subscription paywall."
) {
    compatibleWith(COMPATIBILITY_LYFTA)

    dependsOn(disableLicenseCheckPatch)

    execute {
        GetSubscriptionTypeFingerprint.method.returnEarly("premium")
    }
}
