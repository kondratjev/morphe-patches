package app.morphe.patches.rustore.analytics

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.Fingerprint
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE
import java.util.logging.Logger

private val logger = Logger.getLogger("DisableRuStoreAnalytics")

/**
 * Disables VK-specific analytics systems found in RuStore that are not
 * covered by the universal [DisableAnalyticsPatch]:
 *
 * - **AltCraft Analytics** — VK's internal event analytics
 * - **Radar Telemetry** — device/network snapshot collection
 */
@Suppress("unused")
val disableRuStoreAnalyticsPatch = bytecodePatch(
    name = "Disable RuStore analytics",
    description = "Disables VK-specific analytics in RuStore, " +
            "including AltCraft event tracking and Radar device telemetry.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        var patched = 0
        var skipped = 0

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

        // ── AltCraft Analytics ───────────────────────────────────
        if (AltCraftAnalyticsSendFingerprint.patchOrWarn("AltCraftAnalytics.send()", "return-void")) patched++ else skipped++

        // ── Radar Telemetry ──────────────────────────────────────
        if (RadarFlushSnapshotDoWorkFingerprint.patchOrWarn(
                "RadarFlushSnapshotWorker.doWork()",
                "const/4 v0, 0x0\nreturn-object v0"
            )
        ) patched++ else skipped++

        logger.info("RuStore analytics patch summary: $patched patched, $skipped skipped")
    }
}
