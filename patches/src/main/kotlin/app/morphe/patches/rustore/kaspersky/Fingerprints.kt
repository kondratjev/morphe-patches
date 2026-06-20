package app.morphe.patches.rustore.kaspersky

import app.morphe.patcher.Fingerprint

/**
 * Matches `KasperskyScannerDto.isPeriodicScanEnabled()Z` — single source
 * of truth for whether periodic scanning is enabled. Forcing false
 * works for both new and existing installs.
 */
object KasperskyScannerDtoIsPeriodicScanEnabledFingerprint : Fingerprint(
    definingClass = "Lru/vk/store/feature/kaspersky/impl/data/KasperskyScannerDto;",
    name = "isPeriodicScanEnabled",
    returnType = "Z",
    parameters = emptyList(),
)

/**
 * Matches `KasperskyScannerWorker$a.a()` — `enqueuePeriodic` method
 * that schedules the daily background scan via WorkManager.
 */
object KasperskyScannerWorkerEnqueuePeriodicFingerprint : Fingerprint(
    definingClass = "Lru/vk/store/feature/kaspersky/impl/presentation/KasperskyScannerWorker\$a;",
    name = "a",
    custom = { method, _ ->
        method.parameterTypes.size == 2 &&
            method.parameterTypes[0] == "Lhb/l0;"
    },
)
