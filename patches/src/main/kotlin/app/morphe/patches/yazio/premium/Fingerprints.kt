package app.morphe.patches.yazio.premium

import app.morphe.patcher.Fingerprint

/**
 * Matches `jg8.N(SubscriptionStatus)` — primary premium check.
 * Returns true for GracePeriod, WillRenew, InTrialPeriod.
 */
object SubscriptionCheckFingerprint : Fingerprint(
    definingClass = "Ljg8;",
    name = "N",
    returnType = "Z",
    parameters = listOf("Lyazio/subscription/api/SubscriptionStatus;"),
)

/**
 * Matches `jg8.H(SubscriptionStatus)` — lenient premium check.
 * Returns true for WillExpire, GracePeriod, WillRenew, InTrialPeriod.
 */
object SubscriptionCheckLenientFingerprint : Fingerprint(
    definingClass = "Ljg8;",
    name = "H",
    returnType = "Z",
    parameters = listOf("Lyazio/subscription/api/SubscriptionStatus;"),
)

/**
 * Matches `etj.r(q6k)` — returns true when user is NOT premium.
 * Checks (q6k == null || q6k.z == null), where z is PremiumType.
 */
object IsNotPremiumFingerprint : Fingerprint(
    definingClass = "Letj;",
    name = "r",
    returnType = "Z",
    parameters = listOf("Lq6k;"),
)

/**
 * Matches `gtj.n(q6k)` — returns true when user IS premium.
 * Checks q6k.z (PremiumType) != null.
 */
object IsPremiumFingerprintA : Fingerprint(
    definingClass = "Lgtj;",
    name = "n",
    returnType = "Z",
    parameters = listOf("Lq6k;"),
)

/**
 * Matches `gtj.o(q6k)` — returns true when user IS premium.
 * Checks (q6k != null ? q6k.z : null) != null.
 */
object IsPremiumFingerprintB : Fingerprint(
    definingClass = "Lgtj;",
    name = "o",
    returnType = "Z",
    parameters = listOf("Lq6k;"),
)
