package app.morphe.patches.all.analytics

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import org.w3c.dom.Element
import java.util.logging.Logger

private val logger = Logger.getLogger("DisableAnalytics")

// ═════════════════════════════════════════════════════════════════
// Manifest — disables analytics components & sets opt-out metadata
// ═════════════════════════════════════════════════════════════════

private val disableAnalyticsManifestPatch = resourcePatch {
    execute {
        document("AndroidManifest.xml").use { document ->
            val manifest = document.documentElement
            val application = manifest.childrenNamed("application").single() as Element

            // AppMetrica (Yandex)
            val appMetrica: (String) -> Boolean = {
                it.startsWith("io.appmetrica.analytics.") ||
                    it.startsWith("com.yandex.metrica.") ||
                    it.startsWith("com.yandex.preinstallsatellite.appmetrica.")
            }
            val amRemoved = application.childrenNamed("activity", "provider", "service", "receiver")
                .filter { appMetrica(it.getAttribute("android:name")) }
            application.removeChildren(amRemoved)
            val amDisabled = application.disableComponentsWhere(appMetrica)
            application.setApplicationMetaData("io.appmetrica.analytics.auto_tracking_enabled", "false")
            application.setApplicationMetaData("io.appmetrica.analytics.location_tracking_enabled", "false")
            logger.info("AppMetrica: removed ${amRemoved.size}, disabled $amDisabled components")

            // MyTracker (VK / Mail.ru)
            val mtDisabled = application.disableComponentsWhere {
                it.startsWith("com.my.tracker.") ||
                    it.startsWith("ru.mail.mytracker.") ||
                    it.contains(".mytracker.", ignoreCase = true)
            }
            logger.info("MyTracker: disabled $mtDisabled components")

            // Firebase Analytics (Google)
            mapOf(
                "firebase_analytics_collection_enabled" to "false",
                "firebase_crashlytics_collection_enabled" to "false",
                "firebase_performance_collection_enabled" to "false",
                "firebase_performance_logcat_enabled" to "false",
                "firebase_data_collection_default_enabled" to "false",
                "google_analytics_adid_collection_enabled" to "false",
                "google_analytics_deferred_deep_link_enabled" to "false",
            ).forEach { (k, v) -> application.setApplicationMetaData(k, v) }
            val fbDisabled = application.disableComponentsByName(
                "com.google.android.datatransport.runtime.backends.TransportBackendDiscovery",
                "com.google.android.datatransport.runtime.scheduling.jobscheduling.JobInfoSchedulerService",
                "com.google.android.datatransport.runtime.scheduling.jobscheduling.AlarmManagerSchedulerBroadcastReceiver",
                "com.google.firebase.sessions.SessionLifecycleService",
            )
            logger.info("Firebase: disabled $fbDisabled components")

            // Google Analytics (legacy)
            val gaDisabled = application.disableComponentsByPrefix(
                "com.google.android.gms.analytics.",
                "com.google.android.gms.tagmanager.",
            )
            logger.info("Google Analytics: disabled $gaDisabled components")

            // Sentry
            application.setApplicationMetaData("io.sentry.enabled", "false")
            application.setApplicationMetaData("io.sentry.dsn", "")
            val sentryDisabled = application.disableComponentsWhere {
                it.startsWith("io.sentry.") || it.contains(".Sentry")
            }
            logger.info("Sentry: disabled $sentryDisabled components")

            // Adjust
            val adjPerms = manifest.childrenNamed("uses-permission")
                .filter { it.getAttribute("android:name").startsWith("com.adjust.") }
            manifest.removeChildren(adjPerms)
            val adjDisabled = application.disableComponentsByPrefix("com.adjust.")
            logger.info("Adjust: removed ${adjPerms.size} permissions, disabled $adjDisabled components")

            // AppsFlyer
            val afPerms = manifest.childrenNamed("uses-permission")
                .filter { it.getAttribute("android:name") == "com.appsflyer.referrer.INSTALL_PROVIDER" }
            manifest.removeChildren(afPerms)
            val afDisabled = application.disableComponentsByPrefix("com.appsflyer.")
            logger.info("AppsFlyer: removed ${afPerms.size} permissions, disabled $afDisabled components")

            // Amplitude
            val ampDisabled = application.disableComponentsByPrefix("com.amplitude.")
            logger.info("Amplitude: disabled $ampDisabled components")

            // Mixpanel
            val mpDisabled = application.disableComponentsByPrefix("com.mixpanel.")
            logger.info("Mixpanel: disabled $mpDisabled components")
        }
    }
}

// ═════════════════════════════════════════════════════════════════
// Bytecode — neutralizes analytics entry-point methods
// ═════════════════════════════════════════════════════════════════

@Suppress("unused")
val disableAnalyticsPatch = bytecodePatch(
    name = "Disable analytics",
    description = "Disables analytics and tracking from multiple SDKs, " +
        "including AppMetrica, MyTracker, Firebase, Sentry, Google Analytics, " +
        "Amplitude, Mixpanel, Adjust, and AppsFlyer.",
    default = true,
) {
    dependsOn(disableAnalyticsManifestPatch)

    execute {
        // AppMetrica public API — all void methods
        if (AppMetricaPublicApiFingerprint.methodOrNull != null) {
            AppMetricaPublicApiFingerprint.method.addInstructions(0, "return-void")
            logger.info("Patched AppMetrica public API")
        } else {
            logger.info("Skipped AppMetrica public API (not found)")
        }

        // AppMetrica internal — reportData / sendCrash
        if (AppMetricaInternalReportFingerprint.methodOrNull != null) {
            AppMetricaInternalReportFingerprint.method.addInstructions(0, "return-void")
            logger.info("Patched AppMetrica internal (reportData/sendCrash)")
        } else {
            logger.info("Skipped AppMetrica internal (not found)")
        }

        // AppMetrica internal — queue* Future methods
        if (AppMetricaInternalQueueFingerprint.methodOrNull != null) {
            AppMetricaInternalQueueFingerprint.method.addInstructions(
                0,
                """
                    const/4 p0, 0x0
                    invoke-static {p0}, Ljava/util/concurrent/CompletableFuture;->completedFuture(Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;
                    move-result-object p0
                    return-object p0
                """,
            )
            logger.info("Patched AppMetrica internal (queue*)")
        } else {
            logger.info("Skipped AppMetrica internal (queue*) (not found)")
        }

        // AppMetrica internal — U1$g.call()
        if (AppMetricaInternalCallbackFingerprint.methodOrNull != null) {
            AppMetricaInternalCallbackFingerprint.method.addInstructions(0, "const/4 p0, 0x0\nreturn-object p0")
            logger.info("Patched AppMetrica internal (U1\$g callback)")
        } else {
            logger.info("Skipped AppMetrica internal (U1\$g callback) (not found)")
        }

        // MyTracker — initTracker
        if (MyTrackerInitFingerprint.methodOrNull != null) {
            MyTrackerInitFingerprint.method.addInstructions(0, "return-void")
            logger.info("Patched MyTracker initTracker")
        } else {
            logger.info("Skipped MyTracker initTracker (not found)")
        }
    }
}
