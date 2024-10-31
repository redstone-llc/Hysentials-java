package llc.redstone.hysentials.renderer.text

import cc.polyfrost.oneconfig.libs.caffeine.cache.Caffeine
import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.handlers.imageicons.ImageIcon
import llc.redstone.hysentials.utils.replace
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer

// Assess how long this takes to run
// TODO: Test to see if it would cause issues with large amounts of text

data class Format(
    val type: FormatType,
    val range: IntRange,
    val value: Any //(ImageIcon, String, etc.)
)

data class Number(
    val hex: String,
    val number: String
)

enum class FormatType {
    IMAGE_ICON,
    STRING,
    HEX,
    NUMBER
}

fun main() {
    val text = "<#E8F7C8><#E8F7C8>: gift 1nfuse +"
    val formats = FancyFormatting2.getFormats(text, true)
    //line across the entire terminal with -
    println("-".repeat(100))
    println("Output:")
    formats.forEach {
        println(it)
    }
}

class FancyFormatting2 {
    companion object {
        var chars: Map<Char, Int> = HashMap()
        var iconPattern = Regex("(.+|)(:[a-zA-Z0-9_]+:)(.+|)")
        var hexPattern = Regex("(.+|)(<#[0-9A-Fa-f]{6}>)(.+|)")
        var numPattern = Regex("(.+|)(<[0-9A-Fa-f]{6}:[0-9]+>)(.+|)")

        var cache = Caffeine.newBuilder().maximumSize(100).build<String, List<Format>>()

        fun getFormats(text: String, testing: Boolean = true): List<Format> {
            if (text.isEmpty()) return emptyList()

            if (cache.getIfPresent(text) != null) {
                return cache.getIfPresent(text)!!
            }

            var text = text
            val formats = mutableListOf<Format>()

            for (it in iconPattern.findAll(text)) {
                val before = it.groupValues[1]
                val after = it.groupValues[3]

                if (before.isNotEmpty()) {
                    var beforeFormats = getFormats(before)
                    formats.addAll(beforeFormats)
                }

                val icon = it.groupValues[2].substring(1, it.groupValues[2].length - 1)
                val iconData = (ImageIcon.getIcon(icon)) ?: continue
                formats.add(Format(FormatType.IMAGE_ICON, it.groups[2]!!.range, iconData))

                if (after.isNotEmpty()) {
                    var afterFormats = getFormats(after)
                    formats.addAll(afterFormats)
                }
                text = text.replaceRange(it.range, "")
            }

            for (it in hexPattern.findAll(text)) {
                var before = it.groupValues[1]
                var after = it.groupValues[3]

                if (before.isNotEmpty()) {
                    var beforeFormats = getFormats(before)
                    formats.addAll(beforeFormats)
                }

                val hex = it.groupValues[2].substring(2, it.groupValues[2].length - 1)
                formats.add(Format(FormatType.HEX, it.groups[2]!!.range, hex.toInt(16)))

                if (after.isNotEmpty()) {
                    var afterFormats = getFormats(after)
                    formats.addAll(afterFormats)
                }
                text = text.replaceRange(it.range, "")
            }

            for (it in numPattern.findAll(text)) {
                var before = it.groupValues[1]
                var after = it.groupValues[3]

                if (before.isNotEmpty()) {
                    var beforeFormats = getFormats(before)
                    formats.addAll(beforeFormats)
                }

                val num = it.groupValues[2].substring(1, it.groupValues[2].length - 1)
                formats.add(Format(FormatType.NUMBER, it.groups[2]!!.range, Number(num.substringBefore(":"), num.substringAfter(":"))))

                if (after.isNotEmpty()) {
                    var afterFormats = getFormats(after)
                    formats.addAll(afterFormats)
                }
                text = text.replaceRange(it.range, "")
            }

            if (text.isNotEmpty()) {
                formats.add(Format(FormatType.STRING, 0..text.length, text))
            }

            cache.put(text, formats)
            return formats
        }

        fun replace(text: String, formats: List<Format>): String {
            var doneText = text
            for (format in formats) {
                doneText = doneText.replaceRange(format.range, "")
            }
            return doneText

        }

        fun getLastFormat(text: String): String {
            val formats = getFormats(text, false)
            if (formats.isEmpty()) return text
            var lastFormat: String = ""
            for (format in formats) {
                if (format.type == FormatType.STRING) {
                    if (!(format.value as String).contains("§")) continue
                    lastFormat = FontRenderer.getFormatFromString(format.value as String)
                }

                if (format.type == FormatType.HEX) {
                    lastFormat = "<#" + (format.value as Int).toString(16) + ">"
                }
            }
            return lastFormat
        }

        fun replaceString(text: String, hidden: Boolean): String {
            val allActiveReplacements = Hysentials.INSTANCE.config?.replaceConfig?.allActiveReplacements ?: return text
            var doneText = text
            for (key in allActiveReplacements.keys) {
                var key = key
                val finalText: String = text.replace("§r", "")
                var value = allActiveReplacements[key] ?: continue
                if (value.isEmpty() || key.isEmpty()) {
                    continue
                }
                value = value.replace("&", "§")
                key = key.replace("&", "§")
                if (Hysentials.INSTANCE.config.replaceConfig.isRegexEnabled) {
                    doneText = finalText.replace(key.toRegex(), value)
                } else {
                    doneText = finalText.replace(key, value)
                }
            }

            if (hidden) {
                var newText = ""
                getFormats(doneText, false).forEach {
                    if (it.type == FormatType.STRING) {
                        newText += it.value as String
                    }
                    if (it.type == FormatType.IMAGE_ICON) {
                        newText += replaceStringWithPlaceholder(":" + (it.value as ImageIcon).name + ":")
                    }
                }
                doneText = newText
            }

            return doneText
        }
    }
}