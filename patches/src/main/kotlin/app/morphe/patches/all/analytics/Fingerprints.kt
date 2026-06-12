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
    custom = { method, classDef ->
        (classDef.type == "Lcom/yandex/metrica/YandexMetrica;" ||
            classDef.type == "Lcom/yandex/metrica/AppMetricaJsInterface;" ||
            classDef.type == "Lcom/yandex/metrica/AppMetricaInitializerJsInterface;") &&
            method.name != "<init>" &&
            method.returnType == "V" &&
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
    custom = { method, classDef ->
        classDef.type == "Lcom/yandex/metrica/impl/ob/U1;" &&
            method.name in setOf("reportData", "sendCrash") &&
            method.returnType == "V" &&
            method.implementation != null
    }
)

/**
 * Matches `U1.queuePauseUserSession()`, `U1.queueReport()`,
 * `U1.queueResumeUserSession()` — Future-returning methods that
 * enqueue analytics reports for background processing.
 */
object AppMetricaInternalQueueFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/yandex/metrica/impl/ob/U1;" &&
            method.name in setOf("queuePauseUserSession", "queueReport", "queueResumeUserSession") &&
            method.returnType == "Ljava/util/concurrent/Future;" &&
            method.implementation != null
    }
)

/**
 * Matches `U1$g.call()` — inner callback class with Void return type.
 * Used internally by AppMetrica for async task execution.
 */
object AppMetricaInternalCallbackFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/yandex/metrica/impl/ob/U1\$g;" &&
            method.name == "call" &&
            method.returnType == "Ljava/lang/Void;" &&
            method.implementation != null
    }
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
    custom = { method, classDef ->
        classDef.type == "Lcom/my/tracker/MyTracker;" &&
            method.name == "initTracker" &&
            method.returnType == "V" &&
            method.implementation != null
    }
)
