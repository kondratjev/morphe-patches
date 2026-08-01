package app.morphe.patches.rustore.kaspersky

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE

@Suppress("unused")
val disableKasperskyScanPatch = bytecodePatch(
    name = "Disable background scan",
    description = "Disables the periodic Kaspersky background device scan.",
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        // Hook A: Force the getter to always return false.
        // Every consumer (UI toggle, WorkManager scheduler, settings)
        // reads this getter — overriding it ensures scan is OFF globally.
        KasperskyScannerDtoIsPeriodicScanEnabledFingerprint.method.addInstructions(
            0,
            "const/4 v0, 0x0\nreturn v0"
        )

        // Hook B: Prevent enqueuePeriodic from scheduling the daily scan.
        // Even if the toggle handler somehow tries to enable the scan,
        // the WorkManager job will never be created.
        KasperskyScannerWorkerEnqueuePeriodicFingerprint.method.addInstructions(
            0,
            "const/4 v0, 0x0\nreturn-object v0"
        )
    }
}
