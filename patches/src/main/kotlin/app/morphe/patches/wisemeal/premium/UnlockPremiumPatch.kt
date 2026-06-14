package app.morphe.patches.wisemeal.premium

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.wisemeal.shared.Constants.COMPATIBILITY_WISEMEAL
import app.morphe.patches.all.pairip.license.disableLicenseCheckPatch
import app.morphe.util.returnEarly

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks all premium features. Need root for Google Sign-In working.",
) {
    compatibleWith(COMPATIBILITY_WISEMEAL)

    dependsOn(disableLicenseCheckPatch)

    execute {
        IsPremiumFingerprint.methodOrNull?.returnEarly(true)
    }
}
