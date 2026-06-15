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
    definingClass = "Lcom/yandex/metrica/impl/ob/U1;",
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
    definingClass = "Lcom/yandex/metrica/impl/ob/U1;",
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
    definingClass = "Lcom/yandex/metrica/impl/ob/U1\$g;",
    name = "call",
    returnType = "Ljava/lang/Void;",
    custom = { method, _ -> method.implementation != null }
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
    definingClass = "Lcom/my/tracker/MyTracker;",
    name = "initTracker",
    returnType = "V",
    custom = { method, _ -> method.implementation != null }
)

// ═══════════════════════════════════════════════════════════════════
// Firebase — Crashlytics & Performance collection switches
// ═══════════════════════════════════════════════════════════════════

/**
 * Matches `FirebaseCrashlytics.setCrashlyticsCollectionEnabled(boolean)` —
 * explicitly enables or disables crash report collection at runtime.
 * Some apps (e.g. Pillo) set this to `true` in an Initializer, overriding
 * the manifest meta-data `firebase_crashlytics_collection_enabled=false`.
 */
object FirebaseCrashlyticsCollectionFingerprint : Fingerprint(
    definingClass = "Lcom/google/firebase/crashlytics/FirebaseCrashlytics;",
    name = "setCrashlyticsCollectionEnabled",
    returnType = "V",
    parameters = listOf("Z"),
)

/**
 * Matches `FirebasePerformance.setPerformanceCollectionEnabled(boolean)` —
 * explicitly enables or disables Firebase Performance monitoring at runtime.
 * Same pattern as Crashlytics — some apps override the manifest default.
 */
object FirebasePerformanceCollectionFingerprint : Fingerprint(
    definingClass = "Lcom/google/firebase/perf/FirebasePerformance;",
    name = "setPerformanceCollectionEnabled",
    returnType = "V",
    parameters = listOf("Z"),
)
