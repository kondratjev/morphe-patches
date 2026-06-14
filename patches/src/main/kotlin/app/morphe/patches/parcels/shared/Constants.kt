package app.morphe.patches.parcels.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY_PARCELS = Compatibility(
        name = "Parcels",
        packageName = "com.brightstripe.parcels",
        appIconColor = 0x6B4EFF,
        targets = listOf(
            AppTarget("3.0.11")
        )
    )
}
