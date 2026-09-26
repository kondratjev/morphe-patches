package app.morphe.patches.lyfta.music

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.stringOption
import app.morphe.patches.lyfta.shared.Constants.COMPATIBILITY_LYFTA
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21c
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableStringReference
import java.util.logging.Logger

internal const val ORIGINAL_YOUTUBE_MUSIC_PACKAGE = "com.google.android.apps.youtube.music"

private const val PACKAGE_NAME_REGEX = "^[a-z]\\w*(\\.[a-z]\\w*)+\$"

@Suppress("unused")
val musicAppPatch = bytecodePatch(
    name = "Support patched YouTube Music",
    description = "Points the workout music button at a patched YouTube Music app " +
            "instead of the stock package, so the button opens it.",
) {
    compatibleWith(COMPATIBILITY_LYFTA)

    val youTubeMusicPackage by stringOption(
        key = "youTubeMusicPackage",
        default = "app.morphe.android.apps.youtube.music",
        values = mapOf(
            "Morphe" to "app.morphe.android.apps.youtube.music",
            "ReVanced" to "app.revanced.android.apps.youtube.music",
            "Anddea" to "anddea.youtube.music",
            "Clone" to "bill.youtube.music",
        ),
        title = "YouTube Music package",
        description = "Package name of the patched YouTube Music app installed on the device.",
        required = true,
    ) { it!!.matches(Regex(PACKAGE_NAME_REGEX)) }

    execute {
        val method = MusicIntentFingerprint.methodOrNull
            ?: return@execute Logger.getLogger(this::class.java.name)
                .warning("Could not find the music button handler. No changes applied.")

        val constIndex = MusicIntentFingerprint.stringMatches
            .first { it.string == ORIGINAL_YOUTUBE_MUSIC_PACKAGE }
            .index
        val register = method.getInstruction<OneRegisterInstruction>(constIndex).registerA
        method.replaceInstruction(
            constIndex,
            BuilderInstruction21c(
                Opcode.CONST_STRING,
                register,
                ImmutableStringReference(youTubeMusicPackage!!),
            ),
        )
    }
}
