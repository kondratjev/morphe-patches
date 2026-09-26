package app.morphe.patches.lyfta.music

import app.morphe.patcher.Fingerprint

/**
 * Matches the workout music button handler in `BaseLogWorkoutVM` — picks the
 * stock YouTube Music package when Spotify is not connected, otherwise the
 * Spotify package, then fires `getLaunchIntentForPackage`.
 * Present in Lyfta 1.600.
 */
object MusicIntentFingerprint : Fingerprint(
    definingClass = "Lcom/lyfta/fragments/workout/base/BaseLogWorkoutVM;",
    strings = listOf(
        "com.google.android.apps.youtube.music",
        "com.spotify.music",
    ),
)
