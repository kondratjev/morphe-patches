package app.morphe.patches.soundcloud.premium

import app.morphe.patcher.Fingerprint

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

object TierChangeDetectorBFingerprint : Fingerprint(
    definingClass = "DefaultTierChangeDetector;",
    name = "b",
    returnType = "V",
    parameters = listOf("Tier;", "Ljava/lang/String;"),
)

object TierChangeDetectorAFingerprint : Fingerprint(
    definingClass = "DefaultTierChangeDetector;",
    name = "a",
    returnType = "V",
    parameters = listOf("DetectedFor;", "DetectedTransition;", "DetectedVia;"),
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

// ── Geo-block bypass ────────────────────────────────────────────

object ApiGraphTrackAuthorizationCtorFingerprint : Fingerprint(
    definingClass = "ApiGraphTrackAuthorization;",
    name = "<init>",
    parameters = listOf(
        "Z", "Z", "Z",
        "Ljava/lang/String;", "Ljava/lang/String;",
        "Z", "Z", "Z", "Z",
    ),
)
