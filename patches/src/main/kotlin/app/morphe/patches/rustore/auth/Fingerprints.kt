package app.morphe.patches.rustore.auth

import app.morphe.patcher.Fingerprint

/**
 * Matches `AuthSuggestDelegateImpl.ensureAuthSuggestShown()` — centralized
 * method that checks authorization before showing login modal on update.
 */
object AuthSuggestShownFingerprint : Fingerprint(
    definingClass = "Lb61/e;",
    name = "a",
    returnType = "Ljava/lang/Object;",
    custom = { method, _ -> method.parameters.size == 1 },
)
