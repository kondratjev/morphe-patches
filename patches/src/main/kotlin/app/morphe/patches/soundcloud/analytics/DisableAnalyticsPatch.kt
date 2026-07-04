package app.morphe.patches.soundcloud.analytics

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.all.analytics.disableAnalyticsPatch as globalDisableAnalyticsPatch
import app.morphe.patches.soundcloud.shared.Constants.COMPATIBILITY_SOUNDCLOUD
import app.morphe.util.returnEarly

@Suppress("unused")
val disableAnalyticsPatch = bytecodePatch(
    name = "Disable analytics",
    description = "Disables SoundCloud's analytics.",
) {
    compatibleWith(COMPATIBILITY_SOUNDCLOUD)

    dependsOn(globalDisableAnalyticsPatch)

    execute {
        HandleMessageFingerprint.methodOrNull?.returnEarly()
    }
}
