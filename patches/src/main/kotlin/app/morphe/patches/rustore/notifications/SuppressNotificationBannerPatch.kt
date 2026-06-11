package app.morphe.patches.rustore.notifications

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE

@Suppress("unused")
val suppressNotificationBannerPatch = bytecodePatch(
    name = "Suppress notification banner",
    description = "Prevents the notification permission banner from " +
            "appearing on settings and update screens. The \"Enable " +
            "notifications?\" prompt will no longer pop up. System " +
            "notification permission can still be toggled manually " +
            "in Android Settings → Apps → RuStore.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        // Hook updateBanner to be a no-op.
        // The banner state (eq1/b.f36911e MutableStateFlow) is initialized
        // to false in the constructor. Preventing updateBanner from running
        // keeps it false permanently — the banner never shows.
        UpdateBannerFingerprint.method.addInstructions(
            0,
            "const/4 v0, 0x0\nreturn-object v0"
        )
    }
}
