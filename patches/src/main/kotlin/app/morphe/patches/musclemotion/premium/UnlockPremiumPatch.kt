package app.morphe.patches.musclemotion.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.musclemotion.shared.Constants.COMPATIBILITY_MUSCLEMOTION
import app.morphe.util.returnEarly

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks all Muscle Motion Strength Training Pro/Individual/Business features.",
) {
    compatibleWith(COMPATIBILITY_MUSCLEMOTION)

    execute {
        // Canonical isPremium check → always true.
        IsPremiumFingerprint.methodOrNull?.returnEarly(true)

        // Secondary android-subscription check → always true.
        HasAndroidSubscriptionFingerprint.methodOrNull?.returnEarly(true)

        // Subscription cancel writer → no-op (never set sp_premium = false).
        SaveUserStatusFingerprint.methodOrNull?.addInstructions(
            0,
            """
                const/4 v0, 0x1
                return-void
            """,
        )
    }
}
