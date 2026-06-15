package app.morphe.patches.soundcloud.consent

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.soundcloud.shared.Constants.COMPATIBILITY_SOUNDCLOUD

@Suppress("unused")
val disableConsentPatch = bytecodePatch(
    name = "Disable OneTrust consent popup",
    description = "Disables the OneTrust consent/cookies popup on first launch.",
) {
    compatibleWith(COMPATIBILITY_SOUNDCLOUD)

    execute {
        PrivacyConsentControllerProviderFingerprint.methodOrNull?.addInstructions(
            0,
            """
                sget-object v0, Lcom/soundcloud/android/privacy/consent/base/NoopPrivacyConsentController;->a:Lcom/soundcloud/android/privacy/consent/base/NoopPrivacyConsentController;
                return-object v0
            """,
        )
    }
}
