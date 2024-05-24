package llc.redstone.hysentials.util;

import llc.redstone.hysentials.config.hysentialmods.FormattingConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

import java.awt.*;

import static net.minecraftforge.fml.client.config.GuiUtils.drawGradientRect;

public class RenderItemUtils {
    public static void drawSlotDecoration(int i, int j, ItemStack item, boolean hotbar) {
        if (!FormattingConfig.slotDecoration) return;
        if (hotbar && !FormattingConfig.showInHotbar) return;
        if (item == null) return;
        String s = item.getDisplayName();
        if (Minecraft.getMinecraft().fontRendererObj.getStringWidth(s) == 0) return; //Don't render if the string is empty or its supposed to be hidden
        String hex = ImageIconRenderer.getHexFromString(s, true);
        if (hex == null || hex.isEmpty()) {
            return;
        }
        Color c = Color.decode(hex);
        int offset = FormattingConfig.slotDecorationColorOffset;
        int red = MathHelper.clamp_int(c.getRed() + offset, 0, 255);
        int green = MathHelper.clamp_int(c.getGreen() + offset, 0, 255);
        int blue = MathHelper.clamp_int(c.getBlue() + offset, 0, 255);
        c = new Color(red, green, blue, FormattingConfig.slotDecorationAlpha);
        int c1 = c.getRGB();
        int c2 = (c1 & 0xFEFEFE) >> 1 | c1 & 0xFF000000;
        GlStateManager.pushMatrix();
        if (FormattingConfig.slotDecorationStyle == 0) { //Circle
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            Renderer.drawCircle(c1, i + 8, j + 8, 8, 100, 5);
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
        } else if (FormattingConfig.slotDecorationStyle == 1) { //Square
            drawGradientRect(0, i, j, i + 16, j + 16, c1, c1);
        } else if (FormattingConfig.slotDecorationStyle == 2){ //Partial Outline
            drawGradientRect(0, i, j, i + 1, j + 16, c2, c1);
            drawGradientRect(0, i + 15, j, i + 16, j + 16, c2, c1);
            drawGradientRect(0, i, j + 15, i + 16, j + 16, c1, c1);
        } else {
            drawGradientRect(0, i, j, i + 16, j + 1, c2, c2);
            drawGradientRect(0, i, j, i + 1, j + 16, c2, c1);
            drawGradientRect(0, i + 15, j, i + 16, j + 16, c2, c1);
            drawGradientRect(0, i, j + 15, i + 16, j + 16, c1, c1);
        }
        GlStateManager.popMatrix();
    }
}
