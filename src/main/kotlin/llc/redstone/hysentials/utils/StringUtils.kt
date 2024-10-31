package llc.redstone.hysentials.utils

import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent
import org.apache.commons.lang3.StringUtils
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import org.apache.commons.lang3.StringUtils as ApacheStringUtils

fun CharSequence?.countMatches(subString: CharSequence): Int = ApacheStringUtils.countMatches(this, subString)

fun String?.stripControlCodes(): String = UTextComponent.stripFormatting(this ?: "")

fun CharSequence?.startsWithAny(vararg sequences: CharSequence?) = ApacheStringUtils.startsWithAny(this, *sequences)
fun CharSequence.startsWithAny(sequences: Iterable<CharSequence>): Boolean = sequences.any { contains(it) }
fun CharSequence?.containsAny(vararg sequences: CharSequence?): Boolean {
    if (this == null) return false
    return sequences.any { it != null && this.contains(it) }
}

fun String.replace(key: String, replacement: String): String = StringUtils.replace(this, key, replacement)

//shift a list of strings to the right and return the String
fun List<String>.shiftRight(): String {
    val last = this.last()
    val list = this.dropLast(1)
    return last + list.joinToString("")
}


fun String.toDashedUUID(): String {
    if (this.length != 32) return this
    return buildString {
        append(this@toDashedUUID)
        insert(20, "-")
        insert(16, "-")
        insert(12, "-")
        insert(8, "-")
    }
}
fun String.substringBefore(delimiter: String): String {
    return this.substringBefore(delimiter, this)
}

fun String.substringAfter(delimiter: String): String {
    return this.substringAfter(delimiter, this)
}



fun String.toTitleCase(): String = this.lowercase().replaceFirstChar { c -> c.titlecase() }
fun String.splitToWords(): String = this.split('_', ' ').joinToString(" ") { it.toTitleCase() }
fun String.isInteger(): Boolean = this.toIntOrNull() != null
fun String.formatCapitalize(): String = this.replace("_", " ").split(" ").joinToString(" ") { it.toTitleCase() }

fun DiffMatchPatch.Patch.getText(): String {
    val var4 = this.diffs.iterator()
    val text = StringBuilder()
    while (var4.hasNext()) {
        val aDiff = var4.next()
        when (aDiff.operation) {
            DiffMatchPatch.Operation.INSERT -> text.append('+')
            DiffMatchPatch.Operation.DELETE -> text.append('-')
            DiffMatchPatch.Operation.EQUAL -> text.append(' ')
        }
        try {
            text.append(aDiff.text.replace('+', ' ')).append("\n")
        } catch (var7: UnsupportedEncodingException) {
            throw Error("This system does not support UTF-8.", var7)
        }
    }
    return text.toString()
}