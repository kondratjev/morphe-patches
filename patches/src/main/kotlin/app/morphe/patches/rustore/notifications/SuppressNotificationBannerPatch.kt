package app.morphe.patches.rustore.notifications

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE

@Suppress("unused")
val suppressNotificationBannerPatch = bytecodePatch(
    name = "Suppress notification banner",
    description = "Prevents the notification permission banner from " +
            "appearing on settings and update screens. The \"Enable " +
            "notifications?\" modal will no longer pop up. System " +
            "notification permission can still be toggled manually " +
            "in Android Settings → Apps → RuStore.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        CanShowNotificationBannerFingerprint.method.addInstructions(
            0,
            """
                sget-object v0, Ljava/lang/Boolean;->FALSE:Ljava/lang/Boolean;
                return-object v0
            """.trimIndent()
        )
    }
}
