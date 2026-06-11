package app.morphe.patches.rustore.ads

import app.morphe.patcher.Fingerprint

/**
 * Matches `RawAdvertisementRepositoryImpl.get()` — the single method
 * that ALL ad loading flows converge on. Patching this blocks SSP,
 * MyTarget, and VKR ads at the data source without affecting any
 * other remote features (notifications, security, etc.).
 *
 * Class `n0` in `j41` = RawAdvertisementRepositoryImpl.
 * Method `a` = get() — fetches ads from 3 sources concurrently.
 */
object RawAdvertisementRepoGetFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lj41/n0;" &&
                method.name == "a" &&
                method.parameters.size >= 7
    }
)
