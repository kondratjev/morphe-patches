package app.morphe.patches.fatsecret.community

import app.morphe.patcher.Fingerprint

/**
 * Matches `BottomNavTab$News.fetchIndexInBottomNav()` — returns 0
 * (the News/Community tab position). Patching to return -1 effectively
 * disables the community tab from bottom navigation.
 *
 * DEX: classes2.dex,
 * Lcom/fatsecret/android/cores/core_entity/domain/BottomNavTab$News;
 * -> fetchIndexInBottomNav()I
 */
object NewsTabIndexFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/fatsecret/android/cores/core_entity/domain/BottomNavTab\$News;" &&
            method.name == "fetchIndexInBottomNav" &&
            method.returnType == "I" &&
            method.parameterTypes.isEmpty()
    }
)
