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
        // Primary subscription check → always true.
        SubscriptionCheckFingerprint.methodOrNull?.returnEarly(true)

        // Lenient subscription check → always true.
        SubscriptionCheckLenientFingerprint.methodOrNull?.returnEarly(true)

        // "is NOT premium" → force false (user IS premium).
        IsNotPremiumFingerprint.methodOrNull?.returnEarly(false)

        // "IS premium" → force true.
        IsPremiumFingerprintA.methodOrNull?.returnEarly(true)

        // "IS premium" → force true.
        IsPremiumFingerprintB.methodOrNull?.returnEarly(true)

        // PremiumType null check (inverted). Returns Boolean.TRUE when
        // NOT premium → return FALSE. Return type is Object (coroutine).
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
