/*
 * This file is part of PolyUI
 * PolyUI - Fast and lightweight UI framework
 * Copyright (C) 2023-2024 Polyfrost and its contributors.
 *   <https://polyfrost.org> <https://github.com/Polyfrost/polui-jvm>
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 *     PolyUI is licensed under the terms of version 3 of the GNU Lesser
 * General Public License as published by the Free Software Foundation,
 * AND the simple request that you adequately accredit us if you use PolyUI.
 * See details here <https://github.com/Polyfrost/polyui-jvm/ACCREDITATION.md>.
 *     This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 * License.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.polyfrost.polyui.component.impl

import llc.redstone.hysentials.polyui.colorTextBounds
import org.jetbrains.annotations.ApiStatus
import org.polyfrost.polyui.PolyUI
import org.polyfrost.polyui.color.PolyColor
import org.polyfrost.polyui.component.Drawable
import org.polyfrost.polyui.event.Event
import org.polyfrost.polyui.input.Translator
import org.polyfrost.polyui.renderer.Renderer
import org.polyfrost.polyui.renderer.data.Font
import org.polyfrost.polyui.unit.Align
import org.polyfrost.polyui.unit.AlignDefault
import org.polyfrost.polyui.unit.Vec2
import org.polyfrost.polyui.utils.*
import kotlin.math.max

open class ColorText(text: Translator.Text, font: Font? = null, fontSize: Float = 12f, at: Vec2? = null, alignment: Align = AlignDefault, wrap: Float = 0f, visibleSize: Vec2? = null, focusable: Boolean = false, vararg children: Drawable?) :
    Drawable(children = children, at, alignment, visibleSize = visibleSize, focusable = focusable) {
    constructor(text: String, font: Font? = null, fontSize: Float = 12f, at: Vec2? = null, alignment: Align = AlignDefault, wrap: Float = 0f, visibleSize: Vec2? = null, focusable: Boolean = false, vararg children: Drawable?) :
            this(Translator.Text.Simple(text), font, fontSize, at, alignment, wrap, visibleSize, focusable, children = children)

    init {
        require(fontSize > 0f) { "Font size must be greater than 0" }
    }

    /**
     * @since 1.0.6
     */
    var strikethrough = false

    /**
     * @since 1.0.6
     */
    var underline = false

    var wrap: Float = wrap
        set(value) {
            if (field == value) return
            field = value
            if (initialized) updatecolorTextBounds()
        }

    // asm: initially it is a dummy object to save need for a field
    // it is immediately overwritten by setup()
    // this is public so it can be inlined
    @Deprecated("Internal object, use text instead", replaceWith = ReplaceWith("text"), level = DeprecationLevel.ERROR)
    @ApiStatus.Internal
    var _translated = text
        set(value) {
            if (field == value) return
            field = value
            if (initialized) updatecolorTextBounds()
        }

    @Suppress("deprecation_error")
    var text: String
        inline get() = _translated.string
        set(value) {
            if (_translated.string == value) return
            if (hasListenersFor(Event.Change.Text::class.java)) {
                val ev = Event.Change.Text(value)
                accept(ev)
                if (ev.cancelled) return
            }
            _translated.string = value
            if (initialized) updatecolorTextBounds()
        }

    /**
     * A list of the lines of this text, and their corresponding width.
     */
    protected val lines = LinkedList<MutablePair<String, Float>>()

    private var _font: Font? = font
        set(value) {
            if (field == value) return
            field = value
            if (initialized) updatecolorTextBounds()
        }

    var font: Font
        get() = _font ?: throw UninitializedPropertyAccessException("font")
        set(value) {
            if (_font == value) return
            _font = value
            spacing = (font.lineSpacing - 1f) * fontSize
        }

    /**
     * The weight of the [font].
     *
     * Setting of this value only works if this font is a member of a family.
     * @since 1.0.7
     */
    var fontWeight: Font.Weight
        inline get() = font.weight
        set(value) {
            val fam = font.family
            if (fam == null) {

                return
            }
            font = fam.get(value, italic)
        }

    /**
     * `true` if [font] is italic
     *
     * Setting of this value only works if this font is a member of a family.
     * @since 1.0.7
     */
    var italic: Boolean
        inline get() = font.italic
        set(value) {
            val fam = font.family
            if (fam == null) {
                return
            }
            font = fam.get(fontWeight, value)
        }

    var fontSize = fontSize
        set(value) {
            if (field == value) return
            field = value
            if (_font != null) spacing = (font.lineSpacing - 1f) * value
            if (initialized) updatecolorTextBounds()
        }

    protected var spacing = 0f
        private set

    val RGBA_COLOR = mapOf(
        '0' to PolyColor(0,0,0),
        '1' to PolyColor(0,0,170),
        '2' to PolyColor(0,170,0),
        '3' to PolyColor(0,170,170),
        '4' to PolyColor(170,0,0),
        '5' to PolyColor(170,0,170),
        '6' to PolyColor(255,170,0),
        '7' to PolyColor(170,170,170),
        '8' to PolyColor(85,85,85),
        '9' to PolyColor(85,85,255),
        'a' to PolyColor(85,255,85),
        'b' to PolyColor(85,255,255),
        'c' to PolyColor(255,85,85),
        'd' to PolyColor(255,85,255),
        'e' to PolyColor(255,255,85),
        'f' to PolyColor(255,255,255)
    )


    override fun render() {
        var y = this.y
        var x = this.x
        val strikethrough = strikethrough
        val underline = underline
        lines.fastEach { (it, width) ->
            val splitMessageSpace = it.split(' ').toMutableList()
            for ((index, s) in splitMessageSpace.withIndex()) {
                if (!s.startsWith("§")) splitMessageSpace[index] = "§r$s"
            }
            val splitMessage = splitMessageSpace.joinToString(" ").split(Regex("[§\\n]"));
            var lastColorCode = PolyColor(255, 255, 255)
            for (s in splitMessage) {
                if (s.isEmpty()) continue
                val colorCode: PolyColor? = RGBA_COLOR[s[0]]
                val currentMessage = s.substring(1)
                if (colorCode != null) {
                    lastColorCode = colorCode
                }
                val color = lastColorCode
                renderer.text(font, x, y, currentMessage, color, fontSize)
                x += renderer.colorTextBounds(font, currentMessage, fontSize).x
            }
            if (strikethrough) {
                val hf = y + fontSize / 2f
                renderer.line(this.x, hf, x + width, hf, color, 1f)
            }
            if (underline) {
                val ff = y + fontSize - spacing + 1f
                renderer.line(this.x, ff, x + width, ff, color, 1f)
            }
            y += fontSize + spacing
        }
    }

    override fun rescale(scaleX: Float, scaleY: Float, position: Boolean) {
        super.rescale(scaleX, scaleY, position)
        val scale = cl1(scaleX, scaleY)
        fontSize *= scale
        spacing *= scale
    }

    @Suppress("deprecation_error")
    override fun setup(polyUI: PolyUI): Boolean {
        if (initialized) return false
        palette = polyUI.colors.text.primary
        if (_translated !is Translator.Text.Dont) {
            _translated = if (_translated is Translator.Text.Formatted) {
                polyUI.translator.translate(_translated.string, *(_translated as Translator.Text.Formatted).args)
            } else {
                polyUI.translator.translate(_translated.string)
            }
            // asm: in translation files \\n is used for new line for some reason
            text = text.replace("\\n", "\n")
        }
        if (_font == null) _font = polyUI.fonts.regular
        updatecolorTextBounds(polyUI.renderer)
        super.setup(polyUI)
        return true
    }

    open fun updatecolorTextBounds(renderer: Renderer = this.renderer) {
        val wrap = if (!hasVisibleSize) wrap else visibleSize.x
        lines.clear()
        text.splitTo('\n', dest = lines)
        if (lines.isEmpty() || (lines.size == 1 && lines[0].first.isEmpty())) {
            size.x = 1f
            size.y = fontSize
            return
        }
        if (wrap == 0f) {
            var mx = 0f
            var ty = -spacing
            lines.fastEach {
                val bounds = renderer.colorTextBounds(font, it.first, fontSize)
                it.second = bounds.x
                mx = max(mx, bounds.x)
                ty += bounds.y + spacing
            }
            size.x = mx
            size.y = ty
            if (visibleSize.y == 0f) visibleSize.y = ty
            return
        }
        val cp = lines.copy()
        lines.clear()
        cp.fastEach { (it, _) ->
            it.wrap(wrap, renderer, font, fontSize, lines)
        }
        val new = lines.size * (fontSize + spacing) - spacing
        if (lines.size > 1 && visibleSize.y != 0f && new > visibleSize.y) {
            // asm: text is larger than its box, cut off the last lines, but a minimum of 1 line
            lines.cut(0, max(0, (visibleSize.y / (fontSize + spacing)).toInt() - 1))
            size.y = visibleSize.y
        } else size.y = new
        size.x = wrap
        // asm: wrap was specified, but no height so just set it
        if (visibleSize.y == 0f) {
            visibleSize.y = size.y
        }
    }

    override fun calculateSize(): Vec2 {
        updatecolorTextBounds(renderer)
        return size
    }

    override fun debugString() =
        """
lines: ${lines.size}
underline=$underline;  strike=$strikethrough;  italic=$italic
font: ${font.resourcePath.substringAfterLast('/')}; size: $fontSize;  weight: $fontWeight
        """
}