package app.morphe.patches.yazio.premium

import app.morphe.patcher.Fingerprint

/**
 * Matches `j18.M(SubscriptionStatus)` — primary premium check.
 * Returns true for WillRenew, GracePeriod, InTrialPeriod.
 * DEX: classes5.dex, Lj18;->M(Lyazio/subscription/api/SubscriptionStatus;)Z
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
 * Matches `j18.H(SubscriptionStatus)` — lenient premium check.
 * Returns true for WillExpire, GracePeriod, WillRenew, InTrialPeriod.
 * DEX: classes5.dex, Lj18;->H(Lyazio/subscription/api/SubscriptionStatus;)Z
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
 * Matches `yz7.N(lhi)` — returns true when user is NOT premium.
 * Checks lhi.z (PremiumType) for null. Called from 6+ files.
 * DEX: classes5.dex, Lyz7;->N(Llhi;)Z
 */
object IsNotPremiumFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lyz7;" &&
            method.name == "N" &&
            method.returnType == "Z" &&
            method.parameterTypes.size == 1 &&
            method.parameterTypes[0] == "Llhi;"
    }
)

/**
 * Matches `e08.a0(lhi)` — returns true when user IS premium.
 * Checks lhi.z (PremiumType) != null. Called from 4+ files.
 * DEX: classes.dex, Le08;->a0(Llhi;)Z
 */
object IsPremiumFingerprintA : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Le08;" &&
            method.name == "a0" &&
            method.returnType == "Z" &&
            method.parameterTypes.size == 1 &&
            method.parameterTypes[0] == "Llhi;"
    }
)

/**
 * Matches `e08.b0(lhi)` — returns true when user IS premium.
 * Checks (lhi != null ? lhi.z : null) != null. Called from 15+ files.
 * DEX: classes.dex, Le08;->b0(Llhi;)Z
 */
object IsPremiumFingerprintB : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Le08;" &&
            method.name == "b0" &&
            method.returnType == "Z" &&
            method.parameterTypes.size == 1 &&
            method.parameterTypes[0] == "Llhi;"
    }
)

/**
 * Matches `ooe.h(Continuation)` — PremiumType null check coroutine.
 * Returns Boolean.TRUE when NOT premium (inverted logic).
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
