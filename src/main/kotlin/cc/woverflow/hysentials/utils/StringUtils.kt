package cc.woverflow.hysentials.utils

import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent
import org.apache.commons.lang3.StringUtils as ApacheStringUtils

fun CharSequence?.countMatches(subString: CharSequence): Int = ApacheStringUtils.countMatches(this, subString)

fun String?.stripControlCodes(): String = UTextComponent.stripFormatting(this ?: "")

fun CharSequence?.startsWithAny(vararg sequences: CharSequence?) = ApacheStringUtils.startsWithAny(this, *sequences)
fun CharSequence.startsWithAny(sequences: Iterable<CharSequence>): Boolean = sequences.any { contains(it) }
fun CharSequence?.containsAny(vararg sequences: CharSequence?): Boolean {
    if (this == null) return false
    return sequences.any { it != null && this.contains(it) }
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