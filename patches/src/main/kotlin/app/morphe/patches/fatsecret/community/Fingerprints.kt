package app.morphe.patches.fatsecret.community

import app.morphe.patcher.Fingerprint

/**
 * Matches `BottomNavTab$News.fetchIndexInBottomNav()` — returns the
 * News/Community tab position (0). Patching to -1 disables the tab.
 */
object NewsTabIndexFingerprint : Fingerprint(
    definingClass = "Lcom/fatsecret/android/cores/core_entity/domain/BottomNavTab\$News;",
    name = "fetchIndexInBottomNav",
    returnType = "I",
    parameters = emptyList(),
)
