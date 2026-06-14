package app.morphe.patches.all.misc.hex

import app.morphe.patches.all.misc.util.byteArrayOf
import app.morphe.patcher.patch.PatchException
import kotlin.math.max

/**
 * Represents a pattern to search for and its replacement pattern in a file.
 *
 * @property bytes The bytes to search for.
 * @property replacementBytesPadded The bytes to replace [bytes] with, padded to match length.
 * @property targetFilePath The path to the file to make the changes in relative to the APK root.
 */
class Replacement(
    private val bytes: ByteArray,
    replacementBytes: ByteArray,
    internal val targetFilePath: String,
) {
    val replacementBytesPadded = replacementBytes + ByteArray(bytes.size - replacementBytes.size)

    @Deprecated("Use the constructor with ByteArray parameters instead.")
    constructor(
        pattern: String,
        replacementPattern: String,
        targetFilePath: String,
    ) : this(
        byteArrayOf(pattern),
        byteArrayOf(replacementPattern),
        targetFilePath,
    )

    /**
     * Replaces the [bytes] with the [replacementBytesPadded] in the [targetFileBytes].
     * Throws [PatchException] if pattern not found.
     */
    internal fun replacePattern(targetFileBytes: ByteArray) {
        val startIndex = indexOfPatternIn(targetFileBytes)
        if (startIndex == -1) {
            throw PatchException(
                "Pattern not found in target file: " +
                    bytes.joinToString(" ") { "%02x".format(it) },
            )
        }
        replacementBytesPadded.copyInto(targetFileBytes, startIndex)
    }

    /**
     * Returns the index of the first occurrence of [bytes] in the haystack
     * using the Boyer-Moore algorithm.
     */
    private fun indexOfPatternIn(haystack: ByteArray): Int {
        val needle = bytes
        val right = IntArray(256) { -1 }
        for (i in 0 until needle.size) right[needle[i].toInt().and(0xFF)] = i

        var skip: Int
        for (i in 0..haystack.size - needle.size) {
            skip = 0
            for (j in needle.size - 1 downTo 0) {
                if (needle[j] != haystack[i + j]) {
                    skip = max(1, j - right[haystack[i + j].toInt().and(0xFF)])
                    break
                }
            }
            if (skip == 0) return i
        }
        return -1
    }
}
