package app.morphe.patches.rustore.debug

import app.morphe.patcher.Fingerprint

/**
 * Matches `FlipperRepository.f(Feature$a$a)Z` — the static method that reads
 * local (DEBUG) feature toggle values. Making this always return true
 * enables `inAppDebugScreenEnabled` and all other debug features.
 *
 * Class `c` = FlipperRepository, method `f` = reads local feature boolean.
 */
object FlipperRepoGetLocalFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lru/vk/store/lib/featuretoggle/c;" &&
                method.name == "f" &&
                method.returnType == "Z"
    }
)
