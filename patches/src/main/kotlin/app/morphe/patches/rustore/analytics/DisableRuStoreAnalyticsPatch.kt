package app.morphe.patches.rustore.analytics

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patches.all.analytics.childrenNamed
import app.morphe.patches.all.analytics.disableComponentsByPrefix
import app.morphe.patches.all.analytics.disableComponentsWhere
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE
import org.w3c.dom.Element

// ═══════════════════════════════════════════════════════════════════
// Manifest — disables VK-specific analytics components
// ═══════════════════════════════════════════════════════════════════

private val disableRuStoreAnalyticsManifestPatch = resourcePatch {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        document("AndroidManifest.xml").use { document ->
            val application = document.documentElement.childrenNamed("application").single() as Element
            var disabled = 0

            disabled += application.disableComponentsWhere { name ->
                name.startsWith("ru.vk.store.lib.analytics.")
            }
            disabled += application.disableComponentsByPrefix("ru.rustore.sdk.metrics.")

            println("Disable RuStore analytics: disabled $disabled manifest components.")
        }
    }
}

// ═══════════════════════════════════════════════════════════════════
// Bytecode — neutralizes VK-specific analytics entry points
// ═══════════════════════════════════════════════════════════════════

@Suppress("unused")
val disableRuStoreAnalyticsPatch = bytecodePatch(
    name = "Disable RuStore analytics",
    description = "Disables VK-specific analytics in RuStore, " +
        "including AltCraft event tracking and Radar device telemetry.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)
    dependsOn(disableRuStoreAnalyticsManifestPatch)

    execute {
        AltCraftSendFingerprint.methodOrNull?.addInstructions(0, "return-void")
        RadarDoWorkFingerprint.methodOrNull?.addInstructions(
            0,
            "const/4 v0, 0x0\nreturn-object v0",
        )
    }
}
