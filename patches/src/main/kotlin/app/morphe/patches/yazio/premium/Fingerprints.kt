package app.morphe.patches.yazio.premium

import app.morphe.patcher.Fingerprint

/**
 * Matches `j18.M(SubscriptionStatus)` — primary premium check.
 * Returns true for WillRenew, GracePeriod, InTrialPeriod.
 */
object SubscriptionCheckFingerprint : Fingerprint(
    definingClass = "Lj18;",
    name = "M",
    returnType = "Z",
    parameters = listOf("Lyazio/subscription/api/SubscriptionStatus;"),
)

/**
 * Matches `j18.H(SubscriptionStatus)` — lenient premium check.
 * Returns true for WillExpire, GracePeriod, WillRenew, InTrialPeriod.
 */
object SubscriptionCheckLenientFingerprint : Fingerprint(
    definingClass = "Lj18;",
    name = "H",
    returnType = "Z",
    parameters = listOf("Lyazio/subscription/api/SubscriptionStatus;"),
)

/**
 * Matches `yz7.N(lhi)` — returns true when user is NOT premium.
 * Checks lhi.z (PremiumType) for null. Called from 6+ files.
 */
object IsNotPremiumFingerprint : Fingerprint(
    definingClass = "Lyz7;",
    name = "N",
    returnType = "Z",
    parameters = listOf("Llhi;"),
)

/**
 * Matches `e08.a0(lhi)` — returns true when user IS premium.
 * Checks lhi.z (PremiumType) != null. Called from 4+ files.
 */
object IsPremiumFingerprintA : Fingerprint(
    definingClass = "Le08;",
    name = "a0",
    returnType = "Z",
    parameters = listOf("Llhi;"),
)

/**
 * Matches `e08.b0(lhi)` — returns true when user IS premium.
 * Checks (lhi != null ? lhi.z : null) != null. Called from 15+ files.
 */
object IsPremiumFingerprintB : Fingerprint(
    definingClass = "Le08;",
    name = "b0",
    returnType = "Z",
    parameters = listOf("Llhi;"),
)

/**
 * Matches `ooe.h(Continuation)` — PremiumType null check coroutine.
 * Returns Boolean.TRUE when NOT premium (inverted logic).
 */
object PremiumTypeNullCheckFingerprint : Fingerprint(
    definingClass = "Looe;",
    name = "h",
    returnType = "Ljava/lang/Object;",
    parameters = listOf("Lfv3;"),
)
