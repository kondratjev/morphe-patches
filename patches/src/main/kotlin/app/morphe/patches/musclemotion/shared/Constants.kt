package app.morphe.patches.musclemotion.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY_MUSCLEMOTION = Compatibility(
        name = "Muscle Motion Strength Training",
        packageName = "air.com.musclemotion.strength.mobile",
        appIconColor = 0x000000,
        targets = listOf(
            AppTarget("3.5.0"),
        ),
    )
}
