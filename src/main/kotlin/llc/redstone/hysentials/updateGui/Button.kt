package llc.redstone.hysentials.updateGui

import llc.redstone.hysentials.util.Renderer
import net.minecraft.util.ResourceLocation

class Button(
    var x: Int, var y: Int, var width: Int, var height: Int, var hoverImage: String?, var gui: HysentialsGui?,
    var onHover: (mouseX: Double, mouseY: Double) -> Boolean = { _, _ -> false },
    var onClick: (mouseX: Double, mouseY: Double, mouseButton: Int) -> Unit,
) {
    private var resource = ResourceLocation(hoverImage)

    fun draw(mouseX: Int, mouseY: Int) {
        if (gui == null) return
        val relativeX = mouseX - gui!!.getLeft()
        val relativeY = mouseY - gui!!.getTop()
        if (relativeX >= x && relativeX <= x + width && relativeY >= y && relativeY <= y + height && onHover(mouseX.toDouble(), mouseY.toDouble())) {
            Renderer.drawImage(resource, x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble())
        }
    }

    fun click(mouseX: Double, mouseY: Double, mouseButton: Int) {
        if (gui == null) return
        val relativeX = mouseX - gui!!.getLeft()
        val relativeY = mouseY - gui!!.getTop()

        if (relativeX >= x && relativeX <= x + width && relativeY >= y && relativeY <= y + height) {
            onClick(mouseX, mouseY, mouseButton)
        }
    }
}