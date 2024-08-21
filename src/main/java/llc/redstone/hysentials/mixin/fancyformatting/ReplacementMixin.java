package llc.redstone.hysentials.mixin.fancyformatting;

import llc.redstone.hysentials.renderer.text.FancyFormattingKt;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = FontRenderer.class, priority = 1005)
public class ReplacementMixin {
    @ModifyVariable(method = "getStringWidth", at = @At("HEAD"), argsOnly = true)
    private String getStringWidthReplace(String text) {
        return FancyFormattingKt.replaceString(text);
    }
}
