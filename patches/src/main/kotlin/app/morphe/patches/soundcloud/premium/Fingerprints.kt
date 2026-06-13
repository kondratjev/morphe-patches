package app.morphe.patches.soundcloud.premium

import app.morphe.patcher.Fingerprint

// ── Plan / tier ─────────────────────────────────────────────────

object UserConsumerPlanConstructorFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/configuration/plans/UserConsumerPlan;",
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
    definingClass = "Lcom/soundcloud/android/configuration/ConfigurationSettingsStorage;",
    returnType = "Lcom/soundcloud/android/configuration/plans/Tier;",
    parameters = emptyList(),
)

object MapToPlanFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/upsell/RemoteUpsellVisibilityController;",
    name = "mapToPlan",
)

// ── Tier detection (prevents expiration dialog) ─────────────────

object GetCurrentTierFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/configuration/features/DefaultFeatureOperations;",
    returnType = "Lcom/soundcloud/android/configuration/plans/Tier;",
    parameters = emptyList(),
)

object GetCurrentConsumerPlanFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/configuration/features/DefaultFeatureOperations;",
    returnType = "Lcom/soundcloud/android/configuration/plans/ConsumerPlan;",
    parameters = emptyList(),
)

object TierChangeDetectorBFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/configuration/DefaultTierChangeDetector;",
    name = "b",
    returnType = "V",
    parameters = listOf(
        "Lcom/soundcloud/android/configuration/plans/Tier;",
        "Ljava/lang/String;",
    ),
)

object TierChangeDetectorAFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/configuration/DefaultTierChangeDetector;",
    name = "a",
    returnType = "V",
    parameters = listOf(
        "Lcom/soundcloud/android/configuration/data/DetectedFor;",
        "Lcom/soundcloud/android/configuration/data/DetectedTransition;",
        "Lcom/soundcloud/android/configuration/data/DetectedVia;",
    ),
)

// ── Offboarding blocking ────────────────────────────────────────

object PlanTransitionsExperimentsFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/payments/plantransitions/experiment/DefaultPlanTransitionsExperiments;",
    name = "a",
    returnType = "Z",
    parameters = listOf(
        "Lcom/soundcloud/android/payments/plantransitions/experiment/PlanTransitionExperiment;",
    ),
)

object PlanTransitionManagerOffboardingFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/payments/plantransitions/ui/PlanTransitionManager;",
    name = "e",
    returnType = "V",
    parameters = listOf(
        "Lcom/soundcloud/android/payments/plantransitions/ui/PlanTransitionManager\$OffboardingBehaviour;",
    ),
)

// ── Ad blocking ─────────────────────────────────────────────────

object GetShouldRequestAdsFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/configuration/features/DefaultFeatureOperations;",
    name = "getShouldRequestAds",
    returnType = "Z",
    parameters = emptyList(),
)

object IsMonetizableAdGeoFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/configuration/features/DefaultFeatureOperations;",
    name = "isMonetizableAdGeo",
    returnType = "Z",
    parameters = emptyList(),
)

object AdPlacementConfigCtorFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/ads/display/data/config/AdPlacementConfiguration;",
    name = "<init>",
    returnType = "V",
)
