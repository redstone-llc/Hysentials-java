package llc.redstone.hysentials.renderer.text

import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.handlers.imageicons.ImageIcon
import llc.redstone.hysentials.utils.replace
import llc.redstone.hysentials.utils.substringBefore
import org.apache.commons.lang3.StringUtils

// Assess how long this takes to run
// TODO: Test to see if it would cause issues with large amounts of text


var chars : Map<Char, Int> = HashMap()
var currentText: String? = null
    private set

@Override
fun setCurrentText(s: String?): String? {
    currentText = s
    return currentText
}

fun replaceString(text: String): String {
    val allActiveReplacements = Hysentials.INSTANCE.config?.replaceConfig?.allActiveReplacements?: return text
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

    doneText = replaceStringWithPlaceholder(doneText)
    return doneText
}

fun replaceStringWithPlaceholder(text: String): String {
    if (chars.isEmpty()) return text
    var doneText = text
    Regex(":([a-zA-Z0-9_]+):").findAll(text).forEach {
        val icon = it.groupValues[1]
        if (icon.endsWith("?")) {
            doneText = doneText.replace(":$icon:", ":${icon.substringBeforeLast("?:")}:")
            return@forEach
        }
        val iconData = ImageIcon.getIcon(icon) ?: return@forEach
        doneText = doneText.replace(":$icon:", iconData.replacement)
    }
    return doneText
}