package app.morphe.patches.rustore.ads

import app.morphe.patcher.Fingerprint

/**
 * Matches `FlipperRepository.c(Feature$Remote$a, Continuation)Object` —
 * the static method that reads remote feature toggle values from the
 * Omicron config server and local cache.
 */
object FlipperRepoGetRemoteFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lru/vk/store/lib/featuretoggle/c;" &&
                method.name == "c" &&
                method.parameters.size == 2
    }
)
