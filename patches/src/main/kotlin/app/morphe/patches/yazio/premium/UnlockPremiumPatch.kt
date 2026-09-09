package app.morphe.patches.yazio.premium

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
        SubscriptionCheckFingerprint.method.returnEarly(true)

        // Lenient subscription check → always true.
        SubscriptionCheckLenientFingerprint.method.returnEarly(true)

        // "is NOT premium" → force false (user IS premium).
        IsNotPremiumFingerprint.method.returnEarly(false)

        // "IS premium" → force true.
        IsPremiumFingerprintA.method.returnEarly(true)

        // "IS premium" → force true.
        IsPremiumFingerprintB.method.returnEarly(true)
    }
}
