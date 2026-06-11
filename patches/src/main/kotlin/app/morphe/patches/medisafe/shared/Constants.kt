package app.morphe.patches.medisafe.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY_MEDISAFE = Compatibility(
        name = "Medisafe",
        packageName = "com.medisafe.android.client",
        appIconColor = 0x00A99D,
        targets = listOf(
            AppTarget("9.50.3")
        )
    )
}
