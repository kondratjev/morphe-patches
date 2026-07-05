package app.morphe.patches.pillo.analytics

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.all.analytics.disableAnalyticsPatch as globalDisableAnalyticsPatch
import app.morphe.patches.pillo.shared.Constants.COMPATIBILITY_PILLO
import app.morphe.util.returnEarly

@Suppress("unused")
val disableAnalyticsPatch = bytecodePatch(
    name = "Disable analytics",
    description = "Disables PulseSDK analytics.",
) {
    compatibleWith(COMPATIBILITY_PILLO)

    dependsOn(globalDisableAnalyticsPatch)

    execute {
        TrackersInitializerCreateFingerprint.methodOrNull?.returnEarly()
    }
}
