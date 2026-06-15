package app.morphe.patches.pillo.analytics

import app.morphe.patcher.Fingerprint

/**
 * Matches `TrackersInitializer.create(Context)` (void, the implementation
 * method — JADX renames it to `create2` but bytecode name is `create`).
 * This is the single call site that initializes PulseSDK with the
 * events.pillo.care endpoint.
 *
 * `returnType = "V"` distinguishes from the bridge method (which returns Object).
 * When no-opped, PulseSDK.INSTANCE.getInstance() returns null,
 * so all PulseEventProvider methods early-exit and no native code runs.
 */
object TrackersInitializerCreateFingerprint : Fingerprint(
    definingClass = "Lxyz/rtrvr/pillo/initializers/TrackersInitializer;",
    name = "create",
    returnType = "V",
    parameters = listOf("Landroid/content/Context;"),
)
