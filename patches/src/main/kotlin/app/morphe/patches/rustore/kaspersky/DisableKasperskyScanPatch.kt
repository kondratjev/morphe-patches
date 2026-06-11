package app.morphe.patches.rustore.kaspersky

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.instruction.NarrowLiteralInstruction
import com.android.tools.smali.dexlib2.iface.reference.FieldReference

@Suppress("unused")
val disableKasperskyScanPatch = bytecodePatch(
    name = "Disable background scan",
    description = "Disables the Kaspersky-powered periodic device scan " +
            "by default on first install. The scan checks all files for " +
            "vulnerabilities. Manual scan in Security settings still works, " +
            "and existing users keep their stored preference.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        val constructor = KasperskyScannerDtoConstructorFingerprint.method
        val instructions = constructor.implementation!!.instructions

        for (i in instructions.indices) {
            val instr = instructions[i]
            if (instr.opcode != Opcode.IPUT_BOOLEAN) continue

            val ref = (instr as ReferenceInstruction).reference
            if (ref !is FieldReference ||
                ref.name != "isPeriodicScanEnabled" ||
                ref.type != "Z"
            ) continue

            // Found the field write — check if previous instruction
            // is const/4 vX, 0x1 (the default value assignment).
            val prev = instructions[i - 1]
            if (prev.opcode == Opcode.CONST_4) {
                val lit = prev as NarrowLiteralInstruction
                if (lit.narrowLiteral == 1) {
                    val reg = (prev as OneRegisterInstruction).registerA
                    constructor.replaceInstruction(i - 1, "const/4 v$reg, 0x0")
                }
            }
        }
    }
}
