package app.morphe.patches.parcels.premium

import app.morphe.patcher.patch.rawResourcePatch
import app.morphe.patches.parcels.shared.Constants.COMPATIBILITY_PARCELS
import app.morphe.patches.all.misc.jsbundle.jsBundlePatch

/**
 * Premium unlock for Parcels (com.brightstripe.parcels 3.0.11).
 *
 * Single-point patch: forces isSubscriptionActive() to return true.
 * Based on analysis of the working premium APK.
 */
@Suppress("unused")
val unlockPremiumPatch = rawResourcePatch(
    name = "Unlock Premium",
    description = "Unlocks premium features.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_PARCELS)

    dependsOn(jsBundlePatch {
        // isSubscriptionActive() → always true
        // Unique anchor: includes AsyncStorage.getItem('Settings:subscribed') context
        // Only 1 match in the entire bundle. Same-length replacement.
        val isSubscriptionActive = "73 2E 64 65 66 61 75 6C 74 2E 61 77 72 61 70 28 63 2E 41 73 79 6E 63 53 74 6F 72 61 67 65 2E 67 65 74 49 74 65 6D 28 27 53 65 74 74 69 6E 67 73 3A 73 75 62 73 63 72 69 62 65 64 27 29 29 3B 63 61 73 65 20 33 3A 72 65 74 75 72 6E 20 74 3D 6E 2E 73 65 6E 74 2C 6E 2E 61 62 72 75 70 74 28 22 72 65 74 75 72 6E 22 2C 31 3D 3D 3D 70 61 72 73 65 49 6E 74 28 74 29 29" to
            "73 2E 64 65 66 61 75 6C 74 2E 61 77 72 61 70 28 63 2E 41 73 79 6E 63 53 74 6F 72 61 67 65 2E 67 65 74 49 74 65 6D 28 27 53 65 74 74 69 6E 67 73 3A 73 75 62 73 63 72 69 62 65 64 27 29 29 3B 63 61 73 65 20 33 3A 72 65 74 75 72 6E 20 74 3D 6E 2E 73 65 6E 74 2C 6E 2E 61 62 72 75 70 74 28 22 72 65 74 75 72 6E 22 2C 21 30 29 20 20 20 20 20 20 20 20 20 20 20 20 20"

        setOf(isSubscriptionActive)
    })
}
