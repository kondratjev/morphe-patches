package app.morphe.patches.wisemeal.premium

import app.morphe.patcher.Fingerprint

/**
 * Matches `w7.c.w()Z` — the IapManager singleton's primary premium check.
 * All 40+ premium gates in the app route through this method.
 *
 * DEX: classes.dex, Lw7/c;->w()Z
 */
object IsPremiumFingerprint : Fingerprint(
    definingClass = "Lw7/c;",
    name = "w",
    returnType = "Z",
    parameters = emptyList(),
)
