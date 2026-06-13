package app.morphe.patches.rustore.analytics

import app.morphe.patcher.Fingerprint

/**
 * Matches `AltCraftAnalyticsImpl.a(String, Map, String, boolean, gg2.f)`
 * — the main send method that all AltCraft event reporting converges on.
 * Class `mg2.b` is the obfuscated name.
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
 * Matches `RadarFlushSnapshotWorker.b(Lfq0/e;)` — the compiled
 * CoroutineWorker.doWork(). Returning null causes WorkManager to
 * treat it as a failure after retries.
 */
object RadarDoWorkFingerprint : Fingerprint(
    definingClass = "RadarFlushSnapshotWorker;",
    name = "b",
    returnType = "Ljava/lang/Object;",
    custom = { method, _ ->
        method.parameterTypes.size == 1 &&
            method.implementation != null
    },
)
