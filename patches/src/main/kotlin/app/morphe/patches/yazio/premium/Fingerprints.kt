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

/**
 * Matches `ooe.h(Continuation)` — the PremiumType null check.
 * Returns Boolean.TRUE when user has NO premium (PremiumType == null),
 * and Boolean.FALSE when user IS premium. Logic is INVERTED:
 *   r4 = (PremiumType != null) ^ 1
 *
 * Class `ooe` is a coroutine that checks lhi.z (PremiumType field).
 * By returning Boolean.FALSE we tell the app "user IS premium".
 *
 * DEX: classes.dex, Looe;->h(Lfv3;)Ljava/lang/Object;
 */
object PremiumTypeNullCheckFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Looe;" &&
            method.name == "h" &&
            method.returnType == "Ljava/lang/Object;" &&
            method.parameterTypes.size == 1
    }
)

/**
 * Matches `iz7.J(Dii)` — user deserialization that maps
 * PremiumTypeDTO to PremiumType. When the DTO is null, the
 * resulting PremiumType on lhi.z is also null (free user).
 *
 * Class `iz7` maps server data to the local lhi user model.
 * DEX: classes5.dex, Liz7;->J(Ldii;)Llhi;
 */
object UserDeserializationFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Liz7;" &&
            method.name == "J" &&
            method.returnType == "Llhi;" &&
            method.parameterTypes.size == 1 &&
            method.parameterTypes[0] == "Ldii;"
    }
)
