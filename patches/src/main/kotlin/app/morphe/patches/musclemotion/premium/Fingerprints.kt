package app.morphe.patches.musclemotion.premium

import app.morphe.patcher.Fingerprint

/**
 * Matches `fr5.e()` — canonical getIsPremium() reads `sp_premium` from SharedPreferences.
 * Returns the boolean premium state used by all UI checks.
 * (Was `l/b1.d()` in 3.5.0; `uq5.e()` in 3.5.5; R8 renamed the class in 3.5.6, same logic.)
 */
object IsPremiumFingerprint : Fingerprint(
    definingClass = "Lfr5;",
    name = "e",
    returnType = "Z",
    parameters = emptyList(),
)

/**
 * Matches `vn7.F0()` — reads `sp_android_subscription` boolean.
 * Called in billing flows as a secondary premium gate.
 * (Was `z0/z3.t0()` in 3.5.0; `fn7.F0()` in 3.5.5; R8 renamed the class in 3.5.6, same logic.)
 */
object HasAndroidSubscriptionFingerprint : Fingerprint(
    definingClass = "Lvn7;",
    name = "F0",
    returnType = "Z",
    parameters = emptyList(),
)

/**
 * Matches `vn7.C0(boolean)` — writes `sp_premium = !cancelled` to SharedPreferences.
 * Called when user cancels subscription; patching to no-op preserves premium = true.
 * (Was `z0/z3.q0()` in 3.5.0; `fn7.C0()` in 3.5.5; R8 renamed the class in 3.5.6, same logic.)
 */
object SaveUserStatusFingerprint : Fingerprint(
    definingClass = "Lvn7;",
    name = "C0",
    returnType = "V",
    parameters = listOf("Z"),
)
