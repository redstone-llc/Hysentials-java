package llc.redstone.hysentials.mixin;

import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.config.hysentialMods.FormattingConfig;
import llc.redstone.hysentials.util.ImageIconRenderer;
import llc.redstone.hysentials.Hysentials;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = GuiUtilRenderComponents.class)
public class GuiUtilRCMixin {
    @Redirect(method = "splitText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;getFormatFromString(Ljava/lang/String;)Ljava/lang/String;"))
    private static String onGetStringWidth(String c0) {
        if (!FormattingConfig.fancyRendering()) {
            return FontRenderer.getFormatFromString(c0);
        }
        return ImageIconRenderer.getFormatFromString(c0);
    }

    @Redirect(method = "splitText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;trimStringToWidth(Ljava/lang/String;IZ)Ljava/lang/String;"))
    private static String onTrimStringToWidth(FontRenderer fr, String c0, int c1, boolean c2) {
        if (!FormattingConfig.fancyRendering()) {
            return fr.trimStringToWidth(c0, c1, c2);
        }
        return Hysentials.INSTANCE.imageIconRenderer.trimStringToWidth(c0, c1, c2);
    }
}
