package app.morphe.patches.lyfta.premium

import app.morphe.patcher.Fingerprint

/**
 * Matches `Utils.getSubscriptionType()` — returns the current subscription tier.
 * Present in Lyfta 1.575 (old UI). Forcing "premium" unlocks premium features.
 */
object GetSubscriptionTypeFingerprint : Fingerprint(
    definingClass = "Lcom/lyfta/util/helpers/Utils;",
    name = "getSubscriptionType"
)

/**
 * Matches `Utils.isPremiumUser()` — central premium gate up to 1.600.
 * Returns true when the local access state equals "premium".
 * (R8 inlined the gate in 1.612 — there the tier force below covers
 * every inline `== "premium"` reader instead.)
 */
object IsPremiumUserFingerprint : Fingerprint(
    definingClass = "Lcom/lyfta/util/helpers/Utils;",
    name = "isPremiumUser",
    returnType = "Z",
    parameters = listOf(),
)

/**
 * Matches `s69.c(...)` in Lyfta 1.617 — paywall router. Reads the tier via
 * the generic session getter (`q2b.i("SUBSCRIPTION", null)`) and shows
 * paywall fragments for non-premium tiers (the premium branch only runs
 * the caller's callback). The patch overwrites the tier register with
 * "premium" right after the getter call.
 */
object PaywallRouterTierFingerprint : Fingerprint(
    definingClass = "Ls69;",
    name = "c",
    returnType = "V",
    parameters = listOf(
        "Ls69;",
        "Landroid/app/Activity;",
        "Ljava/lang/String;",
        "Ldp9;",
        "Z",
        "I",
    ),
    strings = listOf("SUBSCRIPTION"),
)
