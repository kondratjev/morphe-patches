package app.morphe.patches.wisemeal.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY_WISEMEAL = Compatibility(
        name = "WiseMeal",
        packageName = "com.quvideo.wisemeal",
        appIconColor = 0x00C853,
        targets = listOf(
            AppTarget("1.3.0")
        )
    )
}
