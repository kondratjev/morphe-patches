package app.morphe.patches.wisemeal.premium

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.wisemeal.shared.Constants.COMPATIBILITY_WISEMEAL
import app.morphe.patches.all.pairip.license.disableLicenseCheckPatch
import app.morphe.util.returnEarly

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "ROOT ONLY. Unlocks all premium features.",
) {
    compatibleWith(COMPATIBILITY_WISEMEAL)

    dependsOn(disableLicenseCheckPatch)

    execute {
        IsPremiumFingerprint.methodOrNull?.returnEarly(true)
    }
}
