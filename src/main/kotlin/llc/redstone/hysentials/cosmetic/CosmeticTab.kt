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

    companion object {
        var ownedTabReal = CosmeticTab("owned", "Owned", ResourceLocation("hysentials:gui/wardrobe/tab/owned.png"), "32a852", 10.0, 10.0, 5.0, 4.0)
        var headTabReal = CosmeticTab("head", "Headwear", ResourceLocation("hysentials:gui/wardrobe/tab/headwear.png"), "1787e3", 9.0, 10.0, 5.0, 22.0)
        var backTabReal = CosmeticTab("back", "Chestwear", ResourceLocation("hysentials:gui/wardrobe/tab/chestwear.png"), "e69927", 9.0, 9.0, 5.0, 38.0)
        var pantaloonsTabReal = CosmeticTab("pantaloons", "Pantaloons", ResourceLocation("hysentials:gui/wardrobe/tab/pantaloons.png"), "8327e6", 9.0, 11.0, 5.0, 53.0)
        var bootsTabReal = CosmeticTab("boots", "Boots", ResourceLocation("hysentials:gui/wardrobe/tab/boots.png"), "ea323b", 11.0, 7.0, 4.0, 70.0)
        var petsTabReal = CosmeticTab("pet", "Pets", ResourceLocation("hysentials:gui/wardrobe/tab/pets.png"), "e4ea32", 9.0, 10.0, 5.0, 83.0)
        var chatTabReal = CosmeticTab("chat", "Chatbox", ResourceLocation("hysentials:gui/wardrobe/tab/chatbox.png"), "32eade", 9.0, 7.0, 5.0, 99.0)
        var bundlesTabReal = CosmeticTab("bundle", "Bundles", ResourceLocation("hysentials:gui/wardrobe/tab/bundles.png"), "e832e6", 9.0, 10.0, 5.0, 112.0)
        val tabs: List<CosmeticTab> = listOf(
            ownedTabReal,
            headTabReal,
            backTabReal,
            pantaloonsTabReal,
            bootsTabReal,
            petsTabReal,
            chatTabReal,
            bundlesTabReal
        )
    }

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

