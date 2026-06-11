package app.morphe.patches.medisafe.premium

import app.morphe.patcher.Fingerprint

/**
 * Matches `FlowIapHelper.isPaidBundle(Context)Z` — the single method
 * that all premium checks in Medisafe funnel through.
 */
object IsPaidBundleFingerprint : Fingerprint(
    definingClass = "Lcom/medisafe/android/implementations/FlowIapHelper;",
    name = "isPaidBundle",
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;"),
)

/**
 * Matches `PurchaseRestoreWorker.doWork()` — the background worker
 * that checks Google Play for real subscriptions every 24 hours.
 */
object PurchaseRestoreWorkerDoWorkFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/medisafe/android/base/service/PurchaseRestoreWorker;" &&
                method.name == "doWork"
    }
)

/**
 * Matches `SyncResponseHandler.updatePurchaseBundle()` — the server sync
 * handler that overwrites the local purchase bundle with server data.
 * Making this a no-op prevents the server from resetting premium to "base".
 */
object UpdatePurchaseBundleFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/medisafe/android/base/client/net/response/SyncResponseHandler;" &&
                method.name == "updatePurchaseBundle"
    }
)

/**
 * Matches `IFlowIapServiceImpl.handlePurchaseBundleFeatures()` — called
 * when the server returns a new bundle, resetting theme colors and ringtones
 * to free-tier defaults. Making this a no-op preserves user customizations.
 */
object HandlePurchaseBundleFeaturesFingerprint : Fingerprint(
    custom = { method, classDef ->
        classDef.type == "Lcom/medisafe/android/implementations/IFlowIapServiceImpl;" &&
                method.name == "handlePurchaseBundleFeatures"
    }
)
