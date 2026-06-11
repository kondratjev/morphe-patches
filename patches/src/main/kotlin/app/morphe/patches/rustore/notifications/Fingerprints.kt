package app.morphe.patches.rustore.notifications

import app.morphe.patcher.Fingerprint

/**
 * Matches `NotificationPermissionBannerDelegateImpl.d()` — the
 * `updateBanner` suspend wrapper that is called by the `updateBanners`
 * coroutine. Making this a no-op prevents ALL banner update logic,
 * keeping the banner state at its constructor-initialized `false`.
 *
 * Class `b` in `eq1` = NotificationPermissionBannerDelegateImpl.
 * Method `d` = updateBanner(Continuation): Object (returns Unit)
 */
object UpdateBannerFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Leq1/b;" &&
                method.name == "d" &&
                method.returnType == "Ljava/lang/Object;" &&
                method.parameters.size == 1
    }
)

/**
 * Matches `PermissionLauncher.a(String key, String permission)` —
 * the centralized method that ALL permission dialogs go through.
 * No-oping this suppresses the system notification permission dialog
 * AND the update-settings notification prompt.
 *
 * Class `al2.a` = PermissionLauncher.
 * Method `a` = launch(key, permission): void
 */
object PermissionLauncherFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lal2/a;" &&
                method.name == "a" &&
                method.returnType == "V" &&
                method.parameters.size == 2 &&
                method.parameterTypes[0] == "Ljava/lang/String;" &&
                method.parameterTypes[1] == "Ljava/lang/String;"
    }
)
