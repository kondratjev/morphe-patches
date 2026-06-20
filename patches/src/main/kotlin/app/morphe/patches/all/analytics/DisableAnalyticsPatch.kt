package app.morphe.patches.all.analytics

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import app.morphe.util.returnEarly
import org.w3c.dom.Element
import java.util.logging.Logger

private val logger = Logger.getLogger("DisableAnalytics")

// Manifest — disables analytics components & sets opt-out metadata

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
            val amFound = application.childrenNamed("activity", "provider", "service", "receiver")
                .any { appMetrica(it.getAttribute("android:name")) }
            application.removeChildren(
                application.childrenNamed("activity", "provider", "service", "receiver")
                    .filter { appMetrica(it.getAttribute("android:name")) },
            )
            application.disableComponentsWhere(appMetrica)
            application.setApplicationMetaData("io.appmetrica.analytics.auto_tracking_enabled", "false")
            application.setApplicationMetaData("io.appmetrica.analytics.location_tracking_enabled", "false")
            logger.info("AppMetrica: ${if (amFound) "patched" else "not found"}")

            // MyTracker (VK / Mail.ru)
            val mtFound = application.disableComponentsWhere {
                it.startsWith("com.my.tracker.") ||
                    it.startsWith("ru.mail.mytracker.") ||
                    it.contains(".mytracker.", ignoreCase = true)
            } > 0
            logger.info("MyTracker: ${if (mtFound) "patched" else "not found"}")

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
            application.disableComponentsByName(
                "com.google.android.datatransport.runtime.backends.TransportBackendDiscovery",
                "com.google.android.datatransport.runtime.scheduling.jobscheduling.JobInfoSchedulerService",
                "com.google.android.datatransport.runtime.scheduling.jobscheduling.AlarmManagerSchedulerBroadcastReceiver",
                "com.google.firebase.sessions.SessionLifecycleService",
            )
            logger.info("Firebase: patched")

            // Google Analytics (legacy)
            val gaFound = application.disableComponentsByPrefix(
                "com.google.android.gms.analytics.",
                "com.google.android.gms.tagmanager.",
            ) > 0
            logger.info("Google Analytics: ${if (gaFound) "patched" else "not found"}")

            // Sentry
            application.setApplicationMetaData("io.sentry.enabled", "false")
            application.setApplicationMetaData("io.sentry.dsn", "")
            val sentryFound = application.disableComponentsWhere {
                it.startsWith("io.sentry.") || it.contains(".Sentry")
            } > 0
            logger.info("Sentry: ${if (sentryFound) "patched" else "not found"}")

            // Adjust
            manifest.removeChildren(
                manifest.childrenNamed("uses-permission")
                    .filter { it.getAttribute("android:name").startsWith("com.adjust.") },
            )
            val adjFound = application.disableComponentsByPrefix("com.adjust.") > 0
            logger.info("Adjust: ${if (adjFound) "patched" else "not found"}")

            // AppsFlyer
            manifest.removeChildren(
                manifest.childrenNamed("uses-permission")
                    .filter { it.getAttribute("android:name") == "com.appsflyer.referrer.INSTALL_PROVIDER" },
            )
            val afFound = application.disableComponentsByPrefix("com.appsflyer.") > 0
            logger.info("AppsFlyer: ${if (afFound) "patched" else "not found"}")

            // Facebook
            application.setApplicationMetaData("com.facebook.sdk.AutoLogAppEventsEnabled", "false")
            application.setApplicationMetaData("com.facebook.sdk.AdvertiserIDCollectionEnabled", "false")
            application.disableComponentsByPrefix("com.facebook.appevents.")
            val fbFound = application.disableComponentsByPrefix("com.facebook.analytics.") > 0
            logger.info("Facebook: ${if (fbFound) "patched" else "not found"}")

            // MoEngage
            application.setApplicationMetaData("com_moengage_core_file_based_initialisation_enabled", "false")
            application.setApplicationMetaData("com_moengage_core_background_data_sync_enabled", "false")
            application.setApplicationMetaData("com_moengage_core_carrier_tracking_enabled", "false")
            application.setApplicationMetaData("com_moengage_core_device_attribute_tracking_enabled", "false")
            application.setApplicationMetaData("com_moengage_core_user_registration_enabled", "false")
            application.setApplicationMetaData("com_moengage_fcm_registration_enabled", "false")
            val moFound = application.disableComponentsByPrefix("com.moengage.") > 0
            logger.info("MoEngage: ${if (moFound) "patched" else "not found"}")

            // comScore
            val csFound = application.disableComponentsByPrefix("com.comscore.") > 0
            logger.info("comScore: ${if (csFound) "patched" else "not found"}")

            // Amplitude
            val ampFound = application.disableComponentsByPrefix("com.amplitude.") > 0
            logger.info("Amplitude: ${if (ampFound) "patched" else "not found"}")

            // Mixpanel
            val mpFound = application.disableComponentsByPrefix("com.mixpanel.") > 0
            logger.info("Mixpanel: ${if (mpFound) "patched" else "not found"}")
        }
    }
}

// Bytecode — neutralizes analytics entry-point methods

@Suppress("unused")
val disableAnalyticsPatch = bytecodePatch(
    name = "Disable analytics",
    description = "Disables analytics and tracking from multiple SDKs, " +
        "including AppMetrica, MyTracker, Firebase, Sentry, Google Analytics, " +
        "Amplitude, Mixpanel, Adjust, AppsFlyer, Facebook, MoEngage, and comScore.",
    default = true,
) {
    dependsOn(disableAnalyticsManifestPatch)

    execute {
        AppMetricaPublicApiFingerprint.methodOrNull
            ?.returnEarly()
            .also { logger.info("AppMetrica public API: ${if (it != null) "patched" else "not found"}") }

        AppMetricaInternalReportFingerprint.methodOrNull
            ?.returnEarly()
            .also { logger.info("AppMetrica internal: ${if (it != null) "patched" else "not found"}") }

        AppMetricaInternalQueueFingerprint.methodOrNull
            ?.addInstructions(
                0,
                """
                    const/4 p0, 0x0
                    invoke-static {p0}, Ljava/util/concurrent/CompletableFuture;->completedFuture(Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;
                    move-result-object p0
                    return-object p0
                """,
            )
            .also { logger.info("AppMetrica queue: ${if (it != null) "patched" else "not found"}") }

        AppMetricaInternalCallbackFingerprint.methodOrNull
            ?.addInstructions(0, "const/4 p0, 0x0\nreturn-object p0")
            .also { logger.info("AppMetrica callback: ${if (it != null) "patched" else "not found"}") }

        MyTrackerInitFingerprint.methodOrNull
            ?.returnEarly()
            .also { logger.info("MyTracker: ${if (it != null) "patched" else "not found"}") }

        FirebaseCrashlyticsCollectionFingerprint.methodOrNull
            ?.returnEarly()
            .also { logger.info("Firebase Crashlytics collection: ${if (it != null) "patched" else "not found"}") }

        FirebasePerformanceCollectionFingerprint.methodOrNull
            ?.returnEarly()
            .also { logger.info("Firebase Performance collection: ${if (it != null) "patched" else "not found"}") }
    }
}
