package app.morphe.patches.fatsecret.community

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patches.fatsecret.shared.Constants.COMPATIBILITY_FATSECRET
import org.w3c.dom.Element

private val disableCommunityTabResourcePatch = resourcePatch {
    compatibleWith(COMPATIBILITY_FATSECRET)

    execute {
        document("res/menu/bottom_nav_menu.xml").use { document ->
            val menu = document.documentElement
            val nodes = menu.childNodes
            for (i in 0 until nodes.length) {
                val node = nodes.item(i)
                if (node is Element && node.tagName == "item" &&
                    node.getAttribute("android:id") == "@id/tab_home"
                ) {
                    node.setAttribute("android:visible", "false")
                }
            }
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
        // Belt-and-suspenders: also patch bytecode
        NewsTabIndexFingerprint.methodOrNull?.addInstructions(
            0, "const/4 v0, -0x1\nreturn v0",
        )
    }
}
