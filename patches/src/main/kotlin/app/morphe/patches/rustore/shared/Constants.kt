package app.morphe.patches.rustore.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY_RUSTORE = Compatibility(
        name = "RuStore",
        packageName = "ru.vk.store",
        appIconColor = 0x0077FF,
        targets = listOf(
            AppTarget("1.103.0.3")
        )
    )
}
