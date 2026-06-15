package app.morphe.patches.pillo.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall

/** Internal JVM class descriptor for Pillo's Preferences. */
internal const val PREFERENCES_CLASS = "Lxyz/rtrvr/pillo/persistence/preferences/Preferences;"

/**
 * Matches `Job setIsPremiumState(boolean)` in SubscriptionStateProvider.
 * Return type is Job, not void — the method returns the launched coroutine.
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
 * Matches the constructor `<init>` of SubscriptionStateProvider
 * by looking for calls to both Preferences methods inside the constructor body.
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
 * Matches `AdaptyInitializer.create(Context)` (void, the implementation
 * method — JADX renames it to `create2` but bytecode name is `create`).
 * Blocks Adapty SDK initialization (subscription/paywall tracking).
 * Lives in the premium patch because Adapty is a subscription SDK, not analytics.
 */
object AdaptyInitializerCreateFingerprint : Fingerprint(
    definingClass = "Lxyz/rtrvr/pillo/initializers/AdaptyInitializer;",
    name = "create",
    returnType = "V",
    parameters = listOf("Landroid/content/Context;"),
)
