package llc.redstone.hysentials.macrowheel

import cc.polyfrost.oneconfig.libs.universal.UMinecraft
import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.guis.container.Container
import llc.redstone.hysentials.guis.container.GuiItem
import llc.redstone.hysentials.util.Material
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.MouseEvent

class MacroWheelEditor(i: Int) : Container("Macro #${i + 1}", 6) {
    val macro = Hysentials.INSTANCE.macroJson.getMacro(i)
    override fun setItems() {
        fill(GuiItem.fromStack(BLACK_STAINED_GLASS_PANE))

        setItem(13, GuiItem.fromStack(
                GuiItem.makeColorfulItem(
                    Material.NAME_TAG,
                    "&aName",
                    1, 0,
                    "&7Current Name: &a${macro!!.name}",
                    "",
                    "&eClick to edit!"
                )
            )
        )

        setItem(22, GuiItem.fromStack(
                GuiItem.makeColorfulItem(
                    Material.COMMAND,
                    "&aCommand",
                    1, 0,
                    "&7Current Command: &a/${macro.command}",
                    "",
                    "&eClick to edit!"
                )
            )
        )

        val hoverText = macro.hoverText.split("\\n")
        val lore = mutableListOf<String>()
        lore.add("&7Current Hover Text: ")
        for (line in hoverText) {
            lore.add("&r$line")
        }
        lore.add("")
        lore.add("&eClick to edit!")
        setItem(21, GuiItem.fromStack(
                GuiItem.makeColorfulItem(
                    Material.PAPER,
                    "&aHover Text",
                    1, 0,
                    lore
                )
            )
        )

        setItem(23, GuiItem.fromStack(
                GuiItem.makeColorfulItem(
                    macro.icon,
                    "&aIcon",
                    1, 0,
                    "&7Current Icon: &a${macro.icon.name}",
                    "",
                    "&eClick to edit!"
                )
            )
        )

        setItem(
            49, GuiItem.fromStack(
                GuiItem.makeColorfulItem(
                    Material.BARRIER,
                    "&cClose",
                    1, 0
                )
            )
        )
        setItem(
            48, GuiItem.fromStack(
                GuiItem.makeColorfulItem(
                    Material.ARROW,
                    "&aBack",
                    1, 0
                )
            )
        )
    }

    override fun handleMenu(event: MouseEvent?) {
        TODO("Not yet implemented")
    }


    override fun setClickActions() {
        setAction(23) {
            it.event.cancel()
            MaterialSelector(macro!!).open()
        }
        setAction(13) {
            it.event.cancel()
            guiRequest(macro?.name?:"Macro Name", { message: String ->
                macro!!.name = message
                macro.save()
                this.update()
            }, 30000 * 10)
        }
        setAction(22) {
            it.event.cancel()
            guiRequest(macro?.command?:"Macro Command (no /)", { message: String ->
                macro!!.command = message
                macro.save()
                this.update()
                null
            }, 30000 * 10)
        }
        setAction(21) {
            it.event.cancel()
            guiRequest(macro?.hoverText?:"Macro Hover Text", { message: String ->
                macro!!.hoverText = message
                macro.save()
                this.update()
                null
            }, 30000 * 10)
        }
        setAction(49) {
            it.event.cancel()
            UMinecraft.getPlayer()!!.closeScreen()
        }

        setAction(48) {
            it.event.cancel()
            MacroWheelSelector().open()
        }
    }

}
