package llc.redstone.hysentials.polyui.components

import org.polyfrost.polyui.color.PolyColor
import org.polyfrost.polyui.component.Drawable
import org.polyfrost.polyui.unit.Align
import org.polyfrost.polyui.unit.AlignDefault
import org.polyfrost.polyui.unit.Vec2
import org.polyfrost.polyui.utils.areValuesEqual
import org.polyfrost.polyui.utils.cl1
import org.polyfrost.polyui.utils.radii

open class Box
@JvmOverloads
constructor(
    vararg children: Drawable?,
    at: Vec2? = null,
    size: Vec2? = null,
    alignment: Align = AlignDefault,
    visibleSize: Vec2? = null,
    focusable: Boolean = false,
    color: PolyColor? = null,
    var radii: FloatArray = 8f.radii(),
) : Drawable(children = children, at, alignment, size, visibleSize, focusable = focusable) {
    var shadow: Boolean = false
    override fun render() {
        val (topLeft, topRight, bottomRight, bottomLeft) = radii
        var topHeight = topRight.coerceAtLeast(topLeft);
        var bottomHeight = bottomRight.coerceAtLeast(bottomLeft);
        renderer.rect(
            x + topLeft, y, width - topRight - topLeft, topHeight, color
        )
        renderer.rect(
            x, y + topHeight, width, height - bottomHeight - topHeight, color
        )
        renderer.rect(
            x + bottomLeft, y + height - topHeight, width - bottomRight - bottomLeft, bottomHeight, color
        )

        if (shadow) {
            var shadowColor = color.clone()
            shadowColor.recolor(PolyColor(shadowColor.hue, shadowColor.saturation, shadowColor.brightness, shadowColor.alpha / 2))
            renderer.rect(
                x + width, y + (topHeight * 2), topRight, height - bottomHeight * 2, shadowColor
            )
            renderer.rect(
                x + width - (bottomRight), y + height - bottomHeight, bottomRight, bottomHeight, shadowColor
            )
            renderer.rect(
                x + (bottomLeft * 2), y + height, width - (bottomRight * 2), bottomHeight, shadowColor
            )
        }
    }

    override fun rescale(scaleX: Float, scaleY: Float, position: Boolean) {
        super.rescale(scaleX, scaleY, position)
        if (radii.areValuesEqual()) {
            val scale = cl1(scaleX, scaleY)
            for (i in 0..3) {
                radii[i] *= scale
            }
        }
    }
}