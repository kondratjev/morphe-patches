package app.morphe.patches.yazio.premium

import app.morphe.patcher.Fingerprint

/**
 * Matches `vmu.j(SubscriptionStatus)` — primary premium check.
 * Returns true for GracePeriod, WillRenew, InTrialPeriod.
 * (Was `hjs.A` in 26.44.2 — R8 swapped the method names in 26.47.2,
 * same logic.)
 */
object SubscriptionCheckFingerprint : Fingerprint(
    definingClass = "Lvmu;",
    name = "j",
    returnType = "Z",
    parameters = listOf("Lyazio/subscription/api/SubscriptionStatus;"),
)

/**
 * Matches `vmu.g(SubscriptionStatus)` — lenient premium check.
 * Returns true for WillExpire, GracePeriod, WillRenew, InTrialPeriod.
 * (Was `hjs.q` in 26.44.2 — R8 swapped the method names in 26.47.2,
 * same logic.)
 */
object SubscriptionCheckLenientFingerprint : Fingerprint(
    definingClass = "Lvmu;",
    name = "g",
    returnType = "Z",
    parameters = listOf("Lyazio/subscription/api/SubscriptionStatus;"),
)

/**
 * Matches `wnw.p(n5o)` — returns true when user is NOT premium.
 * Checks (user == null || user.z == null), where z is PremiumType.
 * (Was `bvv.l(zqn)` in 26.44.2; R8 renamed in 26.47.2, same logic.)
 */
object IsNotPremiumFingerprint : Fingerprint(
    definingClass = "Lwnw;",
    name = "p",
    returnType = "Z",
    parameters = listOf("Ln5o;"),
)

/**
 * Matches `ynw.n(n5o)` — returns true when user IS premium.
 * Checks user.z (PremiumType) != null.
 * (Was `gvv.g(zqn)` in 26.44.2; R8 renamed in 26.47.2, same logic.)
 */
object IsPremiumFingerprintA : Fingerprint(
    definingClass = "Lynw;",
    name = "n",
    returnType = "Z",
    parameters = listOf("Ln5o;"),
)

/**
 * Matches `ynw.o(n5o)` — returns true when user IS premium.
 * Checks (user != null ? user.z : null) != null.
 * (Was `gvv.h(zqn)` in 26.44.2; R8 renamed in 26.47.2, same logic.)
 */
object IsPremiumFingerprintB : Fingerprint(
    definingClass = "Lynw;",
    name = "o",
    returnType = "Z",
    parameters = listOf("Ln5o;"),
)
