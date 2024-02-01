package llc.redstone.hysentials.utils

import cc.polyfrost.oneconfig.renderer.NanoVGHelper
import cc.polyfrost.oneconfig.renderer.font.Fonts
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraftforge.fml.client.config.GuiUtils.drawGradientRect

fun drawHoveringText(
    textLines: List<String?>,
    mouseX: Int,
    mouseY: Int,
    screenWidth: Int,
    screenHeight: Int,
    maxTextWidth: Int,
    font: FontRenderer
) {
    var textLines = textLines
    if (!textLines.isEmpty()) {
        GlStateManager.disableRescaleNormal()
        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableLighting()
        GlStateManager.disableDepth()
        var tooltipTextWidth = 0

        for (textLine in textLines) {
            val textLineWidth = font.getStringWidth(textLine)

            if (textLineWidth > tooltipTextWidth) {
                tooltipTextWidth = textLineWidth
            }
        }

        var needsWrap = false

        var titleLinesCount = 1
        var tooltipX = mouseX + 12
        if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
            tooltipX = mouseX - 16 - tooltipTextWidth
            if (tooltipX < 4) // if the tooltip doesn't fit on the screen
            {
                tooltipTextWidth = if (mouseX > screenWidth / 2) {
                    mouseX - 12 - 8
                } else {
                    screenWidth - 16 - mouseX
                }
                needsWrap = true
            }
        }

        if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth) {
            tooltipTextWidth = maxTextWidth
            needsWrap = true
        }

        if (needsWrap) {
            var wrappedTooltipWidth = 0
            val wrappedTextLines: MutableList<String?> = ArrayList()
            for (i in textLines.indices) {
                val textLine = textLines[i]
                val wrappedLine = font.listFormattedStringToWidth(textLine, tooltipTextWidth)
                if (i == 0) {
                    titleLinesCount = wrappedLine.size
                }

                for (line in wrappedLine) {
                    val lineWidth = font.getStringWidth(line)
                    if (lineWidth > wrappedTooltipWidth) {
                        wrappedTooltipWidth = lineWidth
                    }
                    wrappedTextLines.add(line)
                }
            }
            tooltipTextWidth = wrappedTooltipWidth
            textLines = wrappedTextLines

            tooltipX = if (mouseX > screenWidth / 2) {
                mouseX - 16 - tooltipTextWidth
            } else {
                mouseX + 12
            }
        }

        var tooltipY = mouseY - 12
        var tooltipHeight = 8

        if (textLines.size > 1) {
            tooltipHeight += (textLines.size - 1) * 10
            if (textLines.size > titleLinesCount) {
                tooltipHeight += 2 // gap between title lines and next lines
            }
        }

        if (tooltipY + tooltipHeight + 6 > screenHeight) {
            tooltipY = screenHeight - tooltipHeight - 6
        }

        val zLevel = 300
        val backgroundColor = -0xfeffff0
        drawGradientRect(
            zLevel,
            tooltipX - 3,
            tooltipY - 4,
            tooltipX + tooltipTextWidth + 3,
            tooltipY - 3,
            backgroundColor,
            backgroundColor
        )
        drawGradientRect(
            zLevel,
            tooltipX - 3,
            tooltipY + tooltipHeight + 3,
            tooltipX + tooltipTextWidth + 3,
            tooltipY + tooltipHeight + 4,
            backgroundColor,
            backgroundColor
        )
        drawGradientRect(
            zLevel,
            tooltipX - 3,
            tooltipY - 3,
            tooltipX + tooltipTextWidth + 3,
            tooltipY + tooltipHeight + 3,
            backgroundColor,
            backgroundColor
        )
        drawGradientRect(
            zLevel,
            tooltipX - 4,
            tooltipY - 3,
            tooltipX - 3,
            tooltipY + tooltipHeight + 3,
            backgroundColor,
            backgroundColor
        )
        drawGradientRect(
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
        drawGradientRect(
            zLevel,
            tooltipX - 3,
            tooltipY - 3 + 1,
            tooltipX - 3 + 1,
            tooltipY + tooltipHeight + 3 - 1,
            borderColorStart,
            borderColorEnd
        )
        drawGradientRect(
            zLevel,
            tooltipX + tooltipTextWidth + 2,
            tooltipY - 3 + 1,
            tooltipX + tooltipTextWidth + 3,
            tooltipY + tooltipHeight + 3 - 1,
            borderColorStart,
            borderColorEnd
        )
        drawGradientRect(
            zLevel,
            tooltipX - 3,
            tooltipY - 3,
            tooltipX + tooltipTextWidth + 3,
            tooltipY - 3 + 1,
            borderColorStart,
            borderColorStart
        )
        drawGradientRect(
            zLevel,
            tooltipX - 3,
            tooltipY + tooltipHeight + 2,
            tooltipX + tooltipTextWidth + 3,
            tooltipY + tooltipHeight + 3,
            borderColorEnd,
            borderColorEnd
        )

        for (lineNumber in textLines.indices) {
            val line = textLines[lineNumber]
            font.drawStringWithShadow(line, tooltipX.toFloat(), tooltipY.toFloat(), -1)

            if (lineNumber + 1 == titleLinesCount) {
                tooltipY += 2
            }

            tooltipY += 10
        }

        GlStateManager.enableLighting()
        GlStateManager.enableDepth()
        RenderHelper.enableStandardItemLighting()
        GlStateManager.enableRescaleNormal()
    }
}

fun NanoVGHelper.drawHoveringText(
    vg: Long,
    textLines: List<String?>,
    mouseX: Float,
    mouseY: Float
) {
    val scaledresolution = ScaledResolution(Minecraft.getMinecraft())
    val screenWidth = scaledresolution.scaledWidth
    val screenHeight = scaledresolution.scaledHeight
    val font = Fonts.MINECRAFT_REGULAR
    val maxTextWidth = -1f

    var textLines = textLines
    if (!textLines.isEmpty()) {
        GlStateManager.disableRescaleNormal()
        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableLighting()
        GlStateManager.disableDepth()
        var tooltipTextWidth = 0f

        for (textLine in textLines) {
            val textLineWidth = getTextWidth(vg, textLine, 16f, font)

            if (textLineWidth > tooltipTextWidth) {
                tooltipTextWidth = textLineWidth
            }
        }

        var needsWrap = false

        var titleLinesCount = 1
        var tooltipX = mouseX + 12f
        if (tooltipX + tooltipTextWidth + 4 > screenWidth) {
            tooltipX = mouseX - 16 - tooltipTextWidth
            if (tooltipX < 4) // if the tooltip doesn't fit on the screen
            {
                tooltipTextWidth = if (mouseX > screenWidth / 2) {
                    mouseX - 12 - 8f
                } else {
                    screenWidth - 16f - mouseX
                }
                needsWrap = true
            }
        }

        if (maxTextWidth > 0 && tooltipTextWidth > maxTextWidth) {
            tooltipTextWidth = maxTextWidth
            needsWrap = true
        }

        var tooltipY = mouseY - 12f
        var tooltipHeight = 8f

        if (textLines.size > 1) {
            tooltipHeight += (textLines.size - 1) * 10
            if (textLines.size > titleLinesCount) {
                tooltipHeight += 2 // gap between title lines and next lines
            }
        }

        if (tooltipY + tooltipHeight + 6 > screenHeight) {
            tooltipY = screenHeight - tooltipHeight - 6
        }

        val zLevel = 300
        val backgroundColor = -0xfeffff0

        drawRect(
            vg,
            tooltipX - 3,
            tooltipY - 4,
            tooltipX + tooltipTextWidth + 3,
            tooltipY - 3,
            backgroundColor
        )
        drawRect(
            vg,
            tooltipX - 3,
            tooltipY + tooltipHeight + 3,
            tooltipX + tooltipTextWidth + 3,
            tooltipY + tooltipHeight + 4,
            backgroundColor
        )
        drawRect(
            vg,
            tooltipX - 3,
            tooltipY - 3,
            tooltipX + tooltipTextWidth + 3,
            tooltipY + tooltipHeight + 3,
            backgroundColor
        )
        drawRect(
            vg,
            tooltipX - 4,
            tooltipY - 3,
            tooltipX - 3,
            tooltipY + tooltipHeight + 3,
            backgroundColor
        )
        drawRect(
            vg,
            tooltipX + tooltipTextWidth + 3,
            tooltipY - 3,
            tooltipX + tooltipTextWidth + 4,
            tooltipY + tooltipHeight + 3,
            backgroundColor
        )
        val borderColorStart = 0x505000FF
        val borderColorEnd = (borderColorStart and 0xFEFEFE) shr 1 or (borderColorStart and -0x1000000)
        drawRect(
            vg,
            tooltipX - 3,
            tooltipY - 3 + 1,
            tooltipX - 3 + 1,
            tooltipY + tooltipHeight + 3 - 1,
            borderColorStart
        )
        drawRect(
            vg,
            tooltipX + tooltipTextWidth + 2,
            tooltipY - 3 + 1,
            tooltipX + tooltipTextWidth + 3,
            tooltipY + tooltipHeight + 3 - 1,
            borderColorStart
        )
        drawRect(
            vg,
            tooltipX - 3,
            tooltipY - 3,
            tooltipX + tooltipTextWidth + 3,
            tooltipY - 3 + 1,
            borderColorStart
        )
        drawRect(
            vg,
            tooltipX - 3,
            tooltipY + tooltipHeight + 2,
            tooltipX + tooltipTextWidth + 3,
            tooltipY + tooltipHeight + 3,
            borderColorEnd
        )

        for (lineNumber in textLines.indices) {
            val line = textLines[lineNumber]
            this.drawText(vg, line, tooltipX, tooltipY, -1, 16f, font)

            if (lineNumber + 1 == titleLinesCount) {
                tooltipY += 2
            }

            tooltipY += 10
        }
    }
}