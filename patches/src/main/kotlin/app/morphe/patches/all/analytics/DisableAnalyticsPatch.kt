package app.morphe.patches.all.analytics

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.Fingerprint
import java.util.logging.Logger

private val logger = Logger.getLogger("DisableAnalytics")

/**
 * Universal bytecode patch that disables analytics and tracking from
 * the most common SDKs found in Android apps.
 *
 * Each SDK is targeted via its public API class and neutralized by
 * inserting an early return at the beginning of the tracking method.
 * All fingerprints use [Fingerprint.methodOrNull] so that the patch
 * gracefully skips SDKs that are not present in the target APK,
 * logging a warning for each skipped SDK.
 *
 * ## SDKs covered
 *
 * ### Tier 1 — Most common (high priority)
 * - **AppMetrica** (Yandex): `io.appmetrica.analytics.AppMetrica`
 * - **MyTracker** (VK / Mail.ru): `com.my.tracker.MyTracker`
 * - **Firebase Analytics** (Google): `com.google.firebase.analytics.FirebaseAnalytics`
 *
 * ### Tier 2 — Common (medium priority)
 * - **Amplitude**: `com.amplitude.api.AmplitudeClient`
 * - **Mixpanel**: `com.mixpanel.android.mpmetrics.MixpanelAPI`
 * - **Adjust**: `com.adjust.sdk.Adjust`
 * - **AppsFlyer**: `com.appsflyer.AppsFlyerLib`
 */
@Suppress("unused")
val disableAnalyticsPatch = bytecodePatch(
    name = "Disable analytics",
    description = "Disables analytics and tracking from multiple SDKs, " +
            "including AppMetrica, MyTracker, Firebase, Amplitude, Mixpanel, " +
            "Adjust, and AppsFlyer",
    default = true,
) {
    execute {
        var patched = 0
        var skipped = 0

        /**
         * Patches a fingerprint if found, or logs a warning if not.
         * Returns true if patched, false if skipped.
         */
        fun Fingerprint.patchOrWarn(name: String, smali: String): Boolean {
            val method = methodOrNull
            return if (method != null) {
                method.addInstructions(0, smali)
                logger.info("Patched: $name")
                true
            } else {
                logger.warning("SDK not found, skipped: $name")
                false
            }
        }

        // ── AppMetrica (Yandex) ───────────────────────────────────
        if (AppMetricaReportEvent1Fingerprint.patchOrWarn("AppMetrica.reportEvent(String)", "return-void")) patched++ else skipped++
        if (AppMetricaReportEvent2Fingerprint.patchOrWarn("AppMetrica.reportEvent(String, String)", "return-void")) patched++ else skipped++
        if (AppMetricaReportEvent3Fingerprint.patchOrWarn("AppMetrica.reportEvent(String, Map)", "return-void")) patched++ else skipped++
        if (AppMetricaSendEventsBufferFingerprint.patchOrWarn("AppMetrica.sendEventsBuffer()", "return-void")) patched++ else skipped++

        // ── AppMetrica ModulesFacade ──────────────────────────────
        if (ModulesFacadeReportEventFingerprint.patchOrWarn("ModulesFacade.reportEvent()", "return-void")) patched++ else skipped++
        if (ModulesFacadeSendEventsBufferFingerprint.patchOrWarn("ModulesFacade.sendEventsBuffer()", "return-void")) patched++ else skipped++

        // ── MyTracker (VK / Mail.ru) ─────────────────────────────
        // Neutralize initTracker so the internal singleton is never
        // created. All track* methods bail out if singleton is null.
        if (MyTrackerInitTrackerFingerprint.patchOrWarn("MyTracker.initTracker()", "return-void")) patched++ else skipped++

        // ── Firebase Analytics (Google) ──────────────────────────
        if (FirebaseAnalyticsLogEventFingerprint.patchOrWarn("FirebaseAnalytics.logEvent()", "return-void")) patched++ else skipped++

        // ── Amplitude ────────────────────────────────────────────
        if (AmplitudeLogEventFingerprint.patchOrWarn("AmplitudeClient.logEvent()", "return-void")) patched++ else skipped++

        // ── Mixpanel ─────────────────────────────────────────────
        if (MixpanelTrackFingerprint.patchOrWarn("MixpanelAPI.track()", "return-void")) patched++ else skipped++

        // ── Adjust ───────────────────────────────────────────────
        if (AdjustTrackEventFingerprint.patchOrWarn("Adjust.trackEvent()", "return-void")) patched++ else skipped++

        // ── AppsFlyer ────────────────────────────────────────────
        if (AppsFlyerTrackEventFingerprint.patchOrWarn("AppsFlyerLib.trackEvent()", "return-void")) patched++ else skipped++

        // ── Summary ──────────────────────────────────────────────
        logger.info("Analytics patch summary: $patched patched, $skipped skipped")
    }
}
