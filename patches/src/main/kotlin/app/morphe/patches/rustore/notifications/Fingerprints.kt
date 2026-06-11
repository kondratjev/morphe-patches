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
