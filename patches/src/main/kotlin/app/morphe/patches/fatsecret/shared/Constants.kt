package app.morphe.patches.fatsecret.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY_FATSECRET = Compatibility(
        name = "FatSecret",
        packageName = "com.fatsecret.android",
        appIconColor = 0x00897B,
        targets = listOf(
            AppTarget("11.6.0.1")
        )
    )
}
