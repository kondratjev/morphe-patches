package app.morphe.patches.pillo.analytics

import app.morphe.patcher.Fingerprint

/**
 * Matches `TrackersInitializer.create2(Context)` — the single call site
 * that initializes PulseSDK with the events.pillo.care endpoint.
 *
 * When this is no-opped, PulseSDK.INSTANCE.getInstance() returns null,
 * so all PulseEventProvider methods early-exit and no native code runs.
 */
object TrackersInitializerCreateFingerprint : Fingerprint(
    definingClass = "Lxyz/rtrvr/pillo/initializers/TrackersInitializer;",
    name = "create2",
    returnType = "V",
    parameters = listOf("Landroid/content/Context;"),
)
