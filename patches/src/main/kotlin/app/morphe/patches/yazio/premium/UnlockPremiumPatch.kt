package app.morphe.patches.yazio.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.yazio.shared.Constants.COMPATIBILITY_YAZIO

@Suppress("unused")
val unlockPremiumPatch = bytecodePatch(
    name = "Unlock Premium",
    description = "Unlocks all Yazio Pro features.",
) {
    compatibleWith(COMPATIBILITY_YAZIO)

    execute {
        // j18.M(SubscriptionStatus) — primary premium check
        SubscriptionCheckFingerprint.methodOrNull?.addInstructions(
            0, "const/4 v0, 0x1\nreturn v0",
        )

        // j18.H(SubscriptionStatus) — lenient check (includes WillExpire)
        SubscriptionCheckLenientFingerprint.methodOrNull?.addInstructions(
            0, "const/4 v0, 0x1\nreturn v0",
        )

        // ooe.h(Continuation) — PremiumType null check (inverted logic)
        // Returns Boolean.TRUE when NOT premium. We return FALSE to
        // indicate "user IS premium". Coroutine → return-object.
        PremiumTypeNullCheckFingerprint.methodOrNull?.addInstructions(
            0,
            """
                const/4 v0, 0x0
                invoke-static {v0}, Ljava/lang/Boolean;->valueOf(Z)Ljava/lang/Boolean;
                move-result-object v0
                return-object v0
            """,
        )
    }
}
