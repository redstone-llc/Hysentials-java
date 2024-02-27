package llc.redstone.hysentials.updateGui

import cc.polyfrost.oneconfig.libs.universal.ChatColor
import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.guis.container.GuiItem
import llc.redstone.hysentials.util.ImageIconRenderer
import llc.redstone.hysentials.util.Renderer
import llc.redstone.hysentials.util.Renderer.Frame
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.*
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.texture.TextureUtil
import net.minecraft.client.shader.Framebuffer
import net.minecraft.util.ResourceLocation
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import java.awt.image.BufferedImage
import java.io.IOException
import java.net.URL
import javax.imageio.ImageIO


class RequestUpdateGui(private var inGame: Boolean, private var deleteOld: Boolean = false) : GuiScreen() {
    companion object {
        var instance: RequestUpdateGui? = null
    }

    private var xSize = 367
    private var ySize = 460
    var scale = 1.0f
    private var guiLeft = 0
    private var guiTop = 0
    var textureManager = Minecraft.getMinecraft().textureManager
    var titleImage: Frame? = null
    var updateImage: Frame? = null
    var updateNote: Frame? = null
    var background = ResourceLocation("hysentials:gui/updateBackground.png")
    var button1 = ResourceLocation("hysentials:gui/updateButton.png")
    var button2 = ResourceLocation("hysentials:gui/UpdateLaterButton.png")

    var fontRenderer: ImageIconRenderer? = Hysentials.INSTANCE.imageIconRenderer
    var fontRendererObj: FontRenderer = Minecraft.getMinecraft().fontRendererObj
    val updateObj = UpdateChecker.updateGetter.updateObj ?: error("Update object is null")
    val updateNotes = UpdateChecker.updateGetter.updateNotes

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)
        drawScreenExternal(mouseX, mouseY, partialTicks)
    }

    fun drawScreenExternal(mouseX: Int, mouseY: Int, partialTicks: Float) {
        GlStateManager.pushMatrix()

        drawGradientRect(0, 0, width, height, -1072689136, -804253680)
        try {
            Renderer.translate(guiLeft.toDouble(), guiTop.toDouble(), 0.0)

            Renderer.drawImage(
                background,
                0.0,
                0.0,
                xSize.toDouble(),
                ySize.toDouble()
            )

            Renderer.drawTextCenteredScaled(
                "§f" + updateObj.name.replace(".jar", "").replace("Hysentials-", "Hysentials ") + " Update",
                0f,
                4f,
                xSize.toFloat(),
                33f,
                1.5f
            )

            if (updateImage != null) {
                Renderer.drawFrameCentered(
                    updateImage,
                    xSize.toDouble(),
                    123.0,
                    219.0,
                    123.0,
                    0.0,
                    49.0
                )
            }

            var notes = updateNotes?.notes ?: "No update notes"
            notes = notes.replace("\n", " /newline ")
            notes = notes.replace("\\n", " /newline ")
            GuiItem.stringToLore(notes, 60, ChatColor.WHITE).forEachIndexed { index, s ->
                if (s == "/newline") {
                    return@forEachIndexed
                }
                if (index > 16) {
                    return@forEachIndexed
                }
                Renderer.drawText(
                    s,
                    12f,
                    192f + (index * 10)
                )
            }

            var relativeX = ((mouseX - guiLeft))
            var relativeY = ((mouseY - guiTop))

            if (relativeX in 12..176 && relativeY in 408..443) {
                Renderer.drawImage(
                    button1,
                    (12).toDouble(),
                    (408).toDouble(),
                    165.0,
                    40.0
                )
            }

            if (relativeX in 190..354 && relativeY in 408..443) {
                Renderer.drawImage(
                    button2,
                    (190).toDouble(),
                    (408).toDouble(),
                    165.0,
                    40.0
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Renderer.untranslate(0.0, 0.0, 0.0)
        GlStateManager.popMatrix()
    }

    override fun initGui() {
        super.initGui()

        try {
            updateImage = if (updateNotes?.image != null) {
                Frame(ImageIO.read(URL(updateNotes.image)))
            } else if (updateNotes != null) {
                Frame(ResourceLocation("hysentials:gui/updater_background.png"))
            } else {
                Frame(ResourceLocation("hysentials:gui/updater_background.png"))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        scale = (Renderer.screen.getHeight() / 500f)
        guiLeft = (Renderer.screen.getWidth() - xSize) / 2
        guiTop = (Renderer.screen.getHeight() - ySize) / 2

        if (guiTop < 0) {
            guiTop = 20
        }
        instance = this
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        mouseClickExternal(mouseX, mouseY, mouseButton)
    }

    fun mouseClickExternal(mouseX: Int, mouseY: Int, mouseButton: Int) {
        var relativeX = ((mouseX - guiLeft))
        var relativeY = ((mouseY - guiTop))
        if (relativeX in 340..353 && relativeY in 13..26) {
            UpdateChecker.updateGetter.updateObj = null
            if (!inGame) {
                Minecraft.getMinecraft().displayGuiScreen(GuiMainMenu())
            } else {
                Minecraft.getMinecraft().thePlayer.closeScreen()
            }
        }

        if (relativeX in 12..176 && relativeY in 408..443) {
            Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(UpdateGui(true, deleteOld))
        }

        if (relativeX in 190..354 && relativeY in 408..443) {
            Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(UpdateGui(false, deleteOld))
        }
    }
}