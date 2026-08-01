package app.morphe.patches.rustore.kaspersky

import app.morphe.patcher.Fingerprint

/**
 * Matches the getter `isPeriodicScanEnabled()Z` in `KasperskyScannerDto` —
 * the single source of truth that ALL consumers read to determine
 * whether periodic scanning is enabled. Forcing this to return false
 * works for both new and existing installs.
 */
object KasperskyScannerDtoIsPeriodicScanEnabledFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lru/vk/store/feature/kaspersky/impl/data/KasperskyScannerDto;" &&
                method.name == "isPeriodicScanEnabled" &&
                method.returnType == "Z" &&
                method.parameters.isEmpty()
    }
)

/**
 * Matches `KasperskyScannerWorker$a.a()` — the `enqueuePeriodic` method
 * that schedules the daily background scan via WorkManager. Making this
 * a no-op prevents the scan from ever being enqueued.
 */
object KasperskyScannerWorkerEnqueuePeriodicFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lru/vk/store/feature/kaspersky/impl/presentation/KasperskyScannerWorker\$a;" &&
                method.name == "a" &&
                method.parameterTypes.size == 2 &&
                method.parameterTypes[0] == "Lhb/l0;"
    }
)
