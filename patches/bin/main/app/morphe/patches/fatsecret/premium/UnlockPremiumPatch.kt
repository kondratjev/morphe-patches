package app.morphe.patches.fatsecret.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.fatsecret.shared.Constants.COMPATIBILITY_FATSECRET

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks all FatSecret Gold features.",
) {
    compatibleWith(COMPATIBILITY_FATSECRET)

    execute {
        // t0.h() — primary isPremium check → always true
        IsPremiumFingerprint.methodOrNull?.addInstructions(
            0, "const/4 v0, 0x1\nreturn v0",
        )

        // t0.g() — premium status loaded → always true
        IsPremiumLoadedFingerprint.methodOrNull?.addInstructions(
            0, "const/4 v0, 0x1\nreturn v0",
        )

        // t0.e() — invalid subscription check → always false
        IsInvalidSubscriptionFingerprint.methodOrNull?.addInstructions(
            0, "const/4 v0, 0x0\nreturn v0",
        )
    }
}
