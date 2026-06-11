package app.morphe.patches.rustore.kaspersky

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE

@Suppress("unused")
val disableKasperskyScanPatch = bytecodePatch(
    name = "Disable background scan",
    description = "Disables the Kaspersky-powered periodic device scan. " +
            "The scan checks all files for vulnerabilities and runs daily " +
            "in the background. This patch forces the feature off regardless " +
            "of the stored user preference.",
    default = true,
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
            """
                sget-object v0, Lbq0/g0;->f15230a:Lbq0/g0;
                return-object v0
            """.trimIndent()
        )
    }
}
