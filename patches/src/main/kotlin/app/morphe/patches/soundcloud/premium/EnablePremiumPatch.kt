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
        // 1. FlagFeature.i() — single evaluation point for ~80 feature flags
        //    Always return true to enable all features
        FlagFeatureEvaluationFingerprint.methodOrNull?.addInstructions(
            0, "const/4 v0, 0x1\nreturn v0",
        )

        // 2. UserConsumerPlan constructor — force go-plus tier
        UserConsumerPlanConstructorFingerprint.methodOrNull?.addInstructions(
            0,
            """
                const-string p1, "high_tier"
                const-string p5, "go-plus"
                const-string p6, "SoundCloud Go"
            """,
        )

        // 3. "pending_plan_downgrade" → Tier.HIGH (prevent offboarding screen)
        GetDowngradeTierFingerprint.methodOrNull?.addInstructions(
            0,
            """
                sget-object v0, Lcom/soundcloud/android/configuration/plans/Tier;->HIGH:Lcom/soundcloud/android/configuration/plans/Tier;
                return-object v0
            """,
        )

        // 4. mapToPlan → UpsellType.None (hide all upgrade UI)
        MapToPlanFingerprint.methodOrNull?.addInstructions(
            0,
            """
                sget-object v0, Lcom/soundcloud/android/upsell/UpsellType${'$'}None;->INSTANCE:Lcom/soundcloud/android/upsell/UpsellType${'$'}None;
                return-object v0
            """,
        )

        // 5. AdPlacementConfiguration constructors — zero out ads
        AdPlacementConfigCtorFingerprint.matchAllOrNull()?.forEach { match ->
            val offset = if (match.method.parameterTypes.first() == "I") 1 else 0
            match.method.addInstructions(
                0,
                listOf(1, 2, 3).joinToString("\n") { "const/4 p${offset + it}, 0x0" },
            )
        }
    }
}
