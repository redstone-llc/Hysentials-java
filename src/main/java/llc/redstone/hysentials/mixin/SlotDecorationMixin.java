package llc.redstone.hysentials.mixin;

import llc.redstone.hysentials.config.hysentialMods.FormattingConfig;
import llc.redstone.hysentials.util.ImageIconRenderer;
import llc.redstone.hysentials.util.Renderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Slot;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

import static net.minecraftforge.fml.client.config.GuiUtils.drawGradientRect;

@Mixin(value = GuiContainer.class, priority = Integer.MAX_VALUE)
public abstract class SlotDecorationMixin {
    @Inject(method = "drawSlot", at = @At(value = "HEAD"))
    public void drawSlot(Slot slotIn, CallbackInfo ci) {
        if (FormattingConfig.slotDecoration) {
            int style = FormattingConfig.slotDecorationStyle;
            int i = slotIn.xDisplayPosition;
            int j = slotIn.yDisplayPosition;
            if (slotIn.getHasStack()) {
                String s = slotIn.getStack().getDisplayName();
                String hex = ImageIconRenderer.getHexFromString(s, true);
                if (hex == null || hex.isEmpty()) {
                    return;
                }
                Color c = Color.decode(hex);
                int offset = FormattingConfig.slotDecorationColorOffset;
                c = new Color(c.getRed() + offset, c.getGreen() + offset, c.getBlue() + offset, FormattingConfig.slotDecorationColor.getAlpha());
                int c2 = c.getRGB();
                if (style == 0) {
                    GlStateManager.pushMatrix();
                    GlStateManager.disableLighting();
                    GlStateManager.disableDepth();
                    Renderer.drawCircle(c2, i + 8, j + 8, 8, 100, 5);
                    GlStateManager.enableLighting();
                    GlStateManager.enableDepth();
                    GlStateManager.popMatrix();
                } else {
                    drawGradientRect(0, i, j, i + 16, j + 16, c2, c2);
                }
            }
        }
    }
}
