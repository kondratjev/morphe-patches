package app.morphe.patches.yazio.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY_YAZIO = Compatibility(
        name = "Yazio",
        packageName = "com.yazio.android",
        appIconColor = 0x00C853,
        targets = listOf(
            AppTarget("12.87.0")
        )
    )
}
