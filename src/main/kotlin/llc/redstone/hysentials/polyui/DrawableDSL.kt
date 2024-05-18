package llc.redstone.hysentials.polyui/*
 * This file is part of PolyUI
 * PolyUI - Fast and lightweight UI framework
 * Copyright (C) 2024 Polyfrost and its contributors.
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

import llc.redstone.hysentials.polyui.components.Box
import llc.redstone.hysentials.polyui.components.Calculate
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.nanovg.NanoVG
import org.polyfrost.polyui.PolyUI
import org.polyfrost.polyui.color.Colors
import org.polyfrost.polyui.color.DarkTheme
import org.polyfrost.polyui.color.PolyColor
import org.polyfrost.polyui.component.Drawable
import org.polyfrost.polyui.component.impl.*
import org.polyfrost.polyui.event.InputManager
import org.polyfrost.polyui.input.Translator
import org.polyfrost.polyui.property.Settings
import org.polyfrost.polyui.renderer.Renderer
import org.polyfrost.polyui.renderer.data.Font
import org.polyfrost.polyui.renderer.data.PolyImage
import org.polyfrost.polyui.unit.Align
import org.polyfrost.polyui.unit.AlignDefault
import org.polyfrost.polyui.unit.Vec2
import org.polyfrost.polyui.utils.LinkedList
import org.polyfrost.polyui.utils.MutablePair
import org.polyfrost.polyui.utils.image

open class DrawableDSL private constructor(val _this: Drawable) {
    operator fun <S : Drawable> S.invoke(init: (DrawableDSL.(S) -> Unit)? = null): S {
        val s = this.apply { init?.invoke(DrawableDSL(this), this) }
        _this.addChild(s)
        return s
    }

    fun <S : Drawable> S.use(init: (DrawableDSL.(S) -> Unit)? = null): S {
        val s = this.apply { init?.invoke(DrawableDSL(this), this) }
        _this.addChild(s)
        return s
    }

    fun <S : Drawable> S.add(init: (DrawableDSL.(S) -> Unit)? = null): S {
        val s = this.apply { init?.invoke(DrawableDSL(this), this) }
        _this.addChild(s)
        return s
    }

    fun block(size: Vec2? = null, alignment: Align = AlignDefault, init: (DrawableDSL.(Block) -> Unit)? = null): Block {
        val o = Block(size = size, alignment = alignment).apply { init?.invoke(DrawableDSL(this), this) }
        _this.addChild(o)
        return o
    }

    fun block(size: Vec2? = null, at: Vec2? = null, alignment: Align = AlignDefault, init: (DrawableDSL.(Block) -> Unit)? = null): Block {
        val o = Block(size = size, alignment = alignment, at = at).apply { init?.invoke(DrawableDSL(this), this) }
        _this.addChild(o)
        return o
    }

    fun box(size: Vec2? = null, alignment: Align = AlignDefault, init: (DrawableDSL.(Box) -> Unit)? = null): Box {
        val o = Box(size = size, alignment = alignment).apply { init?.invoke(DrawableDSL(this), this) }
        _this.addChild(o)
        return o
    }

    fun image(image: PolyImage, alignment: Align = AlignDefault, init: (Image.() -> Unit)? = null): Image {
        val o = Image(image = image, alignment = alignment).apply { init?.invoke(this) }
        _this.addChild(o)
        return o
    }

    fun image(image: PolyImage, alignment: Align = AlignDefault, at: Vec2?, init: (Image.() -> Unit)? = null): Image {
        val o = Image(image = image, alignment = alignment, at = at).apply { init?.invoke(this) }
        _this.addChild(o)
        return o
    }

    fun image(image: String, alignment: Align = AlignDefault, init: (Image.() -> Unit)? = null) = image(image.image(), alignment, init)

    fun text(text: String, alignment: Align = AlignDefault, init: (Text.() -> Unit)? = null): Text {
        val o = Text(text = text, alignment = alignment).apply { init?.invoke(this) }
        _this.addChild(o)
        return o
    }

    fun colorText(text: String, alignment: Align = AlignDefault, init: (ColorText.() -> Unit)? = null): ColorText {
        val o = ColorText(text = text, alignment = alignment).apply { init?.invoke(this) }
        _this.addChild(o)
        return o
    }

    fun textInput(text: String, placeholder: String = "polyui.textinput.placeholder", alignment: Align = AlignDefault, init: (TextInput.() -> Unit)? = null): TextInput {
        val o = TextInput(text = text, placeholder = placeholder, alignment = alignment).apply { init?.invoke(this) }
        _this.addChild(o)
        return o
    }

    fun calculate(text: String, lines: LinkedList<MutablePair<String, Float>>, alignment: Align = AlignDefault, init: (Calculate.() -> Unit)? = null): Calculate {
        val o = Calculate(text = text, lines = lines, alignment = alignment).apply { init?.invoke(this) }
        _this.addChild(o)
        return o
    }

    fun spacer(size: Vec2, init: (DrawableDSL.() -> Unit)? = null) {
        _this.addChild(DrawableDSL(Spacer(size = size)).apply { init?.invoke(this) }._this)
    }

    fun group(alignment: Align = AlignDefault, init: DrawableDSL.(Group) -> Unit): Group {
        val o = Group(alignment = alignment).apply { init.invoke(DrawableDSL(this), this) }
        _this.addChild(o)
        return o
    }

    fun group(alignment: Align = AlignDefault, at: Vec2?, init: DrawableDSL.(Group) -> Unit): Group {
        val o = Group(alignment = alignment, at = at).apply { init.invoke(DrawableDSL(this), this) }
        _this.addChild(o)
        return o
    }

    class Master : DrawableDSL(Block()) {
        var size: Vec2? = null
        private var _renderer: Renderer? = null
        var settings = Settings()
        var inputManager: InputManager? = null
        var translator: Translator? = null
        var backgroundColor: PolyColor? = null
        var alignment: Align = Align(cross = Align.Cross.Start, padding = Vec2.ZERO)
        val colors: Colors = DarkTheme()

        var renderer: Renderer
            get() = _renderer ?: error("Renderer not set")
            set(value) {
                _renderer = value
            }

        fun build() = PolyUI(
            drawables = _this.children!!.toTypedArray(),
            renderer, settings, inputManager, translator, backgroundColor,
            alignment, colors, size
        )
    }
}

@DslMarker
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
private annotation class PolyUIDSL

object Fonts {
    val minecraftTen = Font("/assets/hysentials/fonts/Minecraft Ten.ttf")
    val minecraftReg = Font("/assets/hysentials/fonts/Minecraft-Regular.otf")
}

fun Renderer.colorTextBounds(font: Font, text: String, fontSize: Float): Vec2 {
    var text = text.replace(Regex("§[0-9a-fk-or]"), "")
    return this.textBounds(font, text, fontSize)
}
@PolyUIDSL
@JvmSynthetic
@ApiStatus.Experimental
inline fun polyUI(block: DrawableDSL.Master.() -> Unit) = DrawableDSL.Master().apply(block).build()