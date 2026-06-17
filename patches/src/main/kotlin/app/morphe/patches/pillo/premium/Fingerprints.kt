package app.morphe.patches.pillo.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall

/** Internal JVM class descriptor for Pillo's Preferences. */
internal const val PREFERENCES_CLASS = "Lxyz/rtrvr/pillo/persistence/preferences/Preferences;"

/**
 * Matches `Job setIsPremiumState(boolean)` in SubscriptionStateProvider.
 */
object SetIsPremiumStateFingerprint : Fingerprint(
    definingClass = "Lxyz/rtrvr/pillo/subscription/SubscriptionStateProvider;",
    name = "setIsPremiumState",
    returnType = "Lkotlinx/coroutines/Job;",
    parameters = listOf("Z"),
)

/**
 * Matches `Job setIsAdfreeState(boolean)` in SubscriptionStateProvider.
 */
object SetIsAdfreeStateFingerprint : Fingerprint(
    definingClass = "Lxyz/rtrvr/pillo/subscription/SubscriptionStateProvider;",
    name = "setIsAdfreeState",
    returnType = "Lkotlinx/coroutines/Job;",
    parameters = listOf("Z"),
)

/**
 * Matches the constructor of SubscriptionStateProvider by looking
 * for calls to both Preferences methods in the constructor body.
 */
object SubscriptionStateProviderConstructorFingerprint : Fingerprint(
    definingClass = "Lxyz/rtrvr/pillo/subscription/SubscriptionStateProvider;",
    name = "<init>",
    returnType = "V",
    filters = listOf(
        methodCall(
            definingClass = PREFERENCES_CLASS,
            name = "getLastIsPremiumSubscriptionState",
        ),
        methodCall(
            definingClass = PREFERENCES_CLASS,
            name = "getLastIsAdfreeSubscriptionState",
        ),
    ),
)

/**
 * Matches `AdaptyInitializer.create(Context)` — initializes Adapty SDK
 * (subscription/paywall tracking). Lives in premium patch because
 * Adapty is a subscription SDK, not analytics.
 */
object AdaptyInitializerCreateFingerprint : Fingerprint(
    definingClass = "Lxyz/rtrvr/pillo/initializers/AdaptyInitializer;",
    name = "create",
    returnType = "V",
    parameters = listOf("Landroid/content/Context;"),
)
