package app.morphe.patches.rustore.auth

import app.morphe.patcher.Fingerprint

/**
 * Matches the `MineV2State` constructor (class `v7` in package `pi1`).
 * This class holds the `isLoggedIn` field (`f85554a`) that controls
 * whether the "My Apps" tab shows logged-in or unauthorized UI.
 *
 * The constructor sets `isLoggedIn = false` by default for anonymous users.
 * Patching it to `true` makes the app behave as if the user is logged in
 * at the UI level, without modifying network-level auth interceptors.
 */
object MineV2StateConstructorFingerprint : Fingerprint(
    custom = { _, classDef ->
        classDef.type == "Lpi1/v7;" &&
                classDef.methods.any { it.name == "<init>" }
    }
)

/**
 * Matches the `UserProfileState` constructor (class `j1` in package `gf2`).
 * Controls `isLoggedIn` on the profile screen.
 */
object UserProfileStateConstructorFingerprint : Fingerprint(
    custom = { _, classDef ->
        classDef.type == "Lgf2/j1;" &&
                classDef.methods.any { it.name == "<init>" }
    }
)
