package app.morphe.patches.fatsecret.premium

import app.morphe.patcher.Fingerprint

/**
 * Matches `t0.h()` — primary isPremium check (synchronized).
 * Returns field `c` (boolean isPremium) from the singleton.
 */
object IsPremiumFingerprint : Fingerprint(
    definingClass = "Lcom/fatsecret/android/cores/core_common_utils/abstract_entity/t0;",
    name = "h",
    returnType = "Z",
    parameters = emptyList(),
)

/**
 * Matches `t0.g()` — returns true when premium status has been loaded.
 */
object IsPremiumLoadedFingerprint : Fingerprint(
    definingClass = "Lcom/fatsecret/android/cores/core_common_utils/abstract_entity/t0;",
    name = "g",
    returnType = "Z",
    parameters = emptyList(),
)

/**
 * Matches `t0.e()` — returns true when subscription is invalid.
 */
object IsInvalidSubscriptionFingerprint : Fingerprint(
    definingClass = "Lcom/fatsecret/android/cores/core_common_utils/abstract_entity/t0;",
    name = "e",
    returnType = "Z",
    parameters = emptyList(),
)

/**
 * Matches `t0.m(boolean, boolean)` — emits PremiumStatus to StateFlow.
 * Creates new PremiumStatus(isStatusLoaded, isPremium) and sets it
 * on the MutableStateFlow. Forcing both params to true ensures
 * the reactive stream always emits "premium active".
 */
object PremiumStatusEmitterFingerprint : Fingerprint(
    definingClass = "Lcom/fatsecret/android/cores/core_common_utils/abstract_entity/t0;",
    name = "m",
    returnType = "V",
    parameters = listOf("Z", "Z"),
)
