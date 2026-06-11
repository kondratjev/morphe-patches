package app.morphe.patches.rustore.kaspersky

import app.morphe.patcher.Fingerprint

/**
 * Matches the `KasperskyScannerDto` protobuf constructor — the
 * deserialization entry point where `isPeriodicScanEnabled` defaults
 * to `true` on first install before any user interaction.
 *
 * Signature: `(int seen0, boolean isScanResultViewed,
 *              boolean isPeriodicScanEnabled, serializationMarker)`
 */
object KasperskyScannerDtoConstructorFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lru/vk/store/feature/kaspersky/impl/data/KasperskyScannerDto;" &&
                method.name == "<init>" &&
                method.parameters.size == 4
    }
)
