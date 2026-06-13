package app.morphe.patches.fatsecret.community

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.fatsecret.shared.Constants.COMPATIBILITY_FATSECRET

@Suppress("unused")
val disableCommunityTabPatch = bytecodePatch(
    name = "Disable community tab",
    description = "Hides the News/Community tab from the bottom navigation.",
) {
    compatibleWith(COMPATIBILITY_FATSECRET)

    execute {
        // BottomNavTab$News.fetchIndexInBottomNav() → return -1
        // All tab comparisons will fail for News, effectively disabling it
        NewsTabIndexFingerprint.methodOrNull?.addInstructions(
            0, "const/4 v0, -0x1\nreturn v0",
        )
    }
}
