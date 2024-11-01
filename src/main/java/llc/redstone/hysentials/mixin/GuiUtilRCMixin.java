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
    @Inject(method="splitText", at=@At("HEAD"), cancellable = true)
    private static void onSplitText(IChatComponent p_178908_0_, int p_178908_1_, FontRenderer p_178908_2_, boolean p_178908_3_, boolean p_178908_4_, CallbackInfoReturnable<List<IChatComponent>> cir) {
        if (FormattingConfig.fancyRendering() && !p_178908_3_ && !p_178908_4_) { //Look for chat messages only
            //signs and books can eat shit :)
            cir.setReturnValue(ImageIconRenderer.splitTextOld(p_178908_0_, p_178908_1_, p_178908_2_, false, false));
        }
    }
}
