package app.morphe.patches.lifesum.premium

import app.morphe.patcher.Fingerprint

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
 * Matches `n5c.<init>(Boolean, String, LocalDate, ...)` — Premium data class constructor.
 * Multiple code paths read `premium.a` directly instead of going through `hasPremium()`.
 * Patching the constructor forces `this.a = Boolean.TRUE` for all instances.
 */
object PremiumConstructorFingerprint : Fingerprint(
    definingClass = "Ll/n5c;",
    name = "<init>",
    returnType = "V",
    parameters = listOf(
        "Ljava/lang/Boolean;",
        "Ljava/lang/String;",
        "Lorg/joda/time/LocalDate;",
        "Lorg/joda/time/LocalDate;",
        "Ljava/lang/Integer;",
        "Ljava/lang/Integer;",
        "Ll/zdf;",
        "Ljava/lang/Boolean;",
        "Ljava/lang/Double;",
        "Ljava/util/Map;",
        "Ljava/lang/Double;",
        "Ljava/lang/Double;",
    ),
)
