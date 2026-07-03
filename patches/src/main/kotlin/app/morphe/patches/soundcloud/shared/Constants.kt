package app.morphe.patches.soundcloud.shared

import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY_SOUNDCLOUD = Compatibility(
        name = "SoundCloud",
        packageName = "com.soundcloud.android",
        appIconColor = 0xFF5500,
        targets = listOf(
            AppTarget("2026.06.25-release")
        )
    )
}
