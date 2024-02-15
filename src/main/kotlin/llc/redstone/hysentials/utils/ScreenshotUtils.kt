package llc.redstone.hysentials.utils

import llc.redstone.hysentials.util.Renderer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.TextureUtil
import net.minecraft.client.shader.Framebuffer
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL12
import java.awt.image.BufferedImage

//fun screenshot(messages: String, scale: Float = 1.0f, xSize: Int = 1000): BufferedImage? {
//    if (messages.isEmpty()) {
//        return null
//    }
//    var messages = messages.split("\n")
//    messages = messages.asReversed()
//    if (!OpenGlHelper.isFramebufferEnabled()) {
//        return null
//    }
//
//    val fr: FontRenderer = fontRenderer!!
//    val width = messages.maxOf { fr.getStringWidth(it) } + 4
//    val fb: Framebuffer = createBindFramebuffer((width * scale).toInt(), ((messages.size * 9) * scale).toInt())
//
//    GlStateManager.scale(scale, scale, 1f)
//    val scale = Minecraft.getMinecraft().gameSettings.chatScale
//    GlStateManager.scale(scale, scale, 1f)
//    messages.forEachIndexed { i: Int, value: String ->
//        Renderer.drawString(value, 0f, ((messages.size - 1 - i) * 9f).toInt(), true)
//    }
//
//    val image = fb.screenshot()
//    Minecraft.getMinecraft().entityRenderer.setupOverlayRendering()
//    Minecraft.getMinecraft().framebuffer.bindFramebuffer(true)
//
//    return image
//}

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