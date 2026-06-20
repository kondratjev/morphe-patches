package app.morphe.patches.ornament.premium

import app.morphe.patches.all.misc.hermes.hermesPatch
import app.morphe.patches.ornament.shared.Constants.COMPATIBILITY_ORNAMENT
import app.morphe.patcher.patch.bytecodePatch

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks part of Ornament Pro features.",
) {
    compatibleWith(COMPATIBILITY_ORNAMENT)

    dependsOn(
        hermesPatch {
            setOf(
                // Hermes opcodes: 0x78 = LoadConstTrue, 0x79 = LoadConstFalse, 0x5C = Ret

                // Patch getOfferingLevel to always return true.
                // Original: LoadParam r3,1 + LoadParam r2,2 + GetEnvironment r1,0 + LoadFromEnv r6,r1,0
                // Replaced: LoadConstTrue r0 + Ret r0
                "6C 03 01 6C 02 02 29 01 00 2E 06 01" to
                    "78 00 5C 00 00 00 00 00 00 00 00 00",

                // Patch useHasSubscription hook to always return true.
                // Full function unique (61 bytes): useSelector(getActiveSubscription)
                // Replaced: LoadConstTrue r0 + Ret r0
                "29 00 00 2E 03 00 00 2E 04 00 01 6E 00 01 49 00 04 00 76 02 53 00 03 02 00" +
                    "36 01 00 01 F5 6E 00 02 49 00 04 00 53 00 03 02 00 37 00 00 02 38 FA" +
                    "37 00 00 03 9A BC 53 00 01 02 00 5C 00" to
                    "78 00 5C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00" +
                    "00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00" +
                    "00 00 00 00 00 00 00 00 00 00 00 00 00",

                // Patch accessToFeature generator to always return true.
                // Unique context (14 bytes):
                //   JmpTrue r1 -> JmpTrue r0 (never jumps, r0=undefined=falsy)
                //   LoadConstFalse r1 -> LoadConstTrue r1
                "08 03 05 08 02 03 90 08 01 79 01 88 5C 01" to
                    "08 03 05 08 02 03 90 08 00 78 01 88 5C 01",

                // getSubscriptionLevel: replace STANDARD/LITE returns with PRO.
                // Pattern: 37 <dst> <src> <cache> <strId_lo> <strId_hi> 5c <ret>
                //   62 80 = STANDARD, 02 BC = LITE, 66 72 = PRO
                "37 03 01 12 62 80 5C 03" to "37 03 01 12 66 72 5C 03",
                "37 01 01 12 62 80 5C 01" to "37 01 01 12 66 72 5C 01",
                "37 00 00 12 62 80 5C 00" to "37 00 00 12 66 72 5C 00",
                "37 03 03 0B 02 BC 5C 03" to "37 03 03 0B 66 72 5C 03",
                "37 03 01 0B 02 BC 5C 03" to "37 03 01 0B 66 72 5C 03",

                // Rename string "isPaid" -> "isFree" in string table.
                // isPaid is a prefix of isPaidContent (shared storage).
                // Both become isFree/isFreeContent -> property access returns undefined (falsy).
                // Server sends isPaid/isPaidContent, app reads isFree/isFreeContent -> always undefined.
                "69 73 50 61 69 64" to "69 73 46 72 65 65",

                // Patch usePaywallAccess inner selector (closure 23418) to always return true.
                // usePaywallAccess is the MAIN paywall hook (51 usages).
                // It calls getAccessToFeature Redux selector via useSelector(closure).
                // Patch the closure to return true -> useSelector gets true -> hasSubscription=true.
                "29 00 01 2E 01 00 00 2E 02 00 01 6E 00 01 49 00 02 00 76 05" to
                    "78 00 5C 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00",
            )
        },
    )
}
