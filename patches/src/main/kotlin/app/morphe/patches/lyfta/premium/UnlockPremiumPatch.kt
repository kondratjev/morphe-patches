package app.morphe.patches.lyfta.premium

import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.util.returnEarly
import app.morphe.patches.all.pairip.license.disableLicenseCheckPatch
import app.morphe.patches.lyfta.shared.Constants.COMPATIBILITY_LYFTA
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21c
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableStringReference

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks all premium features.",
) {
    compatibleWith(COMPATIBILITY_LYFTA)

    dependsOn(disableLicenseCheckPatch)

    execute {
        // 1.575 (old UI): subscription tier string.
        val legacyPatched = GetSubscriptionTypeFingerprint.methodOrNull
            ?.returnEarly("premium") != null

        // Central premium gate (1.575–1.600; inlined later).
        val gatePatched = IsPremiumUserFingerprint.methodOrNull
            ?.returnEarly(true) != null

        // 1.617: paywall router reads the tier via the generic session
        // getter — forcing the getter is unsafe (16 callers), so overwrite
        // the tier register with "premium" right after the read instead.
        // The premium branch (incl. its callback) then runs naturally.
        val routerPatched = PaywallRouterTierFingerprint.methodOrNull?.let { method ->
            val constIndex = PaywallRouterTierFingerprint.stringMatches
                .first { it.string == "SUBSCRIPTION" }.index
            var moveIndex = constIndex + 1
            while (method.getInstruction(moveIndex).opcode != Opcode.MOVE_RESULT_OBJECT &&
                moveIndex - constIndex < 8
            ) {
                moveIndex++
            }
            if (method.getInstruction(moveIndex).opcode != Opcode.MOVE_RESULT_OBJECT) {
                return@let false
            }
            val reg = method.getInstruction<OneRegisterInstruction>(moveIndex).registerA
            method.replaceInstruction(
                moveIndex,
                BuilderInstruction21c(Opcode.CONST_STRING, reg, ImmutableStringReference("premium")),
            )
            true
        } ?: false

        if (!legacyPatched && !gatePatched && !routerPatched) {
            throw PatchException("No premium fingerprints matched for version ${packageMetadata.versionName}")
        }
    }
}
