package app.morphe.patches.pillo.medication

import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.pillo.shared.Constants.COMPATIBILITY_PILLO
import app.morphe.util.returnEarly

@Suppress("unused")
val allowPausedEditPatch = bytecodePatch(
    name = "Edit paused medications",
    description = "Allows editing paused medications without resuming them first.",
) {
    compatibleWith(COMPATIBILITY_PILLO)

    execute {
        // Paused-state check on the edit screen → always editable.
        EditSuspendedMedFingerprint.method.returnEarly(false)
    }
}
