package app.morphe.patches.yazio.premium

import app.morphe.patcher.Fingerprint

/**
 * Matches `j18.M(SubscriptionStatus)` — the primary premium check.
 * Returns true when subscription status is WillRenew, GracePeriod,
 * or InTrialPeriod (i.e., the user has an active subscription).
 *
 * Class `j18` in `defpackage` = obfuscated utility class.
 * Method `M` = isPremium(SubscriptionStatus)Z.
 */
object SubscriptionCheckFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lj18;" &&
            method.name == "M" &&
            method.returnType == "Z" &&
            method.parameterTypes.size == 1 &&
            method.parameterTypes[0] == "Lyazio/subscription/api/SubscriptionStatus;"
    }
)

/**
 * Matches `j18.H(SubscriptionStatus)` — the lenient premium check.
 * Returns true for WillExpire, GracePeriod, WillRenew, and InTrialPeriod.
 * Used in UI layers that show premium content to users whose subscription
 * is about to expire.
 *
 * Class `j18` in `defpackage` = obfuscated utility class.
 * Method `H` = isPremiumOrExpiring(SubscriptionStatus)Z.
 */
object SubscriptionCheckLenientFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lj18;" &&
            method.name == "H" &&
            method.returnType == "Z" &&
            method.parameterTypes.size == 1 &&
            method.parameterTypes[0] == "Lyazio/subscription/api/SubscriptionStatus;"
    }
)
