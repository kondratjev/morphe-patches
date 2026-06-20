package app.morphe.patches.fatsecret.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patches.fatsecret.shared.Constants.COMPATIBILITY_FATSECRET
import app.morphe.patches.fatsecret.shared.hideMenuItem
import app.morphe.util.returnEarly

private val hidePremiumTabResourcePatch = resourcePatch {
    compatibleWith(COMPATIBILITY_FATSECRET)

    execute {
        document("res/menu/bottom_nav_menu.xml").use { document ->
            document.documentElement.hideMenuItem("tab_premium")
        }
    }
}

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks all FatSecret Gold features and hides the Premium tab.",
) {
    compatibleWith(COMPATIBILITY_FATSECRET)
    dependsOn(hidePremiumTabResourcePatch)

    execute {
        // Primary isPremium check → always true.
        IsPremiumFingerprint.methodOrNull?.returnEarly(true)

        // Premium status loaded → always true.
        IsPremiumLoadedFingerprint.methodOrNull?.returnEarly(true)

        // Invalid subscription check → always false.
        IsInvalidSubscriptionFingerprint.methodOrNull?.returnEarly(false)

        // Force both params true in StateFlow emitter.
        PremiumStatusEmitterFingerprint.methodOrNull?.addInstructions(
            0,
            """
                const/4 p1, 0x1
                const/4 p2, 0x1
            """,
        )
    }
}
