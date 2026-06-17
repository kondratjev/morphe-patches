package app.morphe.patches.soundcloud.consent

import app.morphe.patcher.Fingerprint

/**
 * Matches `PrivacyConsentControllerModule.Companion.a(...)` — the Dagger
 * @Provides method that selects between the real OneTrust-backed controller
 * and `NoopPrivacyConsentController`.
 */
object PrivacyConsentControllerProviderFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/privacy/consent/main/PrivacyConsentControllerModule\$Companion;",
    name = "a",
    returnType = "Lcom/soundcloud/android/privacy/consent/base/PrivacyConsentController;",
    parameters = listOf(
        "Lcom/soundcloud/android/privacy/legislation/LegislationOperations;",
        "Ldagger/Lazy;",
    ),
)
