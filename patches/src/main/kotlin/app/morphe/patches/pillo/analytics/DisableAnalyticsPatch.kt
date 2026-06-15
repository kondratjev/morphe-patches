package app.morphe.patches.pillo.analytics

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.pillo.shared.Constants.COMPATIBILITY_PILLO
import app.morphe.util.returnEarly

@Suppress("unused")
val disableAnalyticsPatch = bytecodePatch(
    name = "Disable Pulse tracking",
    description = "Disables PulseSDK analytics sending events to events.pillo.care.",
) {
    compatibleWith(COMPATIBILITY_PILLO)

    execute {
        TrackersInitializerCreateFingerprint.methodOrNull?.returnEarly()
    }
}
