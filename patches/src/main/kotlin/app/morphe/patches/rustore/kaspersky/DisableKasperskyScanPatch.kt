package app.morphe.patches.rustore.kaspersky

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE
import app.morphe.util.returnEarly

@Suppress("unused")
val disableKasperskyScanPatch = bytecodePatch(
    name = "Disable background scan",
    description = "Disables the periodic Kaspersky background device scan.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        // Force isPeriodicScanEnabled to false — all consumers read this getter.
        KasperskyScannerDtoIsPeriodicScanEnabledFingerprint.method.returnEarly(false)

        // Prevent enqueuePeriodic from scheduling the daily scan.
        KasperskyScannerWorkerEnqueuePeriodicFingerprint.method.returnEarly(null as Void?)
    }
}
