package app.morphe.patches.rustore.analytics

import app.morphe.patcher.Fingerprint

/**
 * Matches `AltCraftSendEventsUseCase.a(I, Continuation)` — the suspend
 * use-case entry for AltCraft (compiled from `AltCraftSendEventsUseCase.kt`).
 * Obfuscated as `Lwo2/c;` in 1.108.0.2; previously `Lfj2/b;`.
 */
object AltCraftSendFingerprint : Fingerprint(
    definingClass = "Lwo2/c;",
    name = "a",
    custom = { method, _ ->
        method.parameterTypes.size == 2 &&
            method.implementation != null
    },
)

/**
 * Matches `RadarFlushSnapshotWorker.b(Lfq0/e;)` — compiled
 * CoroutineWorker.doWork(). Returning null causes WorkManager to
 * treat it as failure after retries.
 */
object RadarDoWorkFingerprint : Fingerprint(
    definingClass = "Lru/vk/store/lib/analytics/system/radar/presentation/RadarFlushSnapshotWorker;",
    name = "b",
    returnType = "Ljava/lang/Object;",
    custom = { method, _ ->
        method.parameterTypes.size == 1 &&
            method.implementation != null
    },
)
