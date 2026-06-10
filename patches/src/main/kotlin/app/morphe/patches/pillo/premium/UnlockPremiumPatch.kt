package app.morphe.patches.pillo.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.pillo.shared.Constants.COMPATIBILITY_PILLO
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks premium features and removes ads by forcing subscription state to always be active."
) {
    compatibleWith(COMPATIBILITY_PILLO)

    execute {
        // ── Hook A: Force setIsPremiumState parameter to true ──
        SetIsPremiumStateFingerprint.method.addInstruction(
            0,
            "const/4 p1, 0x1"
        )

        // ── Hook B: Force setIsAdfreeState parameter to true ──
        SetIsAdfreeStateFingerprint.method.addInstruction(
            0,
            "const/4 p1, 0x1"
        )

        // ── Hook C: Force constructor-initialized flows to true ──
        // Override the results of getLastIsPremiumSubscriptionState()
        // and getLastIsAdfreeSubscriptionState() so mIsPremium
        // and mIsAdfree are initialized as true from the start.
        val constructor = SubscriptionStateProviderConstructorFingerprint.method
        val instructions = constructor.implementation!!.instructions

        for (i in instructions.indices) {
            val instr = instructions[i]
            if (instr.opcode == Opcode.INVOKE_VIRTUAL) {
                val ref = (instr as ReferenceInstruction).reference
                if (ref is MethodReference &&
                    ref.definingClass == "Lxyz/rtrvr/pillo/persistence/preferences/Preferences;" &&
                    (ref.name == "getLastIsPremiumSubscriptionState" ||
                     ref.name == "getLastIsAdfreeSubscriptionState")
                ) {
                    // The next instruction is move-result — replace it with const/4 vX, 0x1
                    val moveResult = instructions[i + 1]
                    val reg = (moveResult as OneRegisterInstruction).registerA
                    constructor.replaceInstruction(i + 1, "const/4 v$reg, 0x1")
                }
            }
        }
    }
}
