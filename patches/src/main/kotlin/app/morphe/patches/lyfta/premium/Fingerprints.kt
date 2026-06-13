package app.morphe.patches.lyfta.premium

import app.morphe.patcher.Fingerprint

object GetSubscriptionTypeFingerprint : Fingerprint(
    definingClass = "Utils;",
    name = "getSubscriptionType"
)
