package app.morphe.patches.soundcloud.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.soundcloud.shared.Constants.COMPATIBILITY_SOUNDCLOUD

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable SoundCloud Go+",
    description = "Enables all premium features, hides upsell UI and ads.",
) {
    compatibleWith(COMPATIBILITY_SOUNDCLOUD)

    execute {
        // ── Plan override ───────────────────────────────────────
        UserConsumerPlanConstructorFingerprint.methodOrNull?.addInstructions(
            0,
            """
                const-string p1, "high_tier"
                const-string p5, "go-plus"
                const-string p6, "SoundCloud Go"
            """,
        )

        GetDowngradeTierFingerprint.methodOrNull?.addInstructions(
            0,
            """
                sget-object v0, Lcom/soundcloud/android/configuration/plans/Tier;->HIGH:Lcom/soundcloud/android/configuration/plans/Tier;
                return-object v0
            """,
        )

        MapToPlanFingerprint.methodOrNull?.addInstructions(
            0,
            """
                sget-object v0, Lcom/soundcloud/android/upsell/UpsellType${'$'}None;->INSTANCE:Lcom/soundcloud/android/upsell/UpsellType${'$'}None;
                return-object v0
            """,
        )

        // ── Tier/plan state ─────────────────────────────────────
        GetCurrentTierFingerprint.methodOrNull?.addInstructions(
            0,
            """
                sget-object v0, Lcom/soundcloud/android/configuration/plans/Tier;->HIGH:Lcom/soundcloud/android/configuration/plans/Tier;
                return-object v0
            """,
        )

        GetCurrentConsumerPlanFingerprint.methodOrNull?.addInstructions(
            0,
            """
                sget-object v0, Lcom/soundcloud/android/configuration/plans/ConsumerPlan;->GO_PLUS:Lcom/soundcloud/android/configuration/plans/ConsumerPlan;
                return-object v0
            """,
        )

        // ── Block all tier change detection paths ───────────────
        // Path 1: direct tier comparison
        TierChangeDetectorBFingerprint.methodOrNull?.addInstructions(0, "return-void")

        // Path 2: reactive plan update emitter (EventBus source)
        TierChangeDetectorAFingerprint.methodOrNull?.addInstructions(0, "return-void")

        // Path 3: route offboarding to bottom sheet (not fullscreen GoOffboardingActivity)
        // so TierChangeDetector.a() no-op actually blocks it
        PlanTransitionsExperimentsFingerprint.methodOrNull?.addInstructions(
            0, "const/4 v0, 0x1\nreturn v0",
        )

        // ── Ad blocking ─────────────────────────────────────────
        GetShouldRequestAdsFingerprint.methodOrNull?.addInstructions(
            0, "const/4 v0, 0x0\nreturn v0",
        )

        IsMonetizableAdGeoFingerprint.methodOrNull?.addInstructions(
            0, "const/4 v0, 0x0\nreturn v0",
        )

        AdPlacementConfigCtorFingerprint.matchAllOrNull()?.forEach { match ->
            val offset = if (match.method.parameterTypes.first() == "I") 1 else 0
            match.method.addInstructions(
                0,
                listOf(1, 2, 3).joinToString("\n") { "const/4 p${offset + it}, 0x0" },
            )
        }
    }
}
