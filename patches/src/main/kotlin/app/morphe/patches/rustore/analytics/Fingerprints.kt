package app.morphe.patches.rustore.analytics

import app.morphe.patcher.Fingerprint

/**
 * Matches `AltCraftAnalyticsImpl.a(String, Map, String, boolean, gg2.f)` —
 * the main send method all AltCraft event reporting converges on.
 */
object AltCraftSendFingerprint : Fingerprint(
    definingClass = "Lmg2/b;",
    name = "a",
    custom = { method, _ ->
        method.parameterTypes.size == 5 &&
            method.parameterTypes[0] == "Ljava/lang/String;" &&
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
