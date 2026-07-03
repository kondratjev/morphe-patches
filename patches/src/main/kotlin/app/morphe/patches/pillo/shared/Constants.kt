package app.morphe.patches.pillo.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY_PILLO = Compatibility(
        name = "Pillo",
        packageName = "xyz.rtrvr.pillo",
        appIconColor = 0x7C4DFF,
        targets = listOf(
            AppTarget("0.6.12")
        )
    )
}
