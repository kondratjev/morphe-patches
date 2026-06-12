package app.morphe.patches.all.analytics

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import org.w3c.dom.Element

// ═════════════════════════════════════════════════════════════════
// Manifest — disables analytics components & sets opt-out metadata
// ═════════════════════════════════════════════════════════════════

private val disableAnalyticsManifestPatch = resourcePatch {
    execute {
        document("AndroidManifest.xml").use { document ->
            val manifest = document.documentElement
            val application = manifest.childrenNamed("application").single() as Element
            var disabled = 0

            // AppMetrica (Yandex)
            val appMetrica: (String) -> Boolean = {
                it.startsWith("io.appmetrica.analytics.") ||
                    it.startsWith("com.yandex.metrica.") ||
                    it.startsWith("com.yandex.preinstallsatellite.appmetrica.")
            }
            application.removeChildren(
                application.childrenNamed("activity", "provider", "service", "receiver")
                    .filter { appMetrica(it.getAttribute("android:name")) },
            )
            disabled += application.disableComponentsWhere(appMetrica)
            application.setApplicationMetaData("io.appmetrica.analytics.auto_tracking_enabled", "false")
            application.setApplicationMetaData("io.appmetrica.analytics.location_tracking_enabled", "false")

            // MyTracker (VK / Mail.ru)
            disabled += application.disableComponentsWhere {
                it.startsWith("com.my.tracker.") ||
                    it.startsWith("ru.mail.mytracker.") ||
                    it.contains(".mytracker.", ignoreCase = true)
            }

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
            disabled += application.disableComponentsByName(
                "com.google.android.datatransport.runtime.backends.TransportBackendDiscovery",
                "com.google.android.datatransport.runtime.scheduling.jobscheduling.JobInfoSchedulerService",
                "com.google.android.datatransport.runtime.scheduling.jobscheduling.AlarmManagerSchedulerBroadcastReceiver",
                "com.google.firebase.sessions.SessionLifecycleService",
            )

            // Google Analytics (legacy)
            disabled += application.disableComponentsByPrefix(
                "com.google.android.gms.analytics.",
                "com.google.android.gms.tagmanager.",
            )

            // Sentry
            application.setApplicationMetaData("io.sentry.enabled", "false")
            application.setApplicationMetaData("io.sentry.dsn", "")
            disabled += application.disableComponentsWhere {
                it.startsWith("io.sentry.") || it.contains(".Sentry")
            }

            // Adjust — remove permissions + disable components
            manifest.removeChildren(
                manifest.childrenNamed("uses-permission")
                    .filter { it.getAttribute("android:name").startsWith("com.adjust.") },
            )

            // AppsFlyer — remove permission + disable components
            manifest.removeChildren(
                manifest.childrenNamed("uses-permission")
                    .filter { it.getAttribute("android:name") == "com.appsflyer.referrer.INSTALL_PROVIDER" },
            )

            // Adjust, AppsFlyer, Amplitude, Mixpanel — disable by prefix
            for (prefix in listOf("com.adjust.", "com.appsflyer.", "com.amplitude.", "com.mixpanel.")) {
                disabled += application.disableComponentsByPrefix(prefix)
            }

            println("Disable analytics: disabled $disabled manifest components.")
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
        AppMetricaPublicApiFingerprint.methodOrNull?.addInstructions(0, "return-void")

        // AppMetrica internal — reportData / sendCrash
        AppMetricaInternalReportFingerprint.methodOrNull?.addInstructions(0, "return-void")

        // AppMetrica internal — queue* Future methods → return completedFuture(null)
        AppMetricaInternalQueueFingerprint.methodOrNull?.addInstructions(
            0,
            """
                const/4 p0, 0x0
                invoke-static {p0}, Ljava/util/concurrent/CompletableFuture;->completedFuture(Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;
                move-result-object p0
                return-object p0
            """,
        )

        // AppMetrica internal — U1$g.call() → return null
        AppMetricaInternalCallbackFingerprint.methodOrNull?.addInstructions(
            0,
            "const/4 p0, 0x0\nreturn-object p0",
        )

        // MyTracker — initTracker (prevents singleton creation)
        MyTrackerInitFingerprint.methodOrNull?.addInstructions(0, "return-void")
    }
}
