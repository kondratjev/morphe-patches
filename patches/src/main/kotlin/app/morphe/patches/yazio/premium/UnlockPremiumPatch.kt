package app.morphe.patches.yazio.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.yazio.shared.Constants.COMPATIBILITY_YAZIO
import app.morphe.util.returnEarly

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks all Yazio Pro features.",
) {
    compatibleWith(COMPATIBILITY_YAZIO)

    execute {
        // 1. j18.M(SubscriptionStatus) — subscription check → always true
        SubscriptionCheckFingerprint.methodOrNull?.returnEarly(true)

        // 2. j18.H(SubscriptionStatus) — lenient check → always true
        SubscriptionCheckLenientFingerprint.methodOrNull?.returnEarly(true)

        // 3. yz7.N(lhi) — "is NOT premium" → force false (user IS premium)
        IsNotPremiumFingerprint.methodOrNull?.returnEarly(false)

        // 4. e08.a0(lhi) — "IS premium" → force true
        IsPremiumFingerprintA.methodOrNull?.returnEarly(true)

        // 5. e08.b0(lhi) — "IS premium" → force true
        IsPremiumFingerprintB.methodOrNull?.returnEarly(true)

        // 6. ooe.h(Continuation) — PremiumType null check (inverted)
        //    Returns Boolean.TRUE when NOT premium → return FALSE
        //    Return type is Object (coroutine), not Boolean — can't use returnBoxedBooleanEarly
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
