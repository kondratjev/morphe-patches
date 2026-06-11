package app.morphe.patches.medisafe.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import app.morphe.patches.medisafe.shared.Constants.COMPATIBILITY_MEDISAFE

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks all premium features including unlimited dependents, " +
            "medfriends, custom ringtones and theme colors."
) {
    compatibleWith(COMPATIBILITY_MEDISAFE)

    execute {
        // ── Hook A: Force isPaidBundle to always return true ──
        // Every premium check in the app goes through this single method.
        IsPaidBundleFingerprint.method.returnEarly(true)

        // ── Hook B: Suppress background subscription verification ──
        // Prevents the 24h periodic check from detecting the lack of
        // a real Google Play subscription.
        PurchaseRestoreWorkerDoWorkFingerprint.method.addInstructions(
            0,
            """
                invoke-static {}, Landroidx/work/ListenableWorker${'$'}Result;->success()Landroidx/work/ListenableWorker${'$'}Result;
                move-result-object v0
                return-object v0
            """.trimIndent()
        )

        // ── Hook C: Block server from overwriting the local purchase bundle ──
        // Server sync can send available_features with bundleName "base",
        // which would overwrite SharedPreferences. No-op prevents this.
        UpdatePurchaseBundleFingerprint.method.addInstructions(
            0,
            "return-void"
        )

        // ── Hook D: Prevent theme/ringtone resets on server bundle update ──
        // handlePurchaseBundleFeatures resets user colors to 0 and ringtones
        // to default when a free bundle is received from the server.
        HandlePurchaseBundleFeaturesFingerprint.method.addInstructions(
            0,
            "return-void"
        )
    }
}
