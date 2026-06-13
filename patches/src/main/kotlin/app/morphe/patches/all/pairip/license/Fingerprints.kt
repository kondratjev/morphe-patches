package app.morphe.patches.all.pairip.license

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess
import com.android.tools.smali.dexlib2.Opcode

/** Matches `LicenseClient.processResponse()` — processes the license validation server response. */
object ProcessLicenseResponseFingerprint : Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseClient;",
    name = "processResponse",
)

/** Matches any method reading the `repeatedCheckEnabled` static boolean field. */
object RepeatedCheckFingerprint : Fingerprint(
    filters = listOf(
        fieldAccess(
            opcode = Opcode.SGET_BOOLEAN,
            name = "repeatedCheckEnabled"
        )
    )
)

/** Matches `ResponseValidator.validateResponse()` / `LicenseResponseHelper.validateResponse()` — validates the license server response. */
object ValidateLicenseResponseFingerprint : Fingerprint(
    custom = { method, classDef ->
        (classDef.type == "Lcom/pairip/licensecheck/ResponseValidator;" || classDef.type == "Lcom/pairip/licensecheck/LicenseResponseHelper;") &&
                method.name == "validateResponse"
    }
)
