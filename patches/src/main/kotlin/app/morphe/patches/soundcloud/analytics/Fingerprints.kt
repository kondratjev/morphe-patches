package app.morphe.patches.soundcloud.analytics

import app.morphe.patcher.Fingerprint

/** Matches `TrackingHandler.handleMessage(Message)` — dispatches analytics events to their handlers. */
object HandleMessageFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/analytics/base/TrackingHandler;",
    name = "handleMessage",
    returnType = "V",
    parameters = listOf("Landroid/os/Message;"),
)
