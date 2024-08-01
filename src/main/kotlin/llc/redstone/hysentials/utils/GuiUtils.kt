package llc.redstone.hysentials.utils

import llc.redstone.hysentials.guis.container.GuiItem
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack

fun drawEntityOnScreen(posX: Int, posY: Int, scale: Int, xAngle: Float, yAngle: Float, ent: EntityLivingBase) {
    GlStateManager.enableLighting()
    GlStateManager.enableDepth()
    RenderHelper.enableStandardItemLighting()
    GlStateManager.enableRescaleNormal()
    GlStateManager.enableColorMaterial()
    GlStateManager.pushMatrix()
    GlStateManager.translate(posX.toFloat(), posY.toFloat(), 50.0f)
    GlStateManager.scale((-scale).toFloat(), scale.toFloat(), scale.toFloat())
    GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f)
    val f = ent.renderYawOffset
    val g = ent.rotationYaw
    val h = ent.rotationPitch
    val i = ent.prevRotationYawHead
    val j = ent.rotationYawHead

    ent.renderYawOffset = 0.0f
    ent.rotationYaw = 0.0f
    ent.rotationPitch = 0.0f
    ent.prevRotationYawHead = 0.0f
    ent.rotationYawHead = 0.0f
    GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f)
    RenderHelper.enableStandardItemLighting()
    GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f)
    GlStateManager.rotate(-Math.atan((yAngle / 40.0f).toDouble()).toFloat() * 20.0f, 1.0f, 0.0f, 0.0f)
    val renderManager = Minecraft.getMinecraft().renderManager
    GlStateManager.rotate(xAngle, 0.0f, 1.0f, 0.0f)
//        GlStateManager.rotate(yAngle, 1.0f, 0.0f, 0.0f)
    renderManager.setPlayerViewY(180.0f + yAngle)
    renderManager.isRenderShadow = false
    renderManager.renderEntityWithPosYaw(ent, 0.0, 0.0, 0.0, 0.0f, 1.0f)
    renderManager.isRenderShadow = true
    GlStateManager.rotate(-xAngle, 0.0f, 1.0f, 0.0f)
//        GlStateManager.rotate(-yAngle, 1.0f, 0.0f, 0.0f)
    ent.renderYawOffset = f
    ent.rotationYaw = g
    ent.rotationPitch = h
    ent.prevRotationYawHead = i
    ent.rotationYawHead = j
    GlStateManager.popMatrix()
    RenderHelper.disableStandardItemLighting()
    GlStateManager.disableRescaleNormal()
    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit)
    GlStateManager.disableTexture2D()
    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit)
}

fun ItemStack.getLore(): List<String> {
    return GuiItem.getLore(this)
}