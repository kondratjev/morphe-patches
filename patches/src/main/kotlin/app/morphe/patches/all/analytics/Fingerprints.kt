package app.morphe.patches.all.analytics

import app.morphe.patcher.Fingerprint

// ═══════════════════════════════════════════════════════════════════
// AppMetrica (Yandex) — io.appmetrica.analytics
// ═══════════════════════════════════════════════════════════════════

/**
 * Matches `AppMetrica.reportEvent(String)` — the single-argument overload
 * that reports a custom event to Yandex AppMetrica.
 */
object AppMetricaReportEvent1Fingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lio/appmetrica/analytics/AppMetrica;" &&
                method.name == "reportEvent" &&
                method.parameterTypes.size == 1 &&
                method.parameterTypes[0] == "Ljava/lang/String;"
    }
)

/**
 * Matches `AppMetrica.reportEvent(String, String)` — the two-argument
 * overload with event name and JSON string value.
 */
object AppMetricaReportEvent2Fingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lio/appmetrica/analytics/AppMetrica;" &&
                method.name == "reportEvent" &&
                method.parameterTypes.size == 2 &&
                method.parameterTypes[0] == "Ljava/lang/String;" &&
                method.parameterTypes[1] == "Ljava/lang/String;"
    }
)

/**
 * Matches `AppMetrica.reportEvent(String, Map)` — the two-argument
 * overload with event name and attributes map.
 */
object AppMetricaReportEvent3Fingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lio/appmetrica/analytics/AppMetrica;" &&
                method.name == "reportEvent" &&
                method.parameterTypes.size == 2 &&
                method.parameterTypes[0] == "Ljava/lang/String;" &&
                method.parameterTypes[1] == "Ljava/util/Map;"
    }
)

/**
 * Matches `AppMetrica.sendEventsBuffer()` — forces any buffered
 * events to be sent immediately. Neutralizing this prevents
 * on-demand event flushing.
 */
object AppMetricaSendEventsBufferFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lio/appmetrica/analytics/AppMetrica;" &&
                method.name == "sendEventsBuffer" &&
                method.parameterTypes.isEmpty()
    }
)

// ═══════════════════════════════════════════════════════════════════
// AppMetrica ModulesFacade — module-level analytics API
// ═══════════════════════════════════════════════════════════════════

/**
 * Matches `ModulesFacade.reportEvent(ModuleEvent)` — the module-level
 * event reporting entry point that routes through the same internal
 * implementation as AppMetrica.reportEvent.
 */
object ModulesFacadeReportEventFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lio/appmetrica/analytics/ModulesFacade;" &&
                method.name == "reportEvent" &&
                method.parameterTypes.size == 1 &&
                method.parameterTypes[0] == "Lio/appmetrica/analytics/ModuleEvent;"
    }
)

/**
 * Matches `ModulesFacade.sendEventsBuffer()` — module-level event
 * buffer flushing. Delegates to AppMetrica.sendEventsBuffer().
 */
object ModulesFacadeSendEventsBufferFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lio/appmetrica/analytics/ModulesFacade;" &&
                method.name == "sendEventsBuffer" &&
                method.parameterTypes.isEmpty()
    }
)

// ═══════════════════════════════════════════════════════════════════
// MyTracker (VK / Mail.ru) — com.my.tracker
// ═══════════════════════════════════════════════════════════════════

/**
 * Matches `MyTracker.initTracker(String, Application)` — the single
 * initialization method that creates the internal [c1] singleton.
 *
 * All tracking methods (trackEvent, trackAdEvent, trackPurchaseEvent,
 * trackLoginEvent, trackRegistrationEvent, trackInviteEvent,
 * trackLevelEvent, trackMiniAppEvent) check whether the singleton is
 * initialized first. By neutralizing initTracker, the singleton never
 * gets created, and all tracking calls bail out immediately with a
 * log message.
 *
 * This single fingerprint covers *all* MyTracker tracking methods.
 */
object MyTrackerInitTrackerFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/my/tracker/MyTracker;" &&
                method.name == "initTracker" &&
                method.parameterTypes.size == 2 &&
                method.parameterTypes[0] == "Ljava/lang/String;"
    }
)

// ═══════════════════════════════════════════════════════════════════
// Firebase Analytics — com.google.firebase.analytics
// ═══════════════════════════════════════════════════════════════════

/**
 * Matches `FirebaseAnalytics.logEvent(String, Bundle)` — the primary
 * event logging method for Google Firebase Analytics.
 */
object FirebaseAnalyticsLogEventFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/google/firebase/analytics/FirebaseAnalytics;" &&
                method.name == "logEvent" &&
                method.parameterTypes.size == 2 &&
                method.parameterTypes[0] == "Ljava/lang/String;"
    }
)

// ═══════════════════════════════════════════════════════════════════
// Amplitude — com.amplitude.api
// ═══════════════════════════════════════════════════════════════════

/**
 * Matches `AmplitudeClient.logEvent(String, JSONObject)` — the
 * two-argument overload that is the core implementation. The single-
 * argument `logEvent(String)` delegates to this method.
 */
object AmplitudeLogEventFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/amplitude/api/AmplitudeClient;" &&
                method.name == "logEvent" &&
                method.parameterTypes.size == 2 &&
                method.parameterTypes[0] == "Ljava/lang/String;"
    }
)

// ═══════════════════════════════════════════════════════════════════
// Mixpanel — com.mixpanel.android.mpmetrics
// ═══════════════════════════════════════════════════════════════════

/**
 * Matches `MixpanelAPI.track(String, JSONObject)` — the two-argument
 * overload that is the core implementation. The single-argument
 * `track(String)` delegates to this method.
 */
object MixpanelTrackFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/mixpanel/android/mpmetrics/MixpanelAPI;" &&
                method.name == "track" &&
                method.parameterTypes.size == 2 &&
                method.parameterTypes[0] == "Ljava/lang/String;"
    }
)

// ═══════════════════════════════════════════════════════════════════
// Adjust — com.adjust.sdk
// ═══════════════════════════════════════════════════════════════════

/**
 * Matches `Adjust.trackEvent(AdjustEvent)` — the single event
 * tracking method in the Adjust attribution SDK.
 */
object AdjustTrackEventFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/adjust/sdk/Adjust;" &&
                method.name == "trackEvent" &&
                method.parameterTypes.size == 1
    }
)

// ═══════════════════════════════════════════════════════════════════
// AppsFlyer — com.appsflyer
// ═══════════════════════════════════════════════════════════════════

/**
 * Matches `AppsFlyerLib.trackEvent(Context, String, Map)` — the
 * primary event tracking method in the AppsFlyer attribution SDK.
 */
object AppsFlyerTrackEventFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/appsflyer/AppsFlyerLib;" &&
                method.name == "trackEvent" &&
                method.parameterTypes.size == 3
    }
)


