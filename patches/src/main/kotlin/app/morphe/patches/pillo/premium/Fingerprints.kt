package app.morphe.patches.pillo.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import com.android.tools.smali.dexlib2.AccessFlags

/**
 * Matches the private method `setIsPremiumState(Z)V`
 * in `Lxyz/rtrvr/pillo/subscription/SubscriptionStateProvider;`.
 */
object SetIsPremiumStateFingerprint : Fingerprint(
    definingClass = "Lxyz/rtrvr/pillo/subscription/SubscriptionStateProvider;",
    name = "setIsPremiumState",
    returnType = "V",
    accessFlags = listOf(AccessFlags.PRIVATE),
    parameters = listOf("Z"),
)

/**
 * Matches the private method `setIsAdfreeState(Z)V`
 * in `Lxyz/rtrvr/pillo/subscription/SubscriptionStateProvider;`.
 */
object SetIsAdfreeStateFingerprint : Fingerprint(
    definingClass = "Lxyz/rtrvr/pillo/subscription/SubscriptionStateProvider;",
    name = "setIsAdfreeState",
    returnType = "V",
    accessFlags = listOf(AccessFlags.PRIVATE),
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
            definingClass = "Lxyz/rtrvr/pillo/persistence/preferences/Preferences;",
            name = "getLastIsPremiumSubscriptionState",
        ),
        methodCall(
            definingClass = "Lxyz/rtrvr/pillo/persistence/preferences/Preferences;",
            name = "getLastIsAdfreeSubscriptionState",
        ),
    ),
)
