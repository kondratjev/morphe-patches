package app.morphe.patches.lyfta.premium

import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import app.morphe.patches.all.pairip.license.disableLicenseCheckPatch
import app.morphe.patches.lyfta.shared.Constants.COMPATIBILITY_LYFTA

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks all premium features.",
) {
    compatibleWith(COMPATIBILITY_LYFTA)

    dependsOn(disableLicenseCheckPatch)

    execute {
        // 1.575 (old UI): subscription tier string.
        val legacyPatched = GetSubscriptionTypeFingerprint.methodOrNull
            ?.returnEarly("premium") != null

        // 1.591 (new UI): central premium gate + tier string.
        val gatePatched = IsPremiumUserFingerprint.methodOrNull
            ?.returnEarly(true) != null
        val tierPatched = LocalAccessStateFingerprint.methodOrNull
            ?.returnEarly("premium") != null

        if (!legacyPatched && !gatePatched && !tierPatched) {
            throw PatchException("No premium fingerprints matched for version ${packageMetadata.versionName}")
        }
    }
}
