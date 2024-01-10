package cc.woverflow.hysentials.updateGui

import cc.polyfrost.oneconfig.libs.universal.ChatColor
import cc.woverflow.hysentials.Hysentials
import cc.woverflow.hysentials.guis.container.GuiItem
import cc.woverflow.hysentials.util.ImageIconRenderer
import cc.woverflow.hysentials.util.Renderer
import cc.woverflow.hysentials.util.Renderer.Frame
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
                Frame(ImageIO.read(URL(updateNotes.image)));
            } else if (updateNotes != null) {
                Frame(ImageIO.read(URL("https://i.imgur.com/Qq9kXpe.png")));
            } else {
                Frame(ImageIO.read(URL("https://i.imgur.com/Qq9kXpe.png")));
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

    private fun screenshot(messages: String, scale: Float = 1.0f, xSize: Int = 1000): BufferedImage? {
        if (messages.isEmpty()) {
            return null
        }
        var messages = messages.split("\n")
        messages = messages.asReversed()
        if (!OpenGlHelper.isFramebufferEnabled()) {
            return null
        }

        val fr: FontRenderer = fontRenderer!!
        val width = messages.maxOf { fr.getStringWidth(it) } + 4
        val fb: Framebuffer = createBindFramebuffer((width * scale).toInt(), ((messages.size * 9) * scale).toInt())

        GlStateManager.scale(scale, scale, 1f)
        val scale = Minecraft.getMinecraft().gameSettings.chatScale
        GlStateManager.scale(scale, scale, 1f)
        messages.forEachIndexed { i: Int, value: String ->
            Renderer.drawString(value, 0f, ((messages.size - 1 - i) * 9f).toInt(), true)
        }

        val image = fb.screenshot()
        Minecraft.getMinecraft().entityRenderer.setupOverlayRendering()
        Minecraft.getMinecraft().framebuffer.bindFramebuffer(true)

        return image
    }

    fun createBindFramebuffer(w: Int, h: Int): Framebuffer {
        val framebuffer = Framebuffer(w, h, false)
        framebuffer.framebufferColor[0] = 0x36 / 255f
        framebuffer.framebufferColor[1] = 0x39 / 255f
        framebuffer.framebufferColor[2] = 0x3F / 255f
        framebuffer.framebufferClear()
        GlStateManager.matrixMode(5889)
        GlStateManager.loadIdentity()
        GlStateManager.ortho(0.0, w.toDouble(), h.toDouble(), 0.0, 1000.0, 3000.0)
        GlStateManager.matrixMode(5888)
        GlStateManager.loadIdentity()
        GlStateManager.translate(0.0f, 0.0f, -2000.0f)
        framebuffer.bindFramebuffer(true)
        return framebuffer
    }

    fun Framebuffer.screenshot(): BufferedImage {
        val w = this.framebufferWidth
        val h = this.framebufferHeight
        val i = w * h
        val pixelBuffer = BufferUtils.createIntBuffer(i)
        val pixelValues = IntArray(i)
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1)
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1)
        GlStateManager.bindTexture(this.framebufferTexture)
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer)
        pixelBuffer[pixelValues] //Load buffer into array
        TextureUtil.processPixelValues(pixelValues, w, h) //Flip vertically
        val bufferedimage = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
        val j = this.framebufferTextureHeight - this.framebufferHeight
        for (k in j until this.framebufferTextureHeight) {
            for (l in 0 until this.framebufferWidth) {
                bufferedimage.setRGB(l, k - j, pixelValues[k * this.framebufferTextureWidth + l])
            }
        }
        return bufferedimage
    }
}