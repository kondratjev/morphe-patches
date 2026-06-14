package app.morphe.patches.lifesum.premium

import app.morphe.patcher.Fingerprint

/**
 * Matches `ProfileModelExtensionsKt.hasPremium(ProfileModel)Z` — the single
 * extension function that all premium checks in Lifesum funnel through.
 *
 * DEX: classes.dex, Lcom/sillens/shapeupclub/db/models/ProfileModelExtensionsKt;->hasPremium(Lcom/sillens/shapeupclub/db/models/ProfileModel;)Z
 */
object HasPremiumFingerprint : Fingerprint(
    definingClass = "Lcom/sillens/shapeupclub/db/models/ProfileModelExtensionsKt;",
    name = "hasPremium",
    returnType = "Z",
    parameters = listOf("Lcom/sillens/shapeupclub/db/models/ProfileModel;"),
)

/**
 * Matches `n5c.<init>(Boolean, String, LocalDate, ...)` — the Premium data class constructor.
 * Multiple code paths read `premium.a` directly instead of going through `hasPremium()`.
 * Patching the constructor to force `this.a = Boolean.TRUE` catches all of them:
 *   - l/jz4.java:205 (Create Food summary — fiber/sugars/carbs lock)
 *   - l/p0.java:92   (Create Meal summary — fiber/sugars lock)
 *   - l/vc3.java:275  (food detail via cr9 → vva)
 *   - l/cbd.java:20   (recipe/food detail PremiumLocked toolbar)
 *   - l/gp8.java:83   (lifestyle week tab)
 *   - l/sib.java:404  (top bar premium button)
 *   - com/sillens/.../CreateRecipeActivity.java:301,356
 *   - com/sillens/.../KetogenicSettingsActivity.java:221,274,326
 *   - com/sillens/.../TrackMeasurementActivity.java:257
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
