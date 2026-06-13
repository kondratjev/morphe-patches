package app.morphe.patches.yazio.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.yazio.shared.Constants.COMPATIBILITY_YAZIO

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks all Yazio Pro features.",
) {
    compatibleWith(COMPATIBILITY_YAZIO)

    execute {
        // Primary check: j18.M(SubscriptionStatus) — returns true for
        // active subscriptions (WillRenew, GracePeriod, InTrialPeriod).
        SubscriptionCheckFingerprint.method.addInstructions(
            0,
            "const/4 v0, 0x1\nreturn v0",
        )

        // Lenient check: j18.H(SubscriptionStatus) — also includes
        // WillExpire status (subscription hasn't expired yet).
        SubscriptionCheckLenientFingerprint.method.addInstructions(
            0,
            "const/4 v0, 0x1\nreturn v0",
        )
    }
}
