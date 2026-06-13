package app.morphe.patches.rustore.kaspersky

import app.morphe.patcher.Fingerprint

/**
 * Matches the getter `isPeriodicScanEnabled()Z` in `KasperskyScannerDto` —
 * the single source of truth that ALL consumers read to determine
 * whether periodic scanning is enabled. Forcing this to return false
 * works for both new and existing installs.
 */
object KasperskyScannerDtoIsPeriodicScanEnabledFingerprint : Fingerprint(
    definingClass = "Lru/vk/store/feature/kaspersky/impl/data/KasperskyScannerDto;",
    name = "isPeriodicScanEnabled",
    returnType = "Z",
    parameters = emptyList(),
)

/**
 * Matches `KasperskyScannerWorker$a.a()` — the `enqueuePeriodic` method
 * that schedules the daily background scan via WorkManager. Making this
 * a no-op prevents the scan from ever being enqueued.
 */
object KasperskyScannerWorkerEnqueuePeriodicFingerprint : Fingerprint(
    definingClass = "Lru/vk/store/feature/kaspersky/impl/presentation/KasperskyScannerWorker\$a;",
    name = "a",
    custom = { method, _ ->
        method.parameterTypes.size == 2 &&
            method.parameterTypes[0] == "Lhb/l0;"
    },
)
