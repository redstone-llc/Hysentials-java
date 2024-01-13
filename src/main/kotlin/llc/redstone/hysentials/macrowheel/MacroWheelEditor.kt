package llc.redstone.hysentials.macrowheel

import llc.redstone.hysentials.guis.container.Container
import llc.redstone.hysentials.guis.container.GuiItem
import llc.redstone.hysentials.util.Material
import net.minecraftforge.client.event.MouseEvent

class MacroWheelEditor(i: Int) : Container("Macro #${i + 1}", 6) {
    override fun setItems() {
        fill(GuiItem.fromStack(BLACK_STAINED_GLASS_PANE))

        setItem(49, GuiItem.fromStack(
            GuiItem.makeColorfulItem(
                Material.BARRIER,
                "&cClose",
                1, 0
            )
        ))
        setItem(48, GuiItem.fromStack(
            GuiItem.makeColorfulItem(
                Material.ARROW,
                "&aBack",
                1, 0
            )
        ))
    }

    override fun handleMenu(event: MouseEvent?) {
        TODO("Not yet implemented")
    }

    override fun setClickActions() {

    }

}
