package app.morphe.patches.soundcloud.premium

import app.morphe.patcher.Fingerprint

// ── Feature flags ───────────────────────────────────────────────

object FlagFeatureEvaluationFingerprint : Fingerprint(
    definingClass = "Feature\$FlagFeature;",
    name = "i",
    returnType = "Z",
    parameters = listOf(
        "Lcom/soundcloud/android/properties/RemoteFlagProvider;",
        "Lcom/soundcloud/android/properties/LocalFlagProvider;",
        "Lcom/soundcloud/appconfig/DeviceConfiguration;",
    ),
)

// ── Plan / tier ─────────────────────────────────────────────────

object UserConsumerPlanConstructorFingerprint : Fingerprint(
    definingClass = "UserConsumerPlan;",
    name = "<init>",
    parameters = listOf(
        "Ljava/lang/String;",
        "Z",
        "Ljava/lang/String;",
        "Ljava/util/List;",
        "Ljava/lang/String;",
        "Ljava/lang/String;",
    ),
)

object GetDowngradeTierFingerprint : Fingerprint(
    definingClass = "ConfigurationSettingsStorage;",
    returnType = "Tier;",
    parameters = emptyList(),
)

object MapToPlanFingerprint : Fingerprint(
    definingClass = "RemoteUpsellVisibilityController;",
    name = "mapToPlan",
)

// ── Tier detection (prevents expiration dialog) ─────────────────

object GetCurrentTierFingerprint : Fingerprint(
    definingClass = "DefaultFeatureOperations;",
    returnType = "Tier;",
    parameters = emptyList(),
)

object GetCurrentConsumerPlanFingerprint : Fingerprint(
    definingClass = "DefaultFeatureOperations;",
    returnType = "ConsumerPlan;",
    parameters = emptyList(),
)

object TierChangeDetectorFingerprint : Fingerprint(
    definingClass = "DefaultTierChangeDetector;",
    name = "b",
    returnType = "V",
    parameters = listOf("Tier;", "Ljava/lang/String;"),
)

// ── Ad blocking ─────────────────────────────────────────────────

object GetShouldRequestAdsFingerprint : Fingerprint(
    definingClass = "DefaultFeatureOperations;",
    name = "getShouldRequestAds",
    returnType = "Z",
    parameters = emptyList(),
)

object IsMonetizableAdGeoFingerprint : Fingerprint(
    definingClass = "DefaultFeatureOperations;",
    name = "isMonetizableAdGeo",
    returnType = "Z",
    parameters = emptyList(),
)

object AdPlacementConfigCtorFingerprint : Fingerprint(
    definingClass = "AdPlacementConfiguration;",
    name = "<init>",
    returnType = "V",
)

// ── Banner filtering ────────────────────────────────────────────

object BannerSectionMapperFingerprint : Fingerprint(
    definingClass = "BannerSectionMapper;",
    strings = listOf("BannerSection"),
)
