package app.morphe.patches.medisafe.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import app.morphe.patches.medisafe.shared.Constants.COMPATIBILITY_MEDISAFE

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks all premium features.",
) {
    compatibleWith(COMPATIBILITY_MEDISAFE)

    execute {
        // Force isPaidBundle — all premium checks funnel through this method.
        IsPaidBundleFingerprint.method.returnEarly(true)

        // Suppress background subscription verification (24h periodic check).
        PurchaseRestoreWorkerDoWorkFingerprint.method.addInstructions(
            0,
            """
                invoke-static {}, Landroidx/work/ListenableWorker${'$'}Result;->success()Landroidx/work/ListenableWorker${'$'}Result;
                move-result-object v0
                return-object v0
            """.trimIndent()
        )
    }
}
