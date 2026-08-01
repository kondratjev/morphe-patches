package app.morphe.patches.pillo.gms

import app.morphe.patcher.Fingerprint

/**
 * Matches the availability check used by the bundled Play Services client.
 *
 * MicroG-RE is exposed through the rewritten package name, so the bundled
 * check must not reject it based on the original Google package/signature.
 */
object GooglePlayServicesAvailableFingerprint : Fingerprint(
    definingClass = "Lcom/google/android/gms/common/GooglePlayServicesUtilLight;",
    name = "isGooglePlayServicesAvailable",
    returnType = "I",
    parameters = listOf("Landroid/content/Context;", "I"),
)
