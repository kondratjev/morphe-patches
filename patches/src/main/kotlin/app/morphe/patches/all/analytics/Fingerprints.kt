package app.morphe.patches.all.analytics

import app.morphe.patcher.Fingerprint

// ═══════════════════════════════════════════════════════════════════
// AppMetrica (Yandex) — public API classes
// ═══════════════════════════════════════════════════════════════════

/**
 * Matches all void methods in AppMetrica public API classes.
 * These classes expose `reportEvent`, `sendEventsBuffer`, `activate`,
 * and other entry points that route to the internal implementation.
 */
object AppMetricaPublicApiFingerprint : Fingerprint(
    returnType = "V",
    custom = { method, classDef ->
        (classDef.type == "Lcom/yandex/metrica/YandexMetrica;" ||
            classDef.type == "Lcom/yandex/metrica/AppMetricaJsInterface;" ||
            classDef.type == "Lcom/yandex/metrica/AppMetricaInitializerJsInterface;") &&
            method.name != "<init>" &&
            method.implementation != null
    }
)

// ═══════════════════════════════════════════════════════════════════
// AppMetrica — internal implementation (U1)
// ═══════════════════════════════════════════════════════════════════

/**
 * Matches `U1.reportData()` and `U1.sendCrash()` — void methods
 * that process queued analytics data and crash reports.
 */
object AppMetricaInternalReportFingerprint : Fingerprint(
    definingClass = "U1;",
    returnType = "V",
    custom = { method, _ ->
        method.name in setOf("reportData", "sendCrash") &&
            method.implementation != null
    }
)

/**
 * Matches `U1.queuePauseUserSession()`, `U1.queueReport()`,
 * `U1.queueResumeUserSession()` — Future-returning methods that
 * enqueue analytics reports for background processing.
 */
object AppMetricaInternalQueueFingerprint : Fingerprint(
    definingClass = "U1;",
    returnType = "Ljava/util/concurrent/Future;",
    custom = { method, _ ->
        method.name in setOf("queuePauseUserSession", "queueReport", "queueResumeUserSession") &&
            method.implementation != null
    }
)

/**
 * Matches `U1$g.call()` — inner callback class with Void return type.
 * Used internally by AppMetrica for async task execution.
 */
object AppMetricaInternalCallbackFingerprint : Fingerprint(
    definingClass = "U1\$g;",
    name = "call",
    returnType = "Ljava/lang/Void;",
    custom = { method, _ -> method.implementation != null },
)

// ═══════════════════════════════════════════════════════════════════
// MyTracker (VK / Mail.ru)
// ═══════════════════════════════════════════════════════════════════

/**
 * Matches `MyTracker.initTracker(String, Application)` — the single
 * initialization method that creates the internal singleton. All
 * tracking methods (trackEvent, trackAdEvent, etc.) check this
 * singleton first — if it's null, they bail out immediately.
 */
object MyTrackerInitFingerprint : Fingerprint(
    definingClass = "MyTracker;",
    name = "initTracker",
    returnType = "V",
    custom = { method, _ -> method.implementation != null },
)
