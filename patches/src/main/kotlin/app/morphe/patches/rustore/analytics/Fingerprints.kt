package app.morphe.patches.rustore.analytics

import app.morphe.patcher.Fingerprint

// ═══════════════════════════════════════════════════════════════════
// AltCraft Analytics (VK-specific) — mg2.b
// ═══════════════════════════════════════════════════════════════════

/**
 * Matches `mg2.b.a(String, Map, String, boolean, gg2.f)` — the main
 * send method of AltCraftAnalyticsImpl. This is the entry point that
 * all AltCraft event reporting converges on.
 *
 * Class `mg2.b` is the obfuscated name for AltCraftAnalyticsImpl.
 * Method `a` = send(eventName, eventParams, uuid, auto, callback).
 */
object AltCraftAnalyticsSendFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lmg2/b;" &&
                method.name == "a" &&
                method.parameterTypes.size == 5 &&
                method.parameterTypes[0] == "Ljava/lang/String;"
    }
)

// ═══════════════════════════════════════════════════════════════════
// Radar Telemetry (VK-specific) — RadarFlushSnapshotWorker
// ═══════════════════════════════════════════════════════════════════

/**
 * Matches `RadarFlushSnapshotWorker.b(Lfq0/e;)` — the compiled
 * continuation-based implementation of `doWork()` from the
 * CoroutineWorker superclass.
 *
 * Returning null causes WorkManager to treat it as a failure.
 * After retries, WorkManager gives up — radar snapshots are never sent.
 */
object RadarFlushSnapshotDoWorkFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lru/vk/store/lib/analytics/system/radar/presentation/RadarFlushSnapshotWorker;" &&
                method.name == "b" &&
                method.returnType == "Ljava/lang/Object;" &&
                method.parameterTypes.size == 1
    }
)
