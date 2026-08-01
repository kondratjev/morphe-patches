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
        // 1. j18.M(SubscriptionStatus) — subscription check → always true
        SubscriptionCheckFingerprint.methodOrNull?.addInstructions(
            0, "const/4 v0, 0x1\nreturn v0",
        )

        // 2. j18.H(SubscriptionStatus) — lenient check → always true
        SubscriptionCheckLenientFingerprint.methodOrNull?.addInstructions(
            0, "const/4 v0, 0x1\nreturn v0",
        )

        // 3. yz7.N(lhi) — "is NOT premium" → force false (user IS premium)
        IsNotPremiumFingerprint.methodOrNull?.addInstructions(
            0, "const/4 v0, 0x0\nreturn v0",
        )

        // 4. e08.a0(lhi) — "IS premium" → force true
        IsPremiumFingerprintA.methodOrNull?.addInstructions(
            0, "const/4 v0, 0x1\nreturn v0",
        )

        // 5. e08.b0(lhi) — "IS premium" → force true
        IsPremiumFingerprintB.methodOrNull?.addInstructions(
            0, "const/4 v0, 0x1\nreturn v0",
        )

        // 6. ooe.h(Continuation) — PremiumType null check (inverted)
        //    Returns Boolean.TRUE when NOT premium → return FALSE
        PremiumTypeNullCheckFingerprint.methodOrNull?.addInstructions(
            0,
            """
                const/4 v0, 0x0
                invoke-static {v0}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;
                move-result-object v0
                return-object v0
            """,
        )
    }
}
