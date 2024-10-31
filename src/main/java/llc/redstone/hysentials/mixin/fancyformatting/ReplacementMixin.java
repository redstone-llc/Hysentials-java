package llc.redstone.hysentials.mixin.fancyformatting;

import llc.redstone.hysentials.config.hysentialmods.FormattingConfig;
import llc.redstone.hysentials.renderer.text.FancyFormatting2;
import llc.redstone.hysentials.renderer.text.FancyFormattingKt;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = FontRenderer.class, priority = 1005)
public class ReplacementMixin {
    @ModifyVariable(method = "renderStringAtPos", at = @At("HEAD"), argsOnly = true)
    private String renderStringAtPosReplace(String text) {
        return FancyFormatting2.Companion.replaceString(text, false);
    }

    @ModifyVariable(method = "getStringWidth", at = @At("HEAD"), argsOnly = true)
    private String getStringWidthReplace(String text) {
        return FancyFormatting2.Companion.replaceString(text, FormattingConfig.fancyRendering());
    }
}
