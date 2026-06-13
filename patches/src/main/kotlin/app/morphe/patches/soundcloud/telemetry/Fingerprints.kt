package app.morphe.patches.soundcloud.telemetry

import app.morphe.patcher.Fingerprint

object HandleMessageFingerprint : Fingerprint(
    definingClass = "Lcom/soundcloud/android/analytics/base/TrackingHandler;",
    name = "handleMessage",
    returnType = "V",
    parameters = listOf("Landroid/os/Message;"),
)
