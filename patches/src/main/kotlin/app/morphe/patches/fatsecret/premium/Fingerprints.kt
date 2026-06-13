package app.morphe.patches.fatsecret.premium

import app.morphe.patcher.Fingerprint

/**
 * Matches `t0.h()` — primary isPremium check (synchronized).
 * Returns field `c` (boolean isPremium) from the singleton.
 *
 * DEX: classes2.dex, t0;->h()Z (PUBLIC FINAL DECLARED_SYNCHRONIZED)
 */
object IsPremiumFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/fatsecret/android/cores/core_common_utils/abstract_entity/t0;" &&
            method.name == "h" &&
            method.returnType == "Z" &&
            method.parameterTypes.isEmpty()
    }
)

/**
 * Matches `t0.g()` — returns true when premium status has been loaded.
 *
 * DEX: classes2.dex, t0;->g()Z
 */
object IsPremiumLoadedFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/fatsecret/android/cores/core_common_utils/abstract_entity/t0;" &&
            method.name == "g" &&
            method.returnType == "Z" &&
            method.parameterTypes.isEmpty()
    }
)

/**
 * Matches `t0.e()` — returns true when subscription is invalid.
 *
 * DEX: classes2.dex, t0;->e()Z
 */
object IsInvalidSubscriptionFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/fatsecret/android/cores/core_common_utils/abstract_entity/t0;" &&
            method.name == "e" &&
            method.returnType == "Z" &&
            method.parameterTypes.isEmpty()
    }
)

/**
 * Matches `t0.m(boolean, boolean)` — emits PremiumStatus to StateFlow.
 * Creates new PremiumStatus(isStatusLoaded, isPremium) and sets it
 * on the MutableStateFlow. By forcing both params to true, the
 * reactive stream always emits "premium active".
 *
 * DEX: classes2.dex, t0;->m(ZZ)V (PRIVATE FINAL)
 */
object PremiumStatusEmitterFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/fatsecret/android/cores/core_common_utils/abstract_entity/t0;" &&
            method.name == "m" &&
            method.returnType == "V" &&
            method.parameterTypes.size == 2 &&
            method.parameterTypes[0] == "Z" &&
            method.parameterTypes[1] == "Z"
    }
)
