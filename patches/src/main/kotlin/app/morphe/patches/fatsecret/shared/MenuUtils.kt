package app.morphe.patches.fatsecret.shared

import org.w3c.dom.Element

fun Element.hideMenuItem(itemId: String) {
    val nodes = childNodes
    for (i in 0 until nodes.length) {
        val node = nodes.item(i)
        if (node is Element && node.tagName == "item" &&
            node.getAttribute("android:id") == "@id/$itemId"
        ) {
            node.setAttribute("android:visible", "false")
        }
    }
}
