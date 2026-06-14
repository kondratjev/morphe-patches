package app.morphe.patches.lifesum.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.lifesum.shared.Constants.COMPATIBILITY_LIFESUM
import app.morphe.util.returnEarly

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks all premium features.",
) {
    compatibleWith(COMPATIBILITY_LIFESUM)

    execute {
        // ── Hook 1: Force hasPremium to always return true ──
        // Covers all code paths that use the extension function.
        HasPremiumFingerprint.methodOrNull?.returnEarly(true)

        // ── Hook 2: Force Premium.a = true in constructor ──
        // Many code paths read premium.a directly (bypassing hasPremium):
        //   - l/jz4:205  (food summary — fiber/sugars/carbs)
        //   - l/p0:92    (meal summary — fiber/sugars)
        //   - l/vc3:275  (food detail via cr9)
        //   - l/cbd:20   (recipe/food detail PremiumLocked toolbar)
        //   - l/gp8:83   (lifestyle week tab)
        //   - l/sib:404  (top bar premium button)
        //   - CreateRecipeActivity:301,356
        //   - KetogenicSettingsActivity:221,274,326
        //   - TrackMeasurementActivity:257
        // Patching the constructor ensures every n5c instance has isPremium=true.
        PremiumConstructorFingerprint.methodOrNull?.addInstructions(
            0,
            """
                sget-object p1, Ljava/lang/Boolean;->TRUE:Ljava/lang/Boolean;
            """,
        )
    }
}
