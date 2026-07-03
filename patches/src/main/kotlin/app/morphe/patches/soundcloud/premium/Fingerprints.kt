package app.morphe.patches.soundcloud.premium

import app.morphe.patcher.Fingerprint

// Plan / tier

/** Matches `UserConsumerPlan.<init>()` — constructs the user's plan object from backend data. */
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

/** Matches `ConfigurationSettingsStorage.getDowngradeTier()` — returns the downgrade tier plan. */
object GetDowngradeTierFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/configuration/data/ConfigurationSettingsStorage;",
    returnType = "Lcom/soundcloud/android/configuration/plans/Tier;",
    parameters = emptyList(),
)

/** Matches `RemoteUpsellVisibilityController.mapToPlan()` — maps backend upselling data to plan visibility. */
object MapToPlanFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/upsell/RemoteUpsellVisibilityController;",
    name = "mapToPlan",
)

// Tier/plan state

/** Matches `DefaultFeatureOperations.getCurrentTier()` — returns the active tier from local config. */
object GetCurrentTierFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/configuration/features/DefaultFeatureOperations;",
    returnType = "Lcom/soundcloud/android/configuration/plans/Tier;",
    parameters = emptyList(),
)

/** Matches `DefaultFeatureOperations.getCurrentConsumerPlan()` — returns the active ConsumerPlan from local config. */
object GetCurrentConsumerPlanFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/configuration/features/DefaultFeatureOperations;",
    returnType = "Lcom/soundcloud/android/configuration/plans/ConsumerPlan;",
    parameters = emptyList(),
)

// Offboarding / transitions

/** Matches `DefaultConfigurationUpdatesLifecycleObserver.b(RootActivity)` — observes plan changes and triggers transition UI. */
object ConfigurationUpdatesLifecycleObserverFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/configuration/DefaultConfigurationUpdatesLifecycleObserver;",
    name = "b",
    returnType = "V",
    parameters = listOf("Lcom/soundcloud/android/architecture/view/RootActivity;"),
)

// Ad blocking

/** Matches `DefaultFeatureOperations.getShouldRequestAds()` — determines whether ads should be requested. */
object GetShouldRequestAdsFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/configuration/features/DefaultFeatureOperations;",
    name = "getShouldRequestAds",
    returnType = "Z",
    parameters = emptyList(),
)

/** Matches `DefaultFeatureOperations.isMonetizableAdGeo()` — checks if current geo is eligible for monetizable ads. */
object IsMonetizableAdGeoFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/configuration/features/DefaultFeatureOperations;",
    name = "isMonetizableAdGeo",
    returnType = "Z",
    parameters = emptyList(),
)

/** Matches `AdPlacementConfiguration.<init>(boolean, GamConfiguration, AditudeConfiguration)` — constructs ad placement configuration for the player. */
object AdPlacementConfigCtorFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/ads/display/data/config/AdPlacementConfiguration;",
    name = "<init>",
    returnType = "V",
    parameters = listOf(
        "Z",
        "Lcom/soundcloud/android/ads/display/data/config/GamConfiguration;",
        "Lcom/soundcloud/android/ads/display/data/config/AditudeConfiguration;",
    ),
)
