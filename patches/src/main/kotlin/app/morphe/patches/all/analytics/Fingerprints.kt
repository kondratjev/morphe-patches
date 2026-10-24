package app.morphe.patches.all.analytics

import app.morphe.patcher.Fingerprint

// AppMetrica (Yandex) — public API classes

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

// AppMetrica — internal implementation (U1)

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

// MyTracker (VK / Mail.ru)

/**
 * Matches `MyTracker.initTracker(String, Application)` — single
 * initialization method that creates the internal singleton.
 */
object MyTrackerInitFingerprint : Fingerprint(
    definingClass = "Lcom/my/tracker/MyTracker;",
    name = "initTracker",
    returnType = "V",
    custom = { method, _ -> method.implementation != null }
)

// Firebase — Crashlytics & Performance collection switches

/**
 * Matches `FirebaseCrashlytics.setCrashlyticsCollectionEnabled(boolean)` —
 * explicitly enables/disables crash report collection at runtime.
 */
object FirebaseCrashlyticsCollectionFingerprint : Fingerprint(
    definingClass = "Lcom/google/firebase/crashlytics/FirebaseCrashlytics;",
    name = "setCrashlyticsCollectionEnabled",
    returnType = "V",
    parameters = listOf("Z"),
)

/**
 * Matches `FirebasePerformance.setPerformanceCollectionEnabled(boolean)` —
 * explicitly enables/disables Firebase Performance monitoring at runtime.
 */
object FirebasePerformanceCollectionFingerprint : Fingerprint(
    definingClass = "Lcom/google/firebase/perf/FirebasePerformance;",
    name = "setPerformanceCollectionEnabled",
    returnType = "V",
    parameters = listOf("Z"),
)

// Braze

/**
 * Matches `Braze.Companion.configure(Context, BrazeConfig)` — queues the SDK
 * configuration; returning `false` leaves the SDK unconfigured, so nothing is
 * collected or sent.
 */
object BrazeConfigureFingerprint : Fingerprint(
    definingClass = "Lcom/braze/Braze\$Companion;",
    name = "configure",
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;", "Lcom/braze/configuration/BrazeConfig;"),
)

/**
 * Matches `Braze.Companion.enableSdk(Context)` — neutralized so host apps
 * cannot re-enable the SDK after `disableSdk`.
 */
object BrazeEnableSdkFingerprint : Fingerprint(
    definingClass = "Lcom/braze/Braze\$Companion;",
    name = "enableSdk",
    returnType = "V",
    parameters = listOf("Landroid/content/Context;"),
)

/**
 * Matches `Braze.setUserSpecificMemberVariablesAndStartDispatch` — installs the
 * user data model and starts the event dispatch pipeline.
 * The single parameter's type is re-obfuscated every release (`bo.app.*`,
 * `com.braze.managers.*`), so only its arity is pinned.
 */
object BrazeDispatchStartFingerprint : Fingerprint(
    definingClass = "Lcom/braze/Braze;",
    name = "setUserSpecificMemberVariablesAndStartDispatch",
    returnType = "V",
    custom = { method, _ ->
        method.implementation != null && method.parameterTypes.size == 1
    },
)

/**
 * Matches `Braze.openSession(Activity)` — session start reporting.
 */
object BrazeOpenSessionFingerprint : Fingerprint(
    definingClass = "Lcom/braze/Braze;",
    name = "openSession",
    returnType = "V",
    parameters = listOf("Landroid/app/Activity;"),
)

/**
 * Matches `Braze.closeSession(Activity)` — session end reporting.
 */
object BrazeCloseSessionFingerprint : Fingerprint(
    definingClass = "Lcom/braze/Braze;",
    name = "closeSession",
    returnType = "V",
    parameters = listOf("Landroid/app/Activity;"),
)

/**
 * Matches `Braze.changeUser(...)` — user identification (switches the
 * anonymous device user to a named user).
 */
object BrazeChangeUserFingerprint : Fingerprint(
    definingClass = "Lcom/braze/Braze;",
    name = "changeUser",
    returnType = "V",
    custom = { method, _ -> method.implementation != null },
)

/**
 * Matches every `Braze.log*` reporting method — custom events, purchases,
 * ecommerce, banners, feature flags and push attribution. `logout` is excluded.
 */
object BrazeLogEventFingerprint : Fingerprint(
    definingClass = "Lcom/braze/Braze;",
    custom = { method, _ ->
        method.implementation != null &&
            method.name.startsWith("log") &&
            method.name != "logout" &&
            (method.returnType == "V" || method.returnType == "Z")
    },
)
