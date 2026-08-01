package app.morphe.patches.all.analytics

import org.w3c.dom.Element
import org.w3c.dom.Node

fun Element.childrenNamed(vararg names: String): List<Element> {
    val accepted = names.toSet()
    val result = mutableListOf<Element>()
    val nodes = childNodes
    for (i in 0 until nodes.length) {
        val node = nodes.item(i)
        if (node is Element && node.nodeName in accepted) result.add(node)
    }
    return result
}

fun Element.removeChildren(nodes: List<Node>) {
    nodes.forEach(::removeChild)
}

fun Element.getOrCreateApplicationMetaData(name: String): Element =
    childrenNamed("meta-data")
        .firstOrNull { it.getAttribute("android:name") == name }
        ?: ownerDocument.createElement("meta-data").also {
            it.setAttribute("android:name", name)
            appendChild(it)
        }

fun Element.setApplicationMetaData(name: String, value: String) {
    getOrCreateApplicationMetaData(name).setAttribute("android:value", value)
}

fun Element.disableComponentsWhere(predicate: (String) -> Boolean): Int {
    var disabled = 0
    childrenNamed("activity", "provider", "service", "receiver")
        .filter { predicate(it.getAttribute("android:name")) }
        .forEach {
            it.setAttribute("android:enabled", "false")
            it.setAttribute("android:exported", "false")
            disabled++
        }
    return disabled
}

fun Element.disableComponentsByName(vararg names: String): Int {
    val namesSet = names.toSet()
    return disableComponentsWhere { it in namesSet }
}

fun Element.disableComponentsByPrefix(vararg prefixes: String): Int =
    disableComponentsWhere { name -> prefixes.any { name.startsWith(it) } }
