package app.morphe.patches.fatsecret.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patches.fatsecret.shared.Constants.COMPATIBILITY_FATSECRET
import org.w3c.dom.Element

private val hidePremiumTabResourcePatch = resourcePatch {
    compatibleWith(COMPATIBILITY_FATSECRET)

    execute {
        document("res/menu/bottom_nav_menu.xml").use { document ->
            val menu = document.documentElement
            val nodes = menu.childNodes
            for (i in 0 until nodes.length) {
                val node = nodes.item(i)
                if (node is Element && node.tagName == "item" &&
                    node.getAttribute("android:id") == "@id/tab_premium"
                ) {
                    node.setAttribute("android:visible", "false")
                }
            }
        }
    }
}

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks all FatSecret Gold features and hides the Premium tab.",
) {
    compatibleWith(COMPATIBILITY_FATSECRET)
    dependsOn(hidePremiumTabResourcePatch)

    execute {
        // t0.h() — primary isPremium check → always true
        IsPremiumFingerprint.methodOrNull?.addInstructions(
            0, "const/4 v0, 0x1\nreturn v0",
        )

        // t0.g() — premium status loaded → always true
        IsPremiumLoadedFingerprint.methodOrNull?.addInstructions(
            0, "const/4 v0, 0x1\nreturn v0",
        )

        // t0.e() — invalid subscription check → always false
        IsInvalidSubscriptionFingerprint.methodOrNull?.addInstructions(
            0, "const/4 v0, 0x0\nreturn v0",
        )

        // t0.m(boolean, boolean) — StateFlow emitter
        PremiumStatusEmitterFingerprint.methodOrNull?.addInstructions(
            0,
            """
                const/4 p1, 0x1
                const/4 p2, 0x1
            """,
        )
    }
}
