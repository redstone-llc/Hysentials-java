package llc.redstone.hysentials.cosmetic

import cc.polyfrost.oneconfig.utils.InputHandler
import llc.redstone.hysentials.cosmetics.AbstractCosmetic
import llc.redstone.hysentials.schema.HysentialsSchema
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraftforge.fml.client.config.GuiUtils

fun HysentialsSchema.Cosmetic.renderPreview(mouseX: Int, mouseY: Int, tick: Int) {
    val cosmeticModel = this.cosmeticModel?: return
    GlStateManager.disableRescaleNormal()
    RenderHelper.disableStandardItemLighting()
    GlStateManager.disableLighting()
    GlStateManager.disableDepth()

    val tooltipX = (mouseX + 12).toInt()
    val tooltipY = (mouseY - 12).toInt()
    val tooltipTextWidth = 300
    val tooltipHeight = 300

    val zLevel = 300
    val backgroundColor = -0xfeffff0
    GuiUtils.drawGradientRect(
        zLevel,
        tooltipX - 3,
        tooltipY - 4,
        tooltipX + tooltipTextWidth + 3,
        tooltipY - 3,
        backgroundColor,
        backgroundColor
    )
    GuiUtils.drawGradientRect(
        zLevel,
        tooltipX - 3,
        tooltipY + tooltipHeight + 3,
        tooltipX + tooltipTextWidth + 3,
        tooltipY + tooltipHeight + 4,
        backgroundColor,
        backgroundColor
    )
    GuiUtils.drawGradientRect(
        zLevel,
        tooltipX - 3,
        tooltipY - 3,
        tooltipX + tooltipTextWidth + 3,
        tooltipY + tooltipHeight + 3,
        backgroundColor,
        backgroundColor
    )
    GuiUtils.drawGradientRect(
        zLevel,
        tooltipX - 4,
        tooltipY - 3,
        tooltipX - 3,
        tooltipY + tooltipHeight + 3,
        backgroundColor,
        backgroundColor
    )
    GuiUtils.drawGradientRect(
        zLevel,
        tooltipX + tooltipTextWidth + 3,
        tooltipY - 3,
        tooltipX + tooltipTextWidth + 4,
        tooltipY + tooltipHeight + 3,
        backgroundColor,
        backgroundColor
    )
    val borderColorStart = 0x505000FF
    val borderColorEnd = (borderColorStart and 0xFEFEFE) shr 1 or (borderColorStart and -0x1000000)
    GuiUtils.drawGradientRect(
        zLevel,
        tooltipX - 3,
        tooltipY - 3 + 1,
        tooltipX - 3 + 1,
        tooltipY + tooltipHeight + 3 - 1,
        borderColorStart,
        borderColorEnd
    )
    GuiUtils.drawGradientRect(
        zLevel,
        tooltipX + tooltipTextWidth + 2,
        tooltipY - 3 + 1,
        tooltipX + tooltipTextWidth + 3,
        tooltipY + tooltipHeight + 3 - 1,
        borderColorStart,
        borderColorEnd
    )
    GuiUtils.drawGradientRect(
        zLevel,
        tooltipX - 3,
        tooltipY - 3,
        tooltipX + tooltipTextWidth + 3,
        tooltipY - 3 + 1,
        borderColorStart,
        borderColorStart
    )
    GuiUtils.drawGradientRect(
        zLevel,
        tooltipX - 3,
        tooltipY + tooltipHeight + 2,
        tooltipX + tooltipTextWidth + 3,
        tooltipY + tooltipHeight + 3,
        borderColorEnd,
        borderColorEnd
    )

    cosmeticModel.renderPreview(tooltipX, tooltipY, tick)

    GlStateManager.enableLighting()
    GlStateManager.enableDepth()
    RenderHelper.enableStandardItemLighting()
    GlStateManager.enableRescaleNormal()
}