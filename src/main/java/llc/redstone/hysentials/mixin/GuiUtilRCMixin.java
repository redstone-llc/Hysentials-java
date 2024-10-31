package llc.redstone.hysentials.mixin;

import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.hysentialmods.FormattingConfig;
import llc.redstone.hysentials.renderer.text.FancyFormatting2;
import llc.redstone.hysentials.renderer.text.FancyFormatting2Kt;
import llc.redstone.hysentials.util.ImageIconRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = GuiUtilRenderComponents.class)
public class GuiUtilRCMixin {
//    @Redirect(method = "splitText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;getFormatFromString(Ljava/lang/String;)Ljava/lang/String;"))
//    private static String getFormatString(String c0) {
//        if (!FormattingConfig.fancyRendering()) {
//            return FontRenderer.getFormatFromString(c0);
//        }
//        return FancyFormatting2.Companion.getLastFormat(c0);
//    }
//
//    @Redirect(method = "splitText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;getStringWidth(Ljava/lang/String;)I"))
//    private static int onGetStringWidth(FontRenderer fr, String c0) {
//        if (!FormattingConfig.fancyRendering()) {
//            return fr.getStringWidth(c0);
//        }
//        c0 = FancyFormatting2.Companion.replaceString(c0, true);
//        return fr.getStringWidth(c0);
//    }
//
//    @Redirect(method = "splitText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;trimStringToWidth(Ljava/lang/String;IZ)Ljava/lang/String;"))
//    private static String onTrimStringToWidth(FontRenderer fr, String c0, int c1, boolean c2) {
//        if (!FormattingConfig.fancyRendering()) {
//            return fr.trimStringToWidth(c0, c1, c2);
//        }
//        c0 = FancyFormatting2.Companion.replaceString(c0, true);
//        return fr.trimStringToWidth(c0, c1, c2);
////        return fr.trimStringToWidth(c0, c1, c2);
//    }

    @Inject(method="splitText", at=@At("HEAD"), cancellable = true)
    private static void onSplitText(IChatComponent p_178908_0_, int p_178908_1_, FontRenderer p_178908_2_, boolean p_178908_3_, boolean p_178908_4_, CallbackInfoReturnable<List<IChatComponent>> cir) {
        if (FormattingConfig.fancyRendering()) {
            cir.setReturnValue(ImageIconRenderer.splitText(p_178908_0_, p_178908_1_, p_178908_2_, p_178908_3_, p_178908_4_));
        }
    }
}
