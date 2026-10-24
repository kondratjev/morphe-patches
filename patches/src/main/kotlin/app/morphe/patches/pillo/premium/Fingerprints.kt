package app.morphe.patches.pillo.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall

/** Internal JVM class descriptor for Pillo's Preferences. */
internal const val PREFERENCES_CLASS = "Lxyz/rtrvr/pillo/data/persistence/preferences/Preferences;"

/**
 * Matches `setIsPremiumState(boolean)` in SubscriptionStateProvider.
 * Return type is not pinned: `Job` in 0.6.24, obfuscated `q1` in 0.6.25 —
 * same dispatcher body either way. Unique method name per class.
 */
object SetIsPremiumStateFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lxyz/rtrvr/pillo/subscription/SubscriptionStateProvider;" &&
            method.name == "setIsPremiumState" &&
            method.implementation != null
    }
)

/**
 * Matches `setIsAdfreeState(boolean)` in SubscriptionStateProvider.
 * Same return-type drift as premium (see above).
 */
object SetIsAdfreeStateFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lxyz/rtrvr/pillo/subscription/SubscriptionStateProvider;" &&
            method.name == "setIsAdfreeState" &&
            method.implementation != null
    }
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
