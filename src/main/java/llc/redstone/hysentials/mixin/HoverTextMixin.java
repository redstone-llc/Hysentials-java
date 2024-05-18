package llc.redstone.hysentials.mixin;

import llc.redstone.hysentials.config.hysentialmods.FormattingConfig;
import llc.redstone.hysentials.util.C;
import llc.redstone.hysentials.util.ImageIconRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.awt.*;
import java.util.List;

import static llc.redstone.hysentials.util.C.toHex;
import static net.minecraftforge.fml.client.config.GuiUtils.drawGradientRect;

@Mixin(value = GuiUtils.class, priority = Integer.MAX_VALUE, remap = false)
public abstract class HoverTextMixin {

    @Redirect(method = "drawHoveringText", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/config/GuiUtils;drawGradientRect(IIIIIII)V"))
    private static void drawHoveringText(int zLevel, int left, int top, int right, int bottom, int startColor, int endColor, List<String> textLines, final int mouseX, final int mouseY, final int screenWidth, final int screenHeight, final int maxTextWidth, FontRenderer font) {
        int end = (0x505000FF & 0xFEFEFE) >> 1 | 0x505000FF & 0xFF000000;
        if (FormattingConfig.hoverOutlineColor && (startColor == 0x505000FF || endColor == end)) {
            String line = C.removeControlCodes(textLines.get(0)); //remove anything that doesn't effect color
            line = ImageIconRenderer.getHexFromString(line, true);
            if (line == null || line.isEmpty()) {
                drawGradientRect(zLevel, left, top, right, bottom, startColor, endColor);
                return;
            }
            Color c = Color.decode(line);
            int c2 = 255 << 24 | c.getRed() << 16 | c.getGreen() << 8 | c.getBlue();
            drawGradientRect(zLevel, left, top, right, bottom, c2, c2);
        } else {
            drawGradientRect(zLevel, left, top, right, bottom, startColor, endColor);
        }
    }
}
