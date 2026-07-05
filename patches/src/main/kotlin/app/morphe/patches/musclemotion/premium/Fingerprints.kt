package app.morphe.patches.musclemotion.premium

import app.morphe.patcher.Fingerprint

/**
 * Matches `l.b1.d()` — canonical getIsPremium() reads `sp_premium` from SharedPreferences.
 * Returns the boolean premium state used by all UI checks via `g2.n.s()`.
 */
object IsPremiumFingerprint : Fingerprint(
    definingClass = "Ll/b1;",
    name = "d",
    returnType = "Z",
    parameters = emptyList(),
)

/**
 * Matches `z0.z3.t0()` — reads `sp_android_subscription` boolean.
 * Called in billing flows as a secondary premium gate.
 */
object HasAndroidSubscriptionFingerprint : Fingerprint(
    definingClass = "Lz0/z3;",
    name = "t0",
    returnType = "Z",
    parameters = emptyList(),
)

/**
 * Matches `z0.z3.q0(boolean)` — writes `sp_premium = !cancelled` to SharedPreferences.
 * Called when user cancels subscription; patching to no-op preserves premium = true.
 */
object SaveUserStatusFingerprint : Fingerprint(
    definingClass = "Lz0/z3;",
    name = "q0",
    returnType = "V",
    parameters = listOf("Z"),
)
