package app.morphe.patches.soundcloud.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patches.soundcloud.shared.Constants.COMPATIBILITY_SOUNDCLOUD
import app.morphe.util.returnEarly

@Suppress("unused")
val enablePremiumPatch = bytecodePatch(
    name = "Enable SoundCloud Go",
    description = "Enables SoundCloud Go premium features.",
) {
    compatibleWith(COMPATIBILITY_SOUNDCLOUD)

    execute {
        // Override plan construction with Go tier.
        UserConsumerPlanConstructorFingerprint.method.addInstructions(
            0,
            """
                const-string p1, "high_tier"
                const-string p5, "go-plus"
                const-string p6, "SoundCloud Go"
            """,
        )

        GetDowngradeTierFingerprint.method.addInstructions(
            0,
            """
                sget-object v0, Lcom/soundcloud/android/configuration/plans/Tier;->HIGH:Lcom/soundcloud/android/configuration/plans/Tier;
                return-object v0
            """,
        )

        MapToPlanFingerprint.method.addInstructions(
            0,
            """
                sget-object v0, Lcom/soundcloud/android/upsell/UpsellType${'$'}None;->INSTANCE:Lcom/soundcloud/android/upsell/UpsellType${'$'}None;
                return-object v0
            """,
        )

        // Force current tier to HIGH and plan to Go.
        GetCurrentTierFingerprint.method.addInstructions(
            0,
            """
                sget-object v0, Lcom/soundcloud/android/configuration/plans/Tier;->HIGH:Lcom/soundcloud/android/configuration/plans/Tier;
                return-object v0
            """,
        )

        GetCurrentConsumerPlanFingerprint.method.addInstructions(
            0,
            """
                sget-object v0, Lcom/soundcloud/android/configuration/plans/ConsumerPlan;->GO_PLUS:Lcom/soundcloud/android/configuration/plans/ConsumerPlan;
                return-object v0
            """,
        )

        // Block offboarding — lifecycle observer controls all transition UI.
        ConfigurationUpdatesLifecycleObserverFingerprint.method.returnEarly()

        // Disable ads.
        GetShouldRequestAdsFingerprint.method.returnEarly(false)

        IsMonetizableAdGeoFingerprint.method.returnEarly(false)

        // Disable ads — force `enabled` param to false in the data-class ctor.
        AdPlacementConfigCtorFingerprint.matchAllOrNull()?.forEach { match ->
            match.method.addInstructions(
                0,
                "const/4 p1, 0x0",
            )
        }
    }
}
