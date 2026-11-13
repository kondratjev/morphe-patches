package app.morphe.patches.pillo.medication

import app.morphe.patcher.Fingerprint

/**
 * Matches `EditMedEntryData.isSuspended()` — reports whether the med
 * being edited is paused. Read only by the edit screen scaffold;
 * forcing false makes paused meds editable without resuming first.
 */
object EditSuspendedMedFingerprint : Fingerprint(
    definingClass = "Lxyz/rtrvr/pillo/ui/medication/edit/entry/data/EditMedEntryData;",
    name = "isSuspended",
    returnType = "Z",
    parameters = emptyList(),
)
