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
 * Matches `Braze.Companion.configure(Context, BrazeConfig)` — the single entry
 * point that queues the SDK configuration (v42+ "delayed initialization" just
 * stores the config; the instance is created lazily by `getInstance`).
 * Returning `false` without queueing leaves any lazily-created instance with
 * an empty API key, which the SDK tolerates: `verifyProperSdkSetup` only logs
 * a warning ("requires a non-empty API key") and returns.
 */
object BrazeConfigureFingerprint : Fingerprint(
    definingClass = "Lcom/braze/Braze\$Companion;",
    name = "configure",
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;", "Lcom/braze/configuration/BrazeConfig;"),
)

/**
 * Matches `Braze.Companion.enableSdk(Context)` — re-enables the SDK after a
 * `disableSdk` call (host apps toggle this on login). Neutralized so the SDK
 * cannot be re-enabled at runtime; `disableSdk` is left intact.
 */
object BrazeEnableSdkFingerprint : Fingerprint(
    definingClass = "Lcom/braze/Braze\$Companion;",
    name = "enableSdk",
    returnType = "V",
    parameters = listOf("Landroid/content/Context;"),
)

/**
 * Matches `Braze.setUserSpecificMemberVariablesAndStartDispatch(...)` —
 * installs the UDM and starts the dispatch worker that performs device
 * registration and ships queued events/requests. Called at the tail of
 * `getInstance`'s init sequence (only logging follows), and every later `udm`
 * read in the public API is guarded by the SDK's own
 * `earlyReturnIfUdmUninitialized`. Returning early therefore parks the SDK in
 * its tolerated "not started" state: no dispatch, no device registration,
 * no outbound requests. The internal parameter type (`bo.app.*`) is
 * intentionally not pinned — it is minified and changes between SDK versions.
 */
object BrazeDispatchStartFingerprint : Fingerprint(
    definingClass = "Lcom/braze/Braze;",
    name = "setUserSpecificMemberVariablesAndStartDispatch",
    returnType = "V",
    custom = { method, _ -> method.implementation != null },
)

/**
 * Matches `Braze.openSession(Activity)` — session start from the automatic
 * activity lifecycle tracking.
 */
object BrazeOpenSessionFingerprint : Fingerprint(
    definingClass = "Lcom/braze/Braze;",
    name = "openSession",
    returnType = "V",
    parameters = listOf("Landroid/app/Activity;"),
)

/**
 * Matches `Braze.closeSession(Activity)` — session end from the automatic
 * activity lifecycle tracking.
 */
object BrazeCloseSessionFingerprint : Fingerprint(
    definingClass = "Lcom/braze/Braze;",
    name = "closeSession",
    returnType = "V",
    parameters = listOf("Landroid/app/Activity;"),
)

/**
 * Matches all `Braze.logCustomEvent(...)` overloads — custom event reporting.
 */
object BrazeLogCustomEventFingerprint : Fingerprint(
    definingClass = "Lcom/braze/Braze;",
    name = "logCustomEvent",
    returnType = "V",
    custom = { method, _ -> method.implementation != null },
)

/**
 * Matches all `Braze.logPurchase(...)` overloads — purchase reporting.
 */
object BrazeLogPurchaseFingerprint : Fingerprint(
    definingClass = "Lcom/braze/Braze;",
    name = "logPurchase",
    returnType = "V",
    custom = { method, _ -> method.implementation != null },
)

/**
 * Matches all `Braze.changeUser(...)` overloads — user identification
 * (switches the anonymous device user to a named user).
 */
object BrazeChangeUserFingerprint : Fingerprint(
    definingClass = "Lcom/braze/Braze;",
    name = "changeUser",
    returnType = "V",
    custom = { method, _ -> method.implementation != null },
)

/**
 * Matches `Braze.logPushNotificationOpened(Intent)` — push open/click
 * attribution reporting.
 */
object BrazeLogPushNotificationOpenedFingerprint : Fingerprint(
    definingClass = "Lcom/braze/Braze;",
    name = "logPushNotificationOpened",
    returnType = "V",
    parameters = listOf("Landroid/content/Intent;"),
)
