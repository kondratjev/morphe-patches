package app.morphe.patches.soundcloud.analytics

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.soundcloud.shared.Constants.COMPATIBILITY_SOUNDCLOUD
import app.morphe.util.returnEarly

@Suppress("unused")
val disableAnalyticsPatch = bytecodePatch(
    name = "Disable analytics",
    description = "Disables SoundCloud's analytics.",
) {
    compatibleWith(COMPATIBILITY_SOUNDCLOUD)

    execute {
        HandleMessageFingerprint.methodOrNull?.returnEarly()
    }
}
