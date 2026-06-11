package app.morphe.patches.rustore.auth

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE

@Suppress("unused")
val bypassAuthPatch = bytecodePatch(
    name = "Bypass authorization",
    description = "Makes the app behave as if the user is logged in " +
            "at the UI level. The main screen and profile will show " +
            "logged-in state without requiring actual authentication. " +
            "Server-side features like purchases still require real login.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        // ── Hook A: MineV2State.isLoggedIn = true ──
        // Controls the "My Apps" tab — shows full UI instead of
        // UNAUTHORIZED placeholder.
        val mineConstructor = MineV2StateConstructorFingerprint.classDef.methods
            .first { it.name == "<init>" }

        mineConstructor.addInstructions(
            mineConstructor.implementation!!.instructions.size - 1,
            "const/4 v0, 0x1\niput-boolean v0, p0, Lpi1/v7;->f85554a:Z"
        )

        // ── Hook B: UserProfileState.isLoggedIn = true ──
        // Controls the profile screen — shows user info instead of
        // "please log in" state.
        val profileConstructor = UserProfileStateConstructorFingerprint.classDef.methods
            .first { it.name == "<init>" }

        profileConstructor.addInstructions(
            profileConstructor.implementation!!.instructions.size - 1,
            "const/4 v0, 0x1\niput-boolean v0, p0, Lgf2/j1;->f44495a:Z"
        )
    }
}
