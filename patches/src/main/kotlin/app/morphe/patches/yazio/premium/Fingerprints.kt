package app.morphe.patches.yazio.premium

import app.morphe.patcher.Fingerprint

/**
 * Matches `hjs.A(SubscriptionStatus)` — primary premium check.
 * Returns true for GracePeriod, WillRenew, InTrialPeriod.
 * (Was `jg8.N()` in 26.36.1; R8 renamed the class in 26.44.2, same logic.)
 */
object SubscriptionCheckFingerprint : Fingerprint(
    definingClass = "Lhjs;",
    name = "A",
    returnType = "Z",
    parameters = listOf("Lyazio/subscription/api/SubscriptionStatus;"),
)

/**
 * Matches `hjs.q(SubscriptionStatus)` — lenient premium check.
 * Returns true for WillExpire, GracePeriod, WillRenew, InTrialPeriod.
 * (Was `jg8.H()` in 26.36.1; R8 renamed the class in 26.44.2, same logic.)
 */
object SubscriptionCheckLenientFingerprint : Fingerprint(
    definingClass = "Lhjs;",
    name = "q",
    returnType = "Z",
    parameters = listOf("Lyazio/subscription/api/SubscriptionStatus;"),
)

/**
 * Matches `bvv.l(zqn)` — returns true when user is NOT premium.
 * Checks (user == null || user.z == null), where z is PremiumType.
 * (Was `etj.r(q6k)` in 26.36.1; user model `q6k` renamed to `zqn` in 26.44.2, same logic.)
 */
object IsNotPremiumFingerprint : Fingerprint(
    definingClass = "Lbvv;",
    name = "l",
    returnType = "Z",
    parameters = listOf("Lzqn;"),
)

/**
 * Matches `gvv.g(zqn)` — returns true when user IS premium.
 * Checks user.z (PremiumType) != null.
 * (Was `gtj.n(q6k)` in 26.36.1; user model `q6k` renamed to `zqn` in 26.44.2, same logic.)
 */
object IsPremiumFingerprintA : Fingerprint(
    definingClass = "Lgvv;",
    name = "g",
    returnType = "Z",
    parameters = listOf("Lzqn;"),
)

/**
 * Matches `gvv.h(zqn)` — returns true when user IS premium.
 * Checks (user != null ? user.z : null) != null.
 * (Was `gtj.o(q6k)` in 26.36.1; user model `q6k` renamed to `zqn` in 26.44.2, same logic.)
 */
object IsPremiumFingerprintB : Fingerprint(
    definingClass = "Lgvv;",
    name = "h",
    returnType = "Z",
    parameters = listOf("Lzqn;"),
)
