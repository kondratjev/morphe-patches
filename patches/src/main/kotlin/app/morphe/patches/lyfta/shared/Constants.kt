package app.morphe.patches.lyfta.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY_LYFTA = Compatibility(
        name = "Lyfta",
        packageName = "com.lyfta",
        appIconColor = 0x000000,
        targets = listOf(
            AppTarget("1.572"),
            AppTarget("1.570"),
            AppTarget("1.568"),
            AppTarget("1.564"),
            AppTarget("1.560"),
            AppTarget("1.557"),
            AppTarget("1.554"),
            AppTarget("1.551"),
        )
    )
}
