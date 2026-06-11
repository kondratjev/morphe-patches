package app.morphe.patches.rustore.ads

import app.morphe.patcher.Fingerprint

/**
 * Matches the static initializer `<clinit>` of class `b` (Features.kt)
 * which contains the ad-related feature toggle defaults.
 */
object AdFeatureClinitFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lru/vk/store/lib/featuretoggle/b;" &&
                method.name == "<clinit>"
    }
)
