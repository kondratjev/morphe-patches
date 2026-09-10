package app.morphe.patches.all.pairip.license

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction

import java.util.logging.Logger

@Suppress("unused")
val disableLicenseCheckPatch = bytecodePatch(
    name = "Disable Pairip license check",
    description = "Disables the Pairip license verification check.",
    default = false
) {

    execute {
        if (ProcessLicenseResponseFingerprint.methodOrNull == null || ValidateLicenseResponseFingerprint.methodOrNull == null) {
            return@execute Logger.getLogger(this::class.java.name)
                .warning("Could not find Pairip licensing check. No changes applied.")
        }

        ProcessLicenseResponseFingerprint.apply {
            // Set first parameter (responseCode) to 0 (success).
            method.addInstruction(0, "const/4 p1, 0x0")

            // Disable repeated check if present.
            RepeatedCheckFingerprint.matchOrNull(originalMethod)?.apply {
                val repeatedCheckFlagInstr = this.instructionMatches.first()
                val reg = repeatedCheckFlagInstr.getInstruction<OneRegisterInstruction>().registerA
                this.method.replaceInstruction(
                    // increase index by 1 to account for instruction added above
                    repeatedCheckFlagInstr.index + 1,
                    "const/4 v${reg}, 0x0"
                )
            }
        }

        // Short-circuit license response validation.
        ValidateLicenseResponseFingerprint.method.returnEarly()

        // Pass the local installer check even when not installed from Play Store.
        // Uses methodOrNull — older Pairip versions without this check are unaffected.
        LocalInstallerCheckFingerprint.methodOrNull?.returnEarly(true)

        // Never launch the license error dialog, no matter which sub-check failed.
        StartErrorDialogFingerprint.methodOrNull?.returnEarly()

        // Never kill the app after a silently failed check.
        ScheduleAppShutdownFingerprint.methodOrNull?.returnEarly()

        // Never start the license check in the first place — fire-and-forget
        // entry called from the (wrapped) Application. Nothing reads its result.
        CheckLicenseFingerprint.methodOrNull?.returnEarly()
    }
}
