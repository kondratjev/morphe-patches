package app.morphe.patches.fatsecret.premium

import app.morphe.patcher.Fingerprint

/**
 * Matches `t0.h()` — primary isPremium check (synchronized).
 * Returns field `c` (boolean isPremium) from the singleton.
 * Called from dozens of feature gates throughout the app.
 *
 * DEX: classes2.dex,
 * Lcom/fatsecret/android/cores/core_common_utils/abstract_entity/t0;
 * -> h()Z (PUBLIC FINAL DECLARED_SYNCHRONIZED)
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
 * Prevents loading spinners and "pending" states.
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
 * Should return false so app doesn't show "invalid subscription" dialog.
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
