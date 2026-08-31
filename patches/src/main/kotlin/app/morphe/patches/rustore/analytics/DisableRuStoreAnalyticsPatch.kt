package app.morphe.patches.rustore.analytics

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patches.all.analytics.childrenNamed
import app.morphe.patches.all.analytics.disableComponentsByPrefix
import app.morphe.patches.all.analytics.removeChildren
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE
import app.morphe.util.returnEarly
import org.w3c.dom.Element
import java.util.logging.Logger

private val logger = Logger.getLogger("DisableRuStoreAnalytics")

// Manifest — disables VK-specific analytics components

private val disableRuStoreAnalyticsManifestPatch = resourcePatch {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        document("AndroidManifest.xml").use { document ->
            val application = document.documentElement.childrenNamed("application").single() as Element

            application.removeChildren(
                application.childrenNamed("meta-data")
                    .filter { it.getAttribute("android:name").startsWith("ru.ok.tracer.startup.Initializer@") }
            )
            val tracerDisabled = application.disableComponentsByPrefix("ru.ok.tracer.")
            logger.info("OK Tracer: disabled $tracerDisabled components")

            val radarDisabled = application.disableComponentsByPrefix("ru.rustore.sdk.metrics.")
            logger.info("RuStore Metrics: disabled $radarDisabled components")
        }
    }
}

// Bytecode — neutralizes VK-specific analytics entry points

@Suppress("unused")
val disableRuStoreAnalyticsPatch = bytecodePatch(
    name = "Disable analytics",
    description = "Disables VK-specific analytics.",
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    dependsOn(disableRuStoreAnalyticsManifestPatch)

    execute {
        if (AltCraftSendFingerprint.methodOrNull != null) {
            AltCraftSendFingerprint.method.returnEarly(null as Void?)
            logger.info("Patched AltCraft send method")
        } else {
            logger.info("Skipped AltCraft (not found)")
        }

        if (RadarDoWorkFingerprint.methodOrNull != null) {
            RadarDoWorkFingerprint.method.returnEarly(null as Void?)
            logger.info("Patched Radar doWork")
        } else {
            logger.info("Skipped Radar (not found)")
        }
    }
}
