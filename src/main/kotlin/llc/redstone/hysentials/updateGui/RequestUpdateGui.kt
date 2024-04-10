package llc.redstone.hysentials.updateGui

import cc.polyfrost.oneconfig.gui.elements.BasicElement
import cc.polyfrost.oneconfig.libs.universal.UKeyboard
import cc.polyfrost.oneconfig.libs.universal.UResolution
import cc.polyfrost.oneconfig.libs.universal.UResolution.scaleFactor
import cc.polyfrost.oneconfig.libs.universal.UScreen
import cc.polyfrost.oneconfig.renderer.NanoVGHelper
import cc.polyfrost.oneconfig.renderer.asset.Image
import cc.polyfrost.oneconfig.renderer.asset.SVG
import cc.polyfrost.oneconfig.renderer.font.Fonts
import cc.polyfrost.oneconfig.utils.InputHandler
import cc.polyfrost.oneconfig.utils.dsl.nanoVGHelper
import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.util.Renderer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiMainMenu
import net.minecraft.client.gui.GuiScreen
import java.awt.Color


class RequestUpdateGui(private var inGame: Boolean, private var deleteOld: Boolean = false) : GuiScreen() {
    companion object {
        var instance: RequestUpdateGui? = null
    }

    private var xSize = 367
    private var ySize = 460
    private var scale = 0f
    private var guiLeft = 0f
    private var guiTop = 0f


    val updateObj = UpdateChecker.updateGetter.updateObj ?: error("Update object is null")
    val updateNotes = UpdateChecker.updateGetter.updateNotes

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)
        drawScreenExternal(mouseX, mouseY, partialTicks)
    }

    fun drawScreenExternal(mouseX: Int, mouseY: Int, partialTicks: Float) {
        NanoVGHelper.INSTANCE.setupAndDraw(false) {
            val nano = NanoVGHelper.INSTANCE
            val inputHandler = InputHandler()
            inputHandler.scale(scale.toDouble(), scale.toDouble())
            nano.scale(it, scale, scale)

            val x = guiLeft / scale
            val y = guiTop / scale
            val width = xSize
            val height = ySize


            nano.drawImage(
                it,
                Image("/assets/hysentials/gui/updater/background.png"),
                x,
                y,
                width.toFloat(),
                height.toFloat()
            )

            //Title: 35, 31 to 331, 60
            var title = "Hysentials Update Available"
            if (updateNotes?.name != null) {
                title = updateNotes.name
            }
            nano.drawText(
                it,
                title,
                x + 35f + (294f - nano.getTextWidth(it, title, 16f, Fonts.MINECRAFT_REGULAR)) / 2,
                y + 31f + 15f,
                Color(255, 255, 255, 255).rgb,
                16f,
                Fonts.MINECRAFT_REGULAR
            )
            //Version: 15, 76 to 94, 95
            val version = updateObj.name.substringAfter("Hysentials-").substringBefore(".jar")
            if (version.length > 15) {
                nano.drawText(
                    it,
                    version,
                    x + 15f + (80f - nano.getTextWidth(it, version, 8F, Fonts.MINECRAFT_REGULAR)) / 2,
                    y + 76f + 10f,
                    Color(255, 255, 255, 255).rgb,
                    8F,
                    Fonts.MINECRAFT_REGULAR
                )
            } else {
                nano.drawText(
                    it,
                    version,
                    x + 15f + (80f - nano.getTextWidth(it, version, 12F, Fonts.MINECRAFT_REGULAR)) / 2,
                    y + 76f + 10f,
                    Color(255, 255, 255, 255).rgb,
                    12F,
                    Fonts.MINECRAFT_REGULAR
                )
            }

            if (inputHandler.isAreaHovered(x + 334f, y + 34f, 23f, 23f) && inputHandler.isMouseDown(0)) {
                UpdateChecker.updateGetter.updateObj = null
                if (!inGame) {
                    Minecraft.getMinecraft().displayGuiScreen(GuiMainMenu())
                } else {
                    Minecraft.getMinecraft().thePlayer.closeScreen()
                }
            }

            if (UKeyboard.isKeyDown(UKeyboard.KEY_ESCAPE)) {
                UpdateChecker.updateGetter.updateObj = null
                if (!inGame) {
                    Minecraft.getMinecraft().displayGuiScreen(GuiMainMenu())
                } else {
                    Minecraft.getMinecraft().thePlayer.closeScreen()
                }
            }

            val notes = updateNotes?.notes ?: "No notes available"
            nano.drawWrappedString(
                it,
                notes,
                x + 20f,
                y + 220f,
                330f,
                Color(255, 255, 255, 255).rgb,
                12f,
                Fonts.MINECRAFT_REGULAR
            )

            //Update/Install Button
            val textUpdate = if (deleteOld) "Update" else "Install"
            val widthUpdates = 354f / 2 - 5
            var hover = inputHandler.isAreaHovered(x + 6f, y + 382f, widthUpdates, 46f)
            nano.drawSvg(
                it,
                if (!hover) SVG("/assets/hysentials/gui/updater/button.svg") else SVG("/assets/hysentials/gui/updater/buttonPressed.svg"),
                x + 6f,
                y + 382f,
                widthUpdates, // there are supposed to be 2 buttons within this width but I'm not sure how to do that
                46f
            )
            nano.drawText(
                it,
                textUpdate,
                x + 6f + (widthUpdates - nano.getTextWidth(it, textUpdate, 16f, Fonts.MINECRAFT_REGULAR)) / 2,
                y + 382f + 23f,
                Color(41, 44, 45, 255).rgb,
                16f,
                Fonts.MINECRAFT_REGULAR
            )

            if (inputHandler.isAreaHovered(x + 6f, y + 382f, widthUpdates, 46f) && inputHandler.isMouseDown(0)) {
                Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(UpdateGui(true, deleteOld))
            }

            //Update Later/Install Later Button
            val textLater = if (deleteOld) "Update Later" else "Install Later"
            hover = inputHandler.isAreaHovered(x + 6f + 354f / 2 + 5, y + 382f, widthUpdates, 46f)
            nano.drawSvg(
                it,
                if (!hover) SVG("/assets/hysentials/gui/updater/button.svg") else SVG("/assets/hysentials/gui/updater/buttonPressed.svg"),
                x + 6f + 354f / 2 + 5,
                y + 382f,
                widthUpdates, // there are supposed to be 2 buttons within this width but I'm not sure how to do that
                46f
            )
            nano.drawText(
                it,
                textLater,
                x + 6f + 354f / 2 + 5 + (widthUpdates - nano.getTextWidth(it, textLater, 16f, Fonts.MINECRAFT_REGULAR)) / 2,
                y + 382f + 23f,
                Color(41, 44, 45, 255).rgb,
                16f,
                Fonts.MINECRAFT_REGULAR
            )

            if (inputHandler.isAreaHovered(x + 6f + 354f / 2 + 5, y + 382f, widthUpdates, 46f) && inputHandler.isMouseDown(0)) {
                Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(UpdateGui(false, deleteOld))
            }
        }
    }

    override fun initGui() {
        super.initGui()

        instance = this

        scale = UResolution.windowHeight / 480f
        guiLeft = (UResolution.windowWidth - (xSize * scale)) / 2
        guiTop = (UResolution.windowHeight - (ySize * scale)) / 2
        if (guiLeft < 0) {
            guiLeft = 0f
        }
    }
}