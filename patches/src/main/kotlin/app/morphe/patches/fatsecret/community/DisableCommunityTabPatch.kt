package app.morphe.patches.fatsecret.community

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patches.fatsecret.shared.Constants.COMPATIBILITY_FATSECRET
import app.morphe.patches.fatsecret.shared.hideMenuItem
import app.morphe.util.returnEarly

private val disableCommunityTabResourcePatch = resourcePatch {
    compatibleWith(COMPATIBILITY_FATSECRET)

    execute {
        document("res/menu/bottom_nav_menu.xml").use { document ->
            document.documentElement.hideMenuItem("tab_home")
        }
    }
}

@Suppress("unused")
val disableCommunityTabPatch = bytecodePatch(
    name = "Disable community tab",
    description = "Hides the News/Community tab from the bottom navigation.",
) {
    compatibleWith(COMPATIBILITY_FATSECRET)

    dependsOn(disableCommunityTabResourcePatch)

    execute {
        // Also patch bytecode to return -1 (fallback for resource-only patch).
        NewsTabIndexFingerprint.methodOrNull?.returnEarly(-1)
    }
}
