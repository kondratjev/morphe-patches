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

/**
 * Matches private `openHttpURLConnection(URL, String)` in
 * `FirebaseInstallationServiceClient` — sets the `X-Android-Cert`
 * header to the app's cert SHA-1.
 *
 * Note: Pillo's FIS build names it `openHttpURLConnection` (not
 * `openHttpUrlConnection`) and keeps the "Service is unavailable" string
 * in the neighbouring method, so only `X-Android-Cert` is used here.
 */
internal val FirebaseOpenHttpConnectionFingerprint = Fingerprint(
    definingClass = "Lcom/google/firebase/installations/remote/FirebaseInstallationServiceClient;",
    name = "openHttpURLConnection",
    returnType = "Ljava/net/HttpURLConnection;",
    parameters = listOf("Ljava/net/URL;", "Ljava/lang/String;"),
    strings = listOf("X-Android-Cert"),
)

/**
 * Matches private `getFingerprintHashForPackage()` in
 * `FirebaseInstallationServiceClient` — the source of the SHA-1.
 * It reads the cert via `AndroidUtilsLight.getPackageCertificateHashBytes()`,
 * which resolves through GMS process space, so the re-signed cert is
 * returned instead of the original.
 */
internal val FirebaseFingerprintHashFingerprint = Fingerprint(
    definingClass = "Lcom/google/firebase/installations/remote/FirebaseInstallationServiceClient;",
    name = "getFingerprintHashForPackage",
    returnType = "Ljava/lang/String;",
    accessFlags = listOf(com.android.tools.smali.dexlib2.AccessFlags.PRIVATE),
    parameters = listOf(),
)
