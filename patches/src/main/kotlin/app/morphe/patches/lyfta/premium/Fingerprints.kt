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
 * Matches `zob.r0()` in Lyfta 1.612 — reads the "SUBSCRIPTION" value
 * from the session manager (`SessionManager.getValue("SUBSCRIPTION",
 * null)`). Forcing "premium" covers every reader comparing the tier
 * string (premium button label, profile sync, inline `== "premium"`
 * gates — R8 inlined the old `isPremiumUser` gate in 1.612).
 * (Was `Utils.localAccessState()` up to 1.600, same body.)
 */
object LocalAccessStateFingerprint : Fingerprint(
    definingClass = "Lzob;",
    name = "r0",
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
)
