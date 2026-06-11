package app.morphe.patches.rustore.notifications

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE

@Suppress("unused")
val suppressPermissionDialogsPatch = bytecodePatch(
    name = "Suppress permission dialogs",
    description = "Suppresses ALL system permission dialogs " +
            "(notifications, storage, camera, etc.) including the " +
            "in-app notification banner. Permissions can still be " +
            "toggled manually in Android Settings → Apps → RuStore.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        // Hook A: Prevent the in-app notification banner from showing
        // by no-oping updateBanner(). The banner state stays at its
        // constructor-initialized false.
        UpdateBannerFingerprint.method.addInstructions(
            0,
            "const/4 v0, 0x0\nreturn-object v0"
        )

        // Hook B: Prevent ALL system permission dialogs from RuStore.
        // This covers all three notification permission paths:
        // - Update settings screen (AUTO_UPDATE_SCREEN_PERMISSIONS_KEY)
        // - Auto-update notifications (auto_update_notifications)
        // - Start permissions flow (NOTIFICATIONS_PERMISSION_KEY)
        PermissionLauncherFingerprint.method.addInstructions(
            0,
            "return-void"
        )
    }
}
