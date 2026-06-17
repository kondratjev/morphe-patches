package app.morphe.patches.medisafe.premium

import app.morphe.patcher.Fingerprint

/**
 * Matches `FlowIapHelper.isPaidBundle(Context)Z` — single method
 * all premium checks in Medisafe funnel through.
 */
object IsPaidBundleFingerprint : Fingerprint(
    definingClass = "Lcom/medisafe/android/implementations/FlowIapHelper;",
    name = "isPaidBundle",
    returnType = "Z",
    parameters = listOf("Landroid/content/Context;"),
)

/**
 * Matches `PurchaseRestoreWorker.doWork()` — background worker
 * checking Google Play for subscriptions every 24 hours.
 */
object PurchaseRestoreWorkerDoWorkFingerprint : Fingerprint(
    definingClass = "Lcom/medisafe/android/base/service/PurchaseRestoreWorker;",
    name = "doWork",
)
