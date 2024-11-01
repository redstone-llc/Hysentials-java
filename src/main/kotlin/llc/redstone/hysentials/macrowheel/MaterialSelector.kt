package llc.redstone.hysentials.macrowheel

import cc.polyfrost.oneconfig.libs.universal.UMinecraft
import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.guis.container.Backable
import llc.redstone.hysentials.guis.container.GuiAction
import llc.redstone.hysentials.guis.container.GuiItem
import llc.redstone.hysentials.guis.container.PaginationContainer
import llc.redstone.hysentials.util.C
import llc.redstone.hysentials.util.Material
import net.minecraft.client.Minecraft
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import java.util.function.BiConsumer
import java.util.function.Consumer

class MaterialSelector(private var macro: MacroWheelData.MacroWheel) : PaginationContainer("Material Selector", 6, null), Backable {

    override fun getItems(): MutableList<ItemStack> {
        val items = mutableListOf<ItemStack>()
        for (material in Material.values()) {
            if (Item.getItemById(material.id) == null) continue
            items.add(GuiItem.makeColorfulItem(material, "&a${material.name}", 1, 0))
        }
        return items
    }

    override fun getAction(index: Int): BiConsumer<GuiAction.GuiClickEvent, ItemStack> {
        return BiConsumer { event, item ->
            val material = Material.valueOf(
                C.removeColor(item.displayName)
                    .replace(" ", "_")
                    .uppercase())
            macro.icon = material
            macro.save()
            UMinecraft.getPlayer()!!.closeScreen()
            MacroWheelEditor(macro.index).open()
        }
    }

    override fun openBack() {
        MacroWheelEditor(macro.index).open()
    }

    override fun backTitle(): String {
        return "Macro Editor"
    }

    override fun backItemSlot(): Int {
        return 47
    }

}