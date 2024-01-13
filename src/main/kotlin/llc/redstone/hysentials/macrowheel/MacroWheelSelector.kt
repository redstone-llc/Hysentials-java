package llc.redstone.hysentials.macrowheel

import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.guis.container.Container
import llc.redstone.hysentials.guis.container.GuiItem
import llc.redstone.hysentials.util.Material
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.MouseEvent

class MacroWheelSelector : Container("Macro Wheel Selector", 6) {
    val macros = Hysentials.INSTANCE.macroJson.getMacros()
    override fun setItems() {
        fill(GuiItem.fromStack(BLACK_STAINED_GLASS_PANE))

        val macroSlots = arrayOf(12, 13, 14, 21, 23, 30, 31, 32)
        for (i in 0 until 8) {
            if (i < macros.size) {
                setItem(macroSlots[i], macro(i))
            } else {
                setItem(macroSlots[i], emptyMacro(i))
            }
        }


        setItem(49, GuiItem.fromStack(
            GuiItem.makeColorfulItem(
                Material.BARRIER,
                "&cClose",
                1, 0
            )
        ))
    }

    fun emptyMacro(index: Int): GuiItem {
        return GuiItem.fromStack(
            GuiItem.makeColorfulItem(
            Material.WOOD_BUTTON,
            "&aCreate Macro #${index + 1}",
            1, 0,
            "", "&eClick to create a command macro!"
        ))
    }

    fun macro(index: Int): GuiItem {
        return GuiItem.fromStack(
            GuiItem.makeColorfulItem(
            Material.COMMAND,
            "&aMacro #${index + 1}",
            1, 0,
            "&7Icon: &a${macros[index].icon}",
            "&7Command: &b/${macros[index].command}",
            "&7Hover Text: &r${macros[index].hoverText}",
            "",
            "&eClick to edit!",
            "&eRight click to remove!"
        ))
    }

    override fun handleMenu(event: MouseEvent?) {

    }

    override fun setClickActions() {
        setDefaultAction{
            it.event.cancel()
        }
        setAction(49) {
            it.event.cancel()
            Minecraft.getMinecraft().thePlayer.closeScreen()
        }
        val macroSlots = arrayOf(12, 13, 14, 21, 23, 30, 31, 32)
        for (i in 0 until 8) {
            if (i < macros.size) {
                setAction(macroSlots[i]) {
                    it.event.cancel()
                    Minecraft.getMinecraft().thePlayer.closeScreen()
                    MacroWheelEditor(i).open()
                }
                setAction(macroSlots[i]) {
                    it.event.cancel()
                    Hysentials.INSTANCE.macroJson.removeMacro(i)
                    Minecraft.getMinecraft().thePlayer.closeScreen()
                    MacroWheelSelector().open()
                }
            } else {
                setAction(macroSlots[i]) {
                    it.event.cancel()
                    Minecraft.getMinecraft().thePlayer.closeScreen()
                    MacroWheelEditor(i).open()
                }
            }
        }
    }
}