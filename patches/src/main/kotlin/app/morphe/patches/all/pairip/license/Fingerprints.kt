package app.morphe.patches.all.pairip.license

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.fieldAccess
import com.android.tools.smali.dexlib2.Opcode

object ProcessLicenseResponseFingerprint : Fingerprint(
    definingClass = "Lcom/pairip/licensecheck/LicenseClient;",
    name = "processResponse",
)

object RepeatedCheckFingerprint : Fingerprint(
    filters = listOf(
        fieldAccess(
            opcode = Opcode.SGET_BOOLEAN,
            name = "repeatedCheckEnabled"
        )
    )
)

object ValidateLicenseResponseFingerprint : Fingerprint(
    custom = { method, classDef ->
        (classDef.type == "Lcom/pairip/licensecheck/ResponseValidator;" || classDef.type == "Lcom/pairip/licensecheck/LicenseResponseHelper;") &&
                method.name == "validateResponse"
    }
)
