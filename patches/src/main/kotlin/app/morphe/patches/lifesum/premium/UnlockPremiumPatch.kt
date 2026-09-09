package app.morphe.patches.lifesum.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.lifesum.shared.Constants.COMPATIBILITY_LIFESUM
import app.morphe.util.returnEarly

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks all premium features.",
) {
    compatibleWith(COMPATIBILITY_LIFESUM)

    execute {
        // Force network profile premium — unlocks premium-gated server state
        // for all downstream consumers.
        ApiUserProfileGetPremiumFingerprint.method.addInstructions(
            0,
            """
                sget-object p0, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;
                return-object p0
            """,
        )

        // Force hasPremium — covers all code paths using the extension function.
        HasPremiumFingerprint.method.returnEarly(true)

        // Hide trial expiry dates — date-based trial gates treat the
        // subscription as non-expiring when there is no end date.
        ApiUserProfileGetPremiumEndDateFingerprint.method.addInstructions(
            0,
            """
                const/4 p0, 0x0
                return-object p0
            """,
        )
        ApiUserProfileGetPremiumEndDateWithGracePeriodFingerprint.method.addInstructions(
            0,
            """
                const/4 p0, 0x0
                return-object p0
            """,
        )
        ApiUserProfileGetPremiumEndingInDaysFingerprint.method.addInstructions(
            0,
            """
                const/4 p0, 0x0
                return-object p0
            """,
        )
        GetEndDateFingerprint.method.addInstructions(
            0,
            """
                const/4 p0, 0x0
                return-object p0
            """,
        )

        // Ended trials don't auto-renew — force it to complete the persona.
        IsAutoRenewingFingerprint.method.returnEarly(true)

        // Force Premium.a = true in constructor — catches code paths
        // that read premium.a directly (bypassing hasPremium).
        PremiumConstructorFingerprint.method.addInstructions(
            0,
            """
                sget-object p1, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;
            """,
        )
    }
}
