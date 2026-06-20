package app.morphe.patches.ornament.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY_ORNAMENT = Compatibility(
        name = "Ornament",
        packageName = "com.ornament.monitor",
        appIconColor = 0x6750A4,
        targets = listOf(
            AppTarget("4.15.1")
        )
    )
}
