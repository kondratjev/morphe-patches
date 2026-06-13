package app.morphe.patches.soundcloud.telemetry

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.soundcloud.shared.Constants.COMPATIBILITY_SOUNDCLOUD

@Suppress("unused")
val disableTelemetryPatch = bytecodePatch(
    name = "Disable telemetry",
    description = "Disables SoundCloud's telemetry system.",
) {
    compatibleWith(COMPATIBILITY_SOUNDCLOUD)

    execute {
        HandleMessageFingerprint.methodOrNull?.addInstructions(0, "return-void")
    }
}
