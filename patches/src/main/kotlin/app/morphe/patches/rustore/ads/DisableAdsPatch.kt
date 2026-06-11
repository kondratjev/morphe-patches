package app.morphe.patches.rustore.ads

import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.rustore.shared.Constants.COMPATIBILITY_RUSTORE
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.instruction.NarrowLiteralInstruction
import com.android.tools.smali.dexlib2.iface.reference.StringReference

private val AD_FEATURES = setOf(
    "featureAdvertisementAdminEnabled",
    "featureAdvertisementPreloadEnabled",
    "featureAdvertisementRedesignEnabled",
    "featureAdvertisementMultiSlotRequestEnabled",
)

@Suppress("unused")
val disableAdsPatch = bytecodePatch(
    name = "Disable ads",
    description = "Disables all advertisements by setting ad-related " +
            "feature toggle defaults to false. Other remote features " +
            "are left untouched.",
    default = true,
) {
    compatibleWith(COMPATIBILITY_RUSTORE)

    execute {
        val clinit = AdFeatureClinitFingerprint.method
        val instructions = clinit.implementation!!.instructions

        for (i in instructions.indices) {
            val instr = instructions[i]
            if (instr.opcode != Opcode.CONST_STRING) continue

            val ref = (instr as ReferenceInstruction).reference
            if (ref !is StringReference || ref.string !in AD_FEATURES) continue

            // Found an ad feature string — scan backward to find the
            // const/4 vX, 0x1 that sets the default value for this feature.
            var j = i - 1
            while (j >= 0) {
                val prev = instructions[j]
                if (prev.opcode == Opcode.CONST_4) {
                    val lit = prev as NarrowLiteralInstruction
                    if (lit.narrowLiteral == 1) {
                        val reg = (prev as OneRegisterInstruction).registerA
                        clinit.replaceInstruction(j, "const/4 v$reg, 0x0")
                    }
                    break
                }
                j--
            }
        }
    }
}
