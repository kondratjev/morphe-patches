package app.morphe.patches.pillo.gms

import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.extensions.InstructionExtensions.getInstruction
import app.morphe.patcher.extensions.InstructionExtensions.replaceInstruction
import app.morphe.patcher.patch.PatchException
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.patch.resourcePatch
import app.morphe.patcher.string
import app.morphe.patches.pillo.shared.Constants.COMPATIBILITY_PILLO
import app.morphe.util.matchAllMethodIndicesForEach
import app.morphe.util.returnEarly
import com.android.tools.smali.dexlib2.Opcode
import com.android.tools.smali.dexlib2.builder.instruction.BuilderInstruction21c
import com.android.tools.smali.dexlib2.iface.instruction.FiveRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.OneRegisterInstruction
import com.android.tools.smali.dexlib2.iface.instruction.ReferenceInstruction
import com.android.tools.smali.dexlib2.iface.reference.MethodReference
import com.android.tools.smali.dexlib2.immutable.reference.ImmutableStringReference
import org.w3c.dom.Element
import org.w3c.dom.Node

private const val GMS_CORE_VENDOR_GROUP = "app.revanced"
private const val GMS_CORE_PACKAGE = "$GMS_CORE_VENDOR_GROUP.android.gms"
private const val ORIGINAL_PACKAGE = "xyz.rtrvr.pillo"
private const val ORIGINAL_CERTIFICATE_SHA1 = "4d93c347836a9c98821507853a0b85031e7af7b8"

private val gmsStringReplacements = buildMap {
    put("com.google", GMS_CORE_VENDOR_GROUP)
    put("com.google.android.gms", GMS_CORE_PACKAGE)

    // MicroG-RE service actions used by Google Sign-In and One Tap.
    put(
        "com.google.android.gms.auth.api.identity.service.signin.START",
        "$GMS_CORE_PACKAGE.auth.api.identity.service.signin.START",
    )
    put(
        "com.google.android.gms.auth.api.identity.service.credentialsaving.START",
        "$GMS_CORE_PACKAGE.auth.api.identity.service.credentialsaving.START",
    )
    put(
        "com.google.android.gms.auth.api.signin.service.START",
        "$GMS_CORE_PACKAGE.auth.api.signin.service.START",
    )
    put(
        "com.google.android.gms.auth.GOOGLE_SIGN_IN",
        "$GMS_CORE_PACKAGE.auth.GOOGLE_SIGN_IN",
    )
    put(
        "com.google.android.gms.signin.service.START",
        "$GMS_CORE_PACKAGE.signin.service.START",
    )

    // Firebase Cloud Messaging / GServices authorities.
    put("com.google.android.c2dm.permission.RECEIVE", "$GMS_CORE_VENDOR_GROUP.android.c2dm.permission.RECEIVE")
    put("com.google.android.c2dm.permission.SEND", "$GMS_CORE_VENDOR_GROUP.android.c2dm.permission.SEND")
    put("com.google.android.c2dm.intent.RECEIVE", "$GMS_CORE_VENDOR_GROUP.android.c2dm.intent.RECEIVE")
    put("com.google.android.c2dm.intent.REGISTER", "$GMS_CORE_VENDOR_GROUP.android.c2dm.intent.REGISTER")
    put("com.google.android.c2dm.intent.REGISTRATION", "$GMS_CORE_VENDOR_GROUP.android.c2dm.intent.REGISTRATION")
    put("com.google.android.c2dm.intent.UNREGISTER", "$GMS_CORE_VENDOR_GROUP.android.c2dm.intent.UNREGISTER")
    put("com.google.android.gcm.intent.SEND", "$GMS_CORE_VENDOR_GROUP.android.gcm.intent.SEND")
    put("com.google.android.gsf.action.GET_GLS", "$GMS_CORE_VENDOR_GROUP.android.gsf.action.GET_GLS")
    put("com.google.android.gsf.gservices", "$GMS_CORE_VENDOR_GROUP.android.gsf.gservices")
    put("content://com.google.android.gsf.gservices", "content://$GMS_CORE_VENDOR_GROUP.android.gsf.gservices")
    put(
        "content://com.google.android.gsf.gservices/prefix",
        "content://$GMS_CORE_VENDOR_GROUP.android.gsf.gservices/prefix",
    )
}

private fun Element.directChildrenNamed(vararg names: String): List<Element> {
    val accepted = names.toSet()
    return (0 until childNodes.length)
        .map { childNodes.item(it) }
        .filterIsInstance<Element>()
        .filter { it.nodeName in accepted }
}

private fun Element.setApplicationMetaData(name: String, value: String) {
    val metadata = directChildrenNamed("meta-data")
        .firstOrNull { it.getAttribute("android:name") == name }
        ?: ownerDocument.createElement("meta-data").also {
            it.setAttribute("android:name", name)
            appendChild(it)
        }
    metadata.setAttribute("android:value", value)
}

private fun replaceC2dmManifestAttributes(node: Node) {
    if (node is Element) {
        for (i in 0 until node.attributes.length) {
            val attribute = node.attributes.item(i)
            if (attribute.nodeValue.startsWith("com.google.android.c2dm")) {
                attribute.nodeValue = attribute.nodeValue.replace(
                    "com.google.android.c2dm",
                    "$GMS_CORE_VENDOR_GROUP.android.c2dm",
                )
            }
        }
    }

    for (i in 0 until node.childNodes.length) {
        replaceC2dmManifestAttributes(node.childNodes.item(i))
    }
}

private val gmsCoreSupportResourcePatch = resourcePatch(
    name = "MicroG support",
    description = "Routes Pillo Google services calls through MicroG-RE.",
) {
    compatibleWith(COMPATIBILITY_PILLO)

    execute {
        document("AndroidManifest.xml").use { document ->
            val manifest = document.documentElement
            val application = manifest.directChildrenNamed("application").single()

            // Keep Pillo's package unchanged. Firebase and Google OAuth are
            // registered for xyz.rtrvr.pillo; MicroG-RE spoofs this package
            // through application metadata instead.
            application.setApplicationMetaData(
                "$GMS_CORE_PACKAGE.SPOOFED_PACKAGE_NAME",
                ORIGINAL_PACKAGE,
            )
            application.setApplicationMetaData(
                "$GMS_CORE_PACKAGE.SPOOFED_PACKAGE_SIGNATURE",
                ORIGINAL_CERTIFICATE_SHA1,
            )

            // Used by optional MicroG-RE integration helpers.
            application.setApplicationMetaData(
                "app.revanced.MICROG_PACKAGE_NAME",
                GMS_CORE_PACKAGE,
            )

            replaceC2dmManifestAttributes(manifest)

            val queries = manifest.directChildrenNamed("queries").firstOrNull()
                ?: document.createElement("queries").also {
                    manifest.insertBefore(it, application)
                }
            if (queries.directChildrenNamed("package").none {
                    it.getAttribute("android:name") == GMS_CORE_PACKAGE
                }) {
                queries.appendChild(document.createElement("package").also {
                    it.setAttribute("android:name", GMS_CORE_PACKAGE)
                })
            }
        }
    }
}

@Suppress("unused")
val gmsCoreSupportPatch = bytecodePatch(
    name = "MicroG support",
    description = "Fixes Google sign-in and sync on patched builds: routes Google services through MicroG-RE and reports the original certificate to Firebase. Requires MicroG-RE with a Google account.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_PILLO)
    dependsOn(gmsCoreSupportResourcePatch)

    execute {
        // Firebase: report the original certificate SHA-1 (uppercase hex)
        // instead of the re-signed one, so Firebase Installations accepts us.
        // This is the authoritative source of the cert hash inside
        // FirebaseInstallationServiceClient — every caller (the X-Android-Cert
        // header, FID auth, etc.) gets the correct value.
        FirebaseFingerprintHashFingerprint.method.addInstructions(
            0,
            """
                const-string v0, "${ORIGINAL_CERTIFICATE_SHA1.uppercase()}"
                return-object v0
            """.trimIndent(),
        )

        // Belt-and-suspenders: overwrite the header value at the call site too,
        // in case some SDK version computes it inline.
        val openConnection = FirebaseOpenHttpConnectionFingerprint.method
        val xAndroidCertIndex = FirebaseOpenHttpConnectionFingerprint.stringMatches.first().index

        val instructionList = openConnection.implementation!!.instructions.toList()
        val addRequestPropertyInstr = instructionList
            .drop(xAndroidCertIndex)
            .firstOrNull { instr ->
                instr.opcode == Opcode.INVOKE_VIRTUAL &&
                    ((instr as? ReferenceInstruction)?.reference as? MethodReference)
                        ?.name == "addRequestProperty"
            }
            ?: throw PatchException(
                "Could not find addRequestProperty call after X-Android-Cert string.",
            )

        val valueRegister = (addRequestPropertyInstr as FiveRegisterInstruction).registerE
        val insertIndex = instructionList.indexOf(addRequestPropertyInstr)
        openConnection.addInstruction(
            insertIndex,
            "const-string v$valueRegister, \"${ORIGINAL_CERTIFICATE_SHA1.uppercase()}\"",
        )

        gmsStringReplacements.forEach { (from, to) ->
            string(from).matchAllMethodIndicesForEach(requireMatches = false) { index ->
                val register = getInstruction<OneRegisterInstruction>(index).registerA
                replaceInstruction(
                    index,
                    BuilderInstruction21c(
                        Opcode.CONST_STRING,
                        register,
                        ImmutableStringReference(to),
                    ),
                )
            }
        }

        // The bundled client otherwise rejects MicroG-RE before attempting
        // the service connection. One Tap then falls back to legacy Sign-In,
        // which MicroG-RE implements.
        GooglePlayServicesAvailableFingerprint.method.returnEarly(0)
    }
}
