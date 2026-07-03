package app.morphe.patches.rustore.ads

import app.morphe.patcher.Fingerprint

/**
 * Matches `RawAdvertisementRepositoryImpl.get()` — single method
 * all ad loading flows converge on. Patching this blocks SSP,
 * MyTarget, and VKR ads without affecting other remote features.
 */
object RawAdvertisementRepoGetFingerprint : Fingerprint(
    definingClass = "Lk41/r0;",
    name = "a",
    custom = { method, _ -> method.parameters.size >= 7 },
)
