package app.morphe.patches.lyfta.premium

import app.morphe.patcher.Fingerprint

/** Matches `Utils.getSubscriptionType()` — returns the current subscription tier. */
object GetSubscriptionTypeFingerprint : Fingerprint(
    definingClass = "Lcom/lyfta/util/helpers/Utils;",
    name = "getSubscriptionType"
)
