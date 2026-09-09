package app.morphe.patches.lifesum.premium

import app.morphe.patcher.Fingerprint

/**
 * Matches `ApiUserProfile.getPremium()Ljava/lang/Boolean;` — network profile model.
 * Forcing it to return `Boolean.TRUE` unlocks premium-gated server state
 * for all downstream consumers.
 */
object ApiUserProfileGetPremiumFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/lifesum/profile/network/models/ApiUserProfile;" &&
            method.name == "getPremium"
    }
)

/**
 * Matches `ApiUserProfile.getPremiumEndDate()` — trial expiry date from the server.
 * Returning null hides the expiry so date-based trial gates treat the
 * subscription as non-expiring.
 */
object ApiUserProfileGetPremiumEndDateFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/lifesum/profile/network/models/ApiUserProfile;" &&
            method.name == "getPremiumEndDate"
    }
)

/**
 * Matches `ApiUserProfile.getPremiumEndDateWithGracePeriod()` — trial expiry date
 * with grace period from the server. Same treatment as `getPremiumEndDate`.
 */
object ApiUserProfileGetPremiumEndDateWithGracePeriodFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/lifesum/profile/network/models/ApiUserProfile;" &&
            method.name == "getPremiumEndDateWithGracePeriod"
    }
)

/**
 * Matches `ApiUserProfile.getPremiumEndingInDays()` — days until trial expiry.
 * Returning null hides the countdown from trial gates.
 */
object ApiUserProfileGetPremiumEndingInDaysFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/lifesum/profile/network/models/ApiUserProfile;" &&
            method.name == "getPremiumEndingInDays"
    }
)

/**
 * Matches `ProfileModelExtensionsKt.hasPremium(ProfileModel)Z` — single
 * extension function all premium checks in Lifesum funnel through.
 */
object HasPremiumFingerprint : Fingerprint(
    definingClass = "Lcom/sillens/shapeupclub/db/models/ProfileModelExtensionsKt;",
    name = "hasPremium",
    returnType = "Z",
    parameters = listOf("Lcom/sillens/shapeupclub/db/models/ProfileModel;"),
)

/**
 * Matches `ProfileModelExtensionsKt.getEndDate(ProfileModel)` — derived trial end date.
 * Returning null hides the expiry from date-based trial gates.
 */
object GetEndDateFingerprint : Fingerprint(
    definingClass = "Lcom/sillens/shapeupclub/db/models/ProfileModelExtensionsKt;",
    name = "getEndDate",
    returnType = "Lorg/joda/time/LocalDate;",
    parameters = listOf("Lcom/sillens/shapeupclub/db/models/ProfileModel;"),
)

/**
 * Matches `ProfileModelExtensionsKt.isAutoRenewing(ProfileModel)` — ended trials
 * don't auto-renew. Forcing true completes the active-subscriber persona.
 */
object IsAutoRenewingFingerprint : Fingerprint(
    definingClass = "Lcom/sillens/shapeupclub/db/models/ProfileModelExtensionsKt;",
    name = "isAutoRenewing",
    returnType = "Z",
    parameters = listOf("Lcom/sillens/shapeupclub/db/models/ProfileModel;"),
)

/**
 * Matches `l/yrc.<init>(Boolean, String, LocalDate, ...)` — Premium data class constructor
 * (obfuscated name varies per version: `l/n5c` in 20.6.1, `l/cid` in 20.10.0).
 * Multiple code paths read `premium.a` directly instead of going through `hasPremium()`.
 * Patching the constructor forces `this.a = Boolean.TRUE` for all instances.
 */
object PremiumConstructorFingerprint : Fingerprint(
    definingClass = "Ll/yrc;",
    name = "<init>",
    returnType = "V",
    parameters = listOf(
        "Ljava/lang/Boolean;",
        "Ljava/lang/String;",
        "Lorg/joda/time/LocalDate;",
        "Lorg/joda/time/LocalDate;",
        "Ljava/lang/Integer;",
        "Ljava/lang/Integer;",
        "Ll/y3g;",
        "Ljava/lang/Boolean;",
        "Ljava/lang/Double;",
        "Ljava/util/Map;",
        "Ljava/lang/Double;",
        "Ljava/lang/Double;",
    ),
)
