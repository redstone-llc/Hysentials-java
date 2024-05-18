package llc.redstone.hysentials.polyui.components

import llc.redstone.hysentials.polyui.Fonts.minecraftReg
import llc.redstone.hysentials.polyui.colorTextBounds
import org.polyfrost.polyui.PolyUI
import org.polyfrost.polyui.component.Drawable
import org.polyfrost.polyui.input.Translator
import org.polyfrost.polyui.renderer.Renderer
import org.polyfrost.polyui.renderer.data.Font
import org.polyfrost.polyui.unit.Align
import org.polyfrost.polyui.unit.AlignDefault
import org.polyfrost.polyui.unit.Vec2
import org.polyfrost.polyui.utils.*

class Calculate(
    var text: Translator.Text, var lines: LinkedList<MutablePair<String, Float>>, var font: Font? = null, var fontSize: Float = 12f, at: Vec2? = null, alignment: Align = AlignDefault, wrap: Float = 0f, visibleSize: Vec2? = null, focusable: Boolean = false, vararg children: Drawable?
): Drawable(children = children, at, alignment, visibleSize, focusable = focusable) {
    constructor(text: String, lines: LinkedList<MutablePair<String, Float>>, font: Font? = null, fontSize: Float = 12f, at: Vec2? = null, alignment: Align = AlignDefault, wrap: Float = 0f, visibleSize: Vec2? = null, focusable: Boolean = false, vararg children: Drawable?) :
            this(Translator.Text.Simple(text), lines, font, fontSize, at, alignment, wrap, visibleSize, focusable, children = children)

    init {
        require(fontSize > 0f) { "Font size must be greater than 0" }
    }

    override fun render() {
    }

    override fun setup(polyUI: PolyUI): Boolean {
        super.setup(polyUI)
        lines.clear()
        text.string.wrap(
            256f, renderer, font ?: minecraftReg, fontSize, lines
        )
        return true
    }
}

/**
 * Wrap the given text to the given width, inserting them into [lines], or a new list is created if it is null. It also returned.
 */
fun String.wrap(
    maxWidth: Float,
    renderer: Renderer,
    font: Font,
    fontSize: Float,
    lines: LinkedList<MutablePair<String, Float>>?,
): LinkedList<MutablePair<String, Float>> {
    val ls = lines ?: LinkedList()
    if (maxWidth == 0f) {
        ls.add(this with renderer.colorTextBounds(font, this, fontSize).x)
        return ls
    }
    val words = split(" ")
    if (words.isEmpty()) {
        ls.clear()
        return ls
    }
    var currentLine = StringBuilder()

    words.forEach { word ->
        val wordLength = renderer.colorTextBounds(font, word, fontSize).x

        if (wordLength > maxWidth) {
            // ah. word is longer than the maximum wrap width
            if (currentLine.isNotEmpty()) {
                // Finish current line and start a new one with the long word
                val out = currentLine.toString()
                ls.add(out with renderer.colorTextBounds(font, out, fontSize).x)
                currentLine.clear()
            }

            // add the long word to the lines, splitting it up into smaller chunks if needed
            var remainingWord = word
            while (remainingWord.isNotEmpty()) {
                val chunk = remainingWord.substringToWidth(renderer, font, fontSize, maxWidth)
                ls.add(chunk.first with renderer.colorTextBounds(font, chunk.first, fontSize).x)
                remainingWord = chunk.second
            }
        } else if (currentLine.isEmpty()) {
            currentLine.append(word)
        } else if (renderer.colorTextBounds(font, currentLine.toString(), fontSize).x + wordLength <= maxWidth) {
            // ok!
            currentLine.append(' ').append(word)
        } else {
            // asm: word doesn't fit in current line, wrap it to the next line
            val out = currentLine.append(' ').toString()
            ls.add(out with renderer.colorTextBounds(font, out, fontSize).x)
            currentLine = currentLine.clear().append(word)
        }
    }

    // Add the last line
    if (currentLine.isNotEmpty()) {
        val out = currentLine.toString()
        ls.add(out with renderer.colorTextBounds(font, out, fontSize).x)
    }

    return ls
}

fun String.substringToWidth(
    renderer: Renderer,
    font: Font,
    fontSize: Float,
    width: Float,
    debug: Boolean = false,
): Pair<String, String> {
    if (debug && renderer.colorTextBounds(
            font,
            "W",
            fontSize,
        ).x > width
    ) { // this is enabled only on debug mode for performance in prod
        throw RuntimeException("Text box maximum width is too small for the given font size! (string: $this, font: ${font.resourcePath}, fontSize: $fontSize, width: $width)")
    }
    if (renderer.colorTextBounds(font, this, fontSize).x <= width) {
        return this to ""
    }

    var left = 0
    var right = length - 1
    var result = ""
    while (left <= right) {
        val mid = (left + right) / 2
        val substring = substring(0, mid + 1)
        if (renderer.colorTextBounds(font, substring, fontSize).x <= width) {
            result = substring
            left = mid + 1
        } else {
            right = mid - 1
        }
    }
    return result to this.substring(result.length)
}