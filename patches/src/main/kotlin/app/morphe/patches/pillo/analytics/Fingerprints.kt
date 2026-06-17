package app.morphe.patches.pillo.analytics

import app.morphe.patcher.Fingerprint

/**
 * Matches `TrackersInitializer.create(Context)` — initializes PulseSDK
 * with the events.pillo.care endpoint. When no-opped, PulseSDK
 * returns null, so all PulseEventProvider methods early-exit.
 */
object TrackersInitializerCreateFingerprint : Fingerprint(
    definingClass = "Lxyz/rtrvr/pillo/initializers/TrackersInitializer;",
    name = "create",
    returnType = "V",
    parameters = listOf("Landroid/content/Context;"),
)
