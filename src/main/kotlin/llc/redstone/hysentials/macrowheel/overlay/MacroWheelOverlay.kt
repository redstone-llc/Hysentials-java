package llc.redstone.hysentials.macrowheel.overlay

import cc.polyfrost.oneconfig.config.core.OneColor
import cc.polyfrost.oneconfig.libs.universal.UResolution
import cc.polyfrost.oneconfig.renderer.NanoVGHelper
import cc.polyfrost.oneconfig.utils.InputHandler
import llc.redstone.hysentials.GuiIngameHysentials
import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.macrowheel.MacroWheelData
import llc.redstone.hysentials.macrowheel.MacroWheelEditor
import llc.redstone.hysentials.util.C
import llc.redstone.hysentials.util.Material
import llc.redstone.hysentials.util.Renderer
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.client.event.GuiScreenEvent

//middle of the screen
var stopped = false;
class MacroWheelOverlay(
    val x: Float,
    val y: Float,
    val scale: Float,
    val background: OneColor
) {
    companion object {
        fun newI (): MacroWheelOverlay {
            val hudConfig = Hysentials.INSTANCE.config.macroWheelHud
            hudConfig.position.setSize(34f * hudConfig.scale, 34f * hudConfig.scale)
            val hud = MacroWheelOverlay(
                hudConfig.position.x,
                hudConfig.position.y,
                hudConfig.scale,
                hudConfig.bgColor
            )
            return hud
        }
    }
    fun drawPost(event: GuiScreenEvent.DrawScreenEvent.Post, inputHandler: InputHandler) {
        GlStateManager.pushMatrix()
        for (i in 0 until 8) {
            val macro = Hysentials.INSTANCE.macroJson.getMacro(i)

            val x = x + when (i) {
                0, 3, 5 -> 0f
                1, 6 -> 12f
                2, 4, 7 -> 24f
                else -> 0f;
            } * scale
            val y = y + when (i) {
                0, 1, 2 -> 0f
                3, 4 -> 12f
                5, 6, 7 -> 24f
                else -> 0f;
            } * scale
            val width = 10f * scale
            val height = 10f * scale
            if (macro != null) {
                val itemStack = ItemStack(Item.getItemById(macro.icon.id))
                //normal lighting effect

                GlStateManager.pushMatrix()

                GlStateManager.enableLighting()
                GlStateManager.enableDepth()
                RenderHelper.enableGUIStandardItemLighting()
                GlStateManager.enableRescaleNormal()
                GlStateManager.enableColorMaterial()
                GlStateManager.enableLighting()
                val otherScale = 2 * (scale/5f)
                GlStateManager.scale(otherScale, otherScale, otherScale)

                Minecraft.getMinecraft().renderItem.renderItemIntoGUI(
                    itemStack,
                    (x.toInt()).toScaled(otherScale) + (1.9 * scale).toInt().toScaled(otherScale),
                    (y.toInt()).toScaled(otherScale) + (1.9 * scale).toInt().toScaled(otherScale)
                )
                GlStateManager.popMatrix()
            }
        }

        for (i in 0 until 8) {
            val macro = Hysentials.INSTANCE.macroJson.getMacro(i)

            val x = x + when (i) {
                0, 3, 5 -> 0f
                1, 6 -> 12f
                2, 4, 7 -> 24f
                else -> 0f;
            } * scale
            val y = y + when (i) {
                0, 1, 2 -> 0f
                3, 4 -> 12f
                5, 6, 7 -> 24f
                else -> 0f;
            } * scale
            val width = 10f * scale
            val height = 10f * scale
            GlStateManager.pushMatrix()
            //mouse in region
            if (macro != null) {
                if (inputHandler.isAreaHovered(x, y, width, height)) {
                    val lore = mutableListOf<String>()
                    lore.add("§a" + C.translate(macro.name))
                    if (macro.hoverText.isNotEmpty()) lore.addAll(macro.hoverText.split("\\n").map {
                        C.translate(it)
                    })

                    val otherScale = 1 * (scale/5f)
                    GlStateManager.scale(otherScale, otherScale, otherScale)
                    net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(
                        lore,
                        inputHandler.mouseX().toInt().toScaled(otherScale),
                        inputHandler.mouseY().toInt().toScaled(otherScale),
                        UResolution.scaledWidth,
                        UResolution.scaledHeight,
                        -1,
                        Minecraft.getMinecraft().fontRendererObj
                    )

                    if (inputHandler.isMouseDown(0)) {
                        val command = ClientCommandHandler.instance.executeCommand(
                            Minecraft.getMinecraft().thePlayer,
                            macro.command
                        )
                        if (command != 1) {
                            Minecraft.getMinecraft().thePlayer.sendChatMessage("/" + macro.command)
                        }
                        stop()
                    } else if (inputHandler.isMouseDown(1)) {
                        MacroWheelEditor(i).open()
                        stop()
                    }
                }
            } else {
                if (inputHandler.isAreaHovered(x, y, width, height)) {
                    if (inputHandler.isMouseDown(0)) {
                        Hysentials.INSTANCE.macroJson.setMacro(
                            i,
                            MacroWheelData.MacroWheel(i, "Macro #${i + 1}", "help", Material.COMMAND, "")
                        )
                        MacroWheelEditor(i).open()
                        stop()
                    }
                }
            }
            GlStateManager.popMatrix()
        }

        GlStateManager.popMatrix()
    }

    fun stop () {
        stopped = true
        GuiIngameHysentials.wasMacroWheelActive = false
        if (Minecraft.getMinecraft().currentScreen == null) {
            Minecraft.getMinecraft().mouseHelper.grabMouseCursor()
        }
    }

    fun Float.toScaled(scale: Float): Float {
        return this * 1/scale
    }
    fun Int.toScaled(scale: Int): Int {
        return this * 1/scale
    }
    fun Int.toScaled(scale: Float): Int {
        return (this * 1/scale).toInt()
    }

    fun draw(it: Long, partialTicks: Float, inputHandler: InputHandler?) {
        val nvg = NanoVGHelper.INSTANCE
        for (i in 0 until 8) {
            val macro = Hysentials.INSTANCE.macroJson.getMacro(i)
            /*
            Top Corner    Top Middle    Top Corner
            Middle Left   Empty        Middle Right
            Bottom Corner Bottom Middle Bottom Corner
             */
            val x = x + when (i) {
                0, 3, 5 -> 0f
                1, 6 -> 12f
                2, 4, 7 -> 24f
                else -> 0f;
            } * scale
            val y = y + when (i) {
                0, 1, 2 -> 0f
                3, 4 -> 12f
                5, 6, 7 -> 24f
                else -> 0f;
            } * scale
            val width = 10f * scale
            val height = 10f * scale
            if (inputHandler?.isAreaHovered(x, y, width, height) == false) {
                nvg.drawRect(it, x, y, width, height, background.getRGB())
            } else {
                val color = background
                val newColor = Renderer.color(
                    color.red + 20,
                    color.green + 20,
                    color.blue + 20,
                    color.alpha
                )
                nvg.drawRect(it, x, y, width, height, newColor)

            }
            if (macro == null) {
                drawCross(it, x, y + 1 * scale, Renderer.color(140, 140, 140, 150).toInt())
                drawCross(it, x, y, Renderer.color(255, 255, 255, 150).toInt())
            }
        }
    }



    fun drawCross(it: Long, x: Float, y: Float, color: Int) {
        val nvg = NanoVGHelper.INSTANCE
        var x = x + 4.5f * scale
        var y = y + 3.5f * scale

        nvg.drawRect(it, x, y, 1f * scale, 3f * scale, color)

        x -= 1f * scale
        y += 1f * scale

        nvg.drawRect(it, x, y, 3f * scale, 1f * scale, color)
    }
}