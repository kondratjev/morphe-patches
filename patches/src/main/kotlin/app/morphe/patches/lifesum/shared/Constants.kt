package app.morphe.patches.lifesum.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY_LIFESUM = Compatibility(
        name = "Lifesum",
        packageName = "com.sillens.shapeupclub",
        appIconColor = 0x00C853,
        targets = listOf(
            AppTarget("20.6.1")
        )
    )
}
