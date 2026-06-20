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
        // Force hasPremium — covers all code paths using the extension function.
        HasPremiumFingerprint.methodOrNull?.returnEarly(true)

        // Force Premium.a = true in constructor — catches code paths
        // that read premium.a directly (bypassing hasPremium).
        PremiumConstructorFingerprint.methodOrNull?.addInstructions(
            0,
            """
                sget-object p1, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;
            """,
        )
    }
}
