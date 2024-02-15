package llc.redstone.hysentials.cosmetic

import llc.redstone.hysentials.util.Renderer
import net.minecraft.util.ResourceLocation

class CosmeticTab(
    val name: String,
    val displayName: String,
    val resource: ResourceLocation,
    val color: String,
    val width: Double,
    val height: Double,
    val x: Double,
    val y: Double) {

    fun draw (guiLeft: Int, guiTop: Int) {
        if (CosmeticGui.type == name) {
            Renderer.drawImage(
                resource,
                guiLeft + x,
                guiTop + y,
                width,
                height
            )
        }
    }

    fun isHovered (rX: Double, rY: Double): Boolean {
        return rX in x..x + width && rY in y..y + height && CosmeticGui.type !== name
    }
}

