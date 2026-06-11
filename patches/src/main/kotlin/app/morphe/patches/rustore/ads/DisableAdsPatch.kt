package app.morphe.patches.rustore.ads

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE

@Suppress("unused")
val disableAdsPatch = bytecodePatch(
    name = "Disable ads",
    description = "Disables all advertisements by forcing remote " +
            "feature toggle reads to return false for all features. " +
            "Note: this may affect other remote features as well.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        FlipperRepoGetRemoteFingerprint.method.addInstructions(
            0,
            """
                sget-object v0, Ljava/lang/Boolean;->FALSE:Ljava/lang/Boolean;
                return-object v0
            """.trimIndent()
        )
    }
}
