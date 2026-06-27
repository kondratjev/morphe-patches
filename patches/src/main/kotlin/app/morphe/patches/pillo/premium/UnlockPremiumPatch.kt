package app.morphe.patches.pillo.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import app.morphe.patches.pillo.shared.Constants.COMPATIBILITY_PILLO
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks premium features and removes ads."
) {
    compatibleWith(COMPATIBILITY_PILLO)

    execute {
        // Force all setIs...State parameters to true.
        listOf(SetIsPremiumStateFingerprint, SetIsAdfreeStateFingerprint).forEach { f ->
            f.method.addInstruction(0, "const/4 p1, 0x1")
        }

        // Force getLastIs...SubscriptionState returns to true in constructor.
        SubscriptionStateProviderConstructorFingerprint.method.forceBooleanReturn(
            definingClass = PREFERENCES_CLASS,
            "getLastIsPremiumSubscriptionState",
            "getLastIsAdfreeSubscriptionState",
        )

        // Block Adapty SDK init (subscription/paywall tracking).
        AdaptyInitializerCreateFingerprint.methodOrNull?.returnEarly()

        // Block AdMob SDK init (ads gated by isAdfree anyway; skip spin-up).
        AdmobInitializerCreateFingerprint.methodOrNull?.returnEarly()
    }
}

/**
 * Replace every `move-result vX` that follows an `INVOKE_VIRTUAL` call to any
 * of [methodNames] on [definingClass] with `const/4 vX, 0x1`.
 */
private fun MutableMethod.forceBooleanReturn(definingClass: String, vararg methodNames: String) {
    val instructions = implementation!!.instructions
    for (i in instructions.indices) {
        val instr = instructions[i]
        if (instr.opcode != Opcode.INVOKE_VIRTUAL) continue
        val ref = (instr as ReferenceInstruction).reference
        if (ref !is MethodReference) continue
        if (ref.definingClass != definingClass) continue
        if (ref.name !in methodNames) continue

        val moveResult = instructions[i + 1]
        val reg = (moveResult as OneRegisterInstruction).registerA
        replaceInstruction(i + 1, "const/4 v$reg, 0x1")
    }
}
