package app.morphe.patches.all.misc.jsbundle

import app.morphe.patches.all.misc.hex.Replacement
import app.morphe.patches.all.misc.util.byteArrayOf
import app.morphe.patcher.patch.rawResourcePatch
import java.io.FileNotFoundException

const val JS_BUNDLE_PATH = "assets/index.android.bundle"

/**
 * Plaintext JS bundle patcher with Boyer-Moore byte replacement.
 * No magic-header check or SHA-1 footer rewrite (Hermes-specific).
 *
 * Usage:
 * ```
 * dependsOn(jsBundlePatch {
 *     "hex pattern" to "hex replacement"
 * })
 * ```
 */
fun jsBundlePatch(supplier: () -> Set<Pair<String, String>>) =
    rawResourcePatch {
        execute {
            val file = get(JS_BUNDLE_PATH, true)
            if (!file.exists())
                throw FileNotFoundException("JS bundle not found at: $JS_BUNDLE_PATH")

            var fileBytes = file.readBytes()

            // Apply byte replacements (Boyer-Moore via Replacement)
            supplier()
                .map { Replacement(byteArrayOf(it.first), byteArrayOf(it.second), JS_BUNDLE_PATH) }
                .forEach { it.replacePattern(fileBytes) }

            file.writeBytes(fileBytes)
        }
    }
