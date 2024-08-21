package llc.redstone.hysentials.mixin.fancyformatting;

import llc.redstone.hysentials.handlers.imageicons.ImageIcon;
import llc.redstone.hysentials.renderer.text.FancyFormattingKt;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = FontRenderer.class, priority = 1004)
public abstract class ImageIconsMixin {
    @Shadow
    private int textColor;
    @Shadow
    private float alpha;
    @Shadow
    protected float posX;
    @Shadow
    protected float posY;

    @Unique
    private String hysentials$text = null;
    @Unique Boolean hysentials$shadow = null;

    @Unique
    List<Character> hysentials$renderedChars = null;

    @Shadow
    protected abstract float renderChar(char ch, boolean italic);

    @Shadow
    protected abstract void doDraw(float f);

    @ModifyArg(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Ljava/lang/String;charAt(I)C"))
    private int renderStringAtPosReplace(int index) {
        try {
            char c0 = hysentials$text.charAt(index);
            for (ImageIcon icon : ImageIcon.imageIcons.values()) {
                if (icon.replacement == null || icon.replacement.isEmpty()) continue;
                if (icon.replacement.charAt(0) == c0) {
                    System.out.println("Found replacement for " + c0);
                    hysentials$renderedChars = new ArrayList<>();
                    hysentials$renderedChars.add(c0);
                    break;
                }
                if (hysentials$renderedChars != null && icon.replacement.charAt(hysentials$renderedChars.size()) == c0) {
                    hysentials$renderedChars.add(c0);
                    if (hysentials$renderedChars.size() == icon.replacement.length()) {
                        hysentials$renderedChars = null;
                        float f = icon.renderImage(posX, posY, hysentials$shadow, textColor, alpha);
                        doDraw(f);
                        break;
                    }
                } else {
                    hysentials$renderedChars = null;
                    hysentials$renderChars();
                }
            }
        } catch (Exception ignored) {}
        return index;
    }

    @Inject(method = "renderChar", at = @At("HEAD"), cancellable = true)
    private void renderCharReplace(char ch, boolean italic, CallbackInfoReturnable<Float> cir) {
        if (hysentials$renderedChars != null) {
            cir.cancel();
        }
    }

    @Unique
    private void hysentials$renderChars() {
        if (hysentials$renderedChars == null) return;
        for (char c : hysentials$renderedChars) {
            float f = this.renderChar(c, false);
            doDraw(f);
        }
    }


    //Is there a better way to do these two?
    @ModifyVariable(method = "renderStringAtPos", at = @At("HEAD"), argsOnly = true)
    private String renderStringAtPosReplace(String text) {
        hysentials$text = FancyFormattingKt.replaceString(text);
        return FancyFormattingKt.replaceString(text);
    }
    // Is there a better way to do these two?
    @ModifyVariable(method = "renderStringAtPos", at = @At("HEAD"), argsOnly = true, index = 2)
    private boolean renderStringAtPosReplace2(boolean value) {
        hysentials$shadow = value;
        return value;
    }

    @Inject(method = "renderStringAtPos", at = @At("RETURN"))
    private void renderStringAtPosReplaceReturn(String text, boolean shadow, CallbackInfo ci) {
        hysentials$text = null;
        hysentials$shadow = null;
    }

}
