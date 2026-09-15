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
 * Matches `Utils.isPremiumUser()` — central premium gate.
 * Returns true when the local access state equals "premium".
 */
object IsPremiumUserFingerprint : Fingerprint(
    definingClass = "Lcom/lyfta/util/helpers/Utils;",
    name = "isPremiumUser",
    returnType = "Z",
    parameters = listOf(),
)

/**
 * Matches `Utils.localAccessState()` — reads the "SUBSCRIPTION" value
 * from the session manager. Forcing "premium" covers every reader
 * comparing the tier string (premium button label, profile sync, etc.).
 */
object LocalAccessStateFingerprint : Fingerprint(
    definingClass = "Lcom/lyfta/util/helpers/Utils;",
    name = "localAccessState",
    returnType = "Ljava/lang/String;",
    parameters = listOf(),
)
