package app.morphe.patches.lyfta.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY_LYFTA = Compatibility(
        name = "Lyfta",
        packageName = "com.lyfta",
        appIconColor = 0x000000,
        targets = listOf(
            AppTarget("1.573"),
        )
    )
}
