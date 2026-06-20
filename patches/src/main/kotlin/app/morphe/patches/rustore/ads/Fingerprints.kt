package app.morphe.patches.rustore.ads

import app.morphe.patcher.Fingerprint

/**
 * Matches `RawAdvertisementRepositoryImpl.get()` — single method
 * all ad loading flows converge on. Patching this blocks SSP,
 * MyTarget, and VKR ads without affecting other remote features.
 */
object RawAdvertisementRepoGetFingerprint : Fingerprint(
    definingClass = "Lj41/n0;",
    name = "a",
    custom = { method, _ -> method.parameters.size >= 7 },
)
