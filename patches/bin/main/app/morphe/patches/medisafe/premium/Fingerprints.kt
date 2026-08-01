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
