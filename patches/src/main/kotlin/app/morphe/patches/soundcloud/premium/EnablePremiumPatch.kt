package app.morphe.patches.soundcloud.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.soundcloud.shared.Constants.COMPATIBILITY_SOUNDCLOUD
import app.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable SoundCloud Go+",
    description = "Enables all premium features, hides upsell UI and ads.",
) {
    compatibleWith(COMPATIBILITY_SOUNDCLOUD)

    execute {
        // Override plan construction with Go+ tier.
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

        // Force current tier to HIGH and plan to Go+.
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

        // Block offboarding — lifecycle observer controls all transition UI.
        ConfigurationUpdatesLifecycleObserverFingerprint.methodOrNull?.returnEarly()

        // Disable ads.
        GetShouldRequestAdsFingerprint.methodOrNull?.returnEarly(false)

        IsMonetizableAdGeoFingerprint.methodOrNull?.returnEarly(false)

        AdPlacementConfigCtorFingerprint.matchAllOrNull()?.forEach { match ->
            val offset = if (match.method.parameterTypes.first() == "I") 1 else 0
            match.method.addInstructions(
                0,
                (1..3).joinToString("\n") { "const/4 p${offset + it}, 0x0" },
            )
        }
    }
}
