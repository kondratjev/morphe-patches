package app.kondratjev.patches.all.manifest.debug

import app.morphe.patcher.patch.resourcePatch
import app.morphe.util.getNode
import org.w3c.dom.Element

@Suppress("unused")
val enableDebugPatch = resourcePatch(
    name = "Enable debug",
    description = "Enables debugging on the app by setting android:debuggable=\"true\".",
    default = false
) {
    execute {
        document("AndroidManifest.xml").use { document ->
            (document.getNode("application") as Element)
                .setAttribute("android:debuggable", "true")
        }
    }
}
