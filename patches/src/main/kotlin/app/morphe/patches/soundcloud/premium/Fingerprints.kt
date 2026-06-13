package app.morphe.patches.soundcloud.premium

import app.morphe.patcher.Fingerprint

/**
 * Matches `Feature$FlagFeature.i(RemoteFlagProvider, LocalFlagProvider, DeviceConfiguration)`
 * — the single evaluation point for every boolean feature flag.
 * Covers ~80 FlagFeatures + all KillSwitches.
 *
 * The method evaluates: remote config → local override → boolean result.
 * Patching to return true enables all feature flags unconditionally.
 */
object FlagFeatureEvaluationFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/features/Feature\$FlagFeature;",
    name = "i",
    returnType = "Z",
    parameters = listOf(
        "Lcom/soundcloud/android/properties/RemoteFlagProvider;",
        "Lcom/soundcloud/android/properties/LocalFlagProvider;",
        "Lcom/soundcloud/appconfig/DeviceConfiguration;",
    ),
)

/**
 * Matches `UserConsumerPlan` constructor with @JsonCreator annotation.
 * Parameters: (String planId, boolean autoRenew, String tierId,
 *              List<Upsell> upsells, String planName, String displayName).
 * Patching forces go-plus tier and display name.
 */
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

/**
 * Matches `ConfigurationSettingsStorage` method that reads
 * "pending_plan_downgrade" string and returns Tier.
 * Patching returns Tier.HIGH to prevent offboarding screen.
 */
object GetDowngradeTierFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/configuration/ConfigurationSettingsStorage;",
    returnType = "Lcom/soundcloud/android/configuration/plans/Tier;",
    parameters = emptyList(),
)

/**
 * Matches `RemoteUpsellVisibilityController.mapToPlan`
 * — maps API upsell graph to internal UpsellType.
 * Patching returns UpsellType.None to hide all upsell UI.
 */
object MapToPlanFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/upsell/RemoteUpsellVisibilityController;",
    name = "mapToPlan",
)

/**
 * Matches `AdPlacementConfiguration` constructors.
 * Patching zeroes out boolean + config params to disable ads.
 */
object AdPlacementConfigCtorFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/ads/aditude/AdPlacementConfiguration;",
    name = "<init>",
    returnType = "V",
)
