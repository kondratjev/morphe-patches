package app.morphe.patches.rustore.auth

import app.morphe.patcher.Fingerprint

/**
 * Matches `AuthSuggestDelegateImpl.ensureAuthSuggestShown()` — the SINGLE
 * centralized method that checks authorization before showing the login
 * modal when the user taps "Update all" or any individual update button.
 *
 * Class `e` in `b61` = AuthSuggestDelegateImpl.
 * Method `a` = ensureAuthSuggestShown(Continuation): Object
 */
object AuthSuggestShownFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lb61/e;" &&
                method.name == "a" &&
                method.returnType == "Ljava/lang/Object;" &&
                method.parameters.size == 1
    }
)
