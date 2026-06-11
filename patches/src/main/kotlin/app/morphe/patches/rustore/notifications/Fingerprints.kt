package app.morphe.patches.rustore.notifications

import app.morphe.patcher.Fingerprint

/**
 * Matches `NotificationPermissionBannerDelegateImpl.canShowInCurrentSession()` —
 * the decision method that determines whether the notification permission
 * banner/modal should be shown across all screens (Mine, Update Settings, etc.).
 *
 * Class `b` in `eq1` = NotificationPermissionBannerDelegateImpl.
 * Method `a` = canShowInCurrentSession(Continuation): Object
 */
object CanShowNotificationBannerFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Leq1/b;" &&
                method.name == "a" &&
                method.returnType == "Ljava/lang/Object;" &&
                method.parameters.size == 1
    }
)
