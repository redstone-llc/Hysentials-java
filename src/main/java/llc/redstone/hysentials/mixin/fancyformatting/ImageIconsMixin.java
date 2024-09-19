package llc.redstone.hysentials.mixin.fancyformatting;

import llc.redstone.hysentials.config.hysentialmods.FormattingConfig;
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
    Boolean hysentials$shadow = null;

    @Unique
    String hysentials$renderedChars = null;
    @Unique
    boolean hysentials$working = false;
    @Unique
    boolean hysentials$onemore = false;
    @Unique
    ImageIcon hysentials$currentIcon = null;

    @Shadow
    protected abstract float renderChar(char ch, boolean italic);

    @Shadow
    protected abstract void doDraw(float f);

    @ModifyVariable(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Ljava/lang/String;charAt(I)C", ordinal = 0, shift = At.Shift.AFTER))
    private int renderStringAtPosReplace(int index) {
        try {
            if (!FormattingConfig.fancyRendering()) {
                resetHysentialsState();
                return index;
            }
            if (FancyFormattingKt.getCurrentText() == null) return index;
            String text = FancyFormattingKt.getCurrentText();
            char c0 = text.charAt(index);

            if (!hysentials$working) {
                for (ImageIcon icon : ImageIcon.imageIcons.values()) {
                    if (icon.firstChar == c0) {
                        hysentials$currentIcon = icon;
                        hysentials$renderedChars = "";
                        hysentials$renderedChars += c0;
                        System.out.println("How are you being called?");
                        hysentials$working = true;
                        return index;
                    }
                }
            }

            if (!hysentials$working || hysentials$currentIcon == null || hysentials$renderedChars == null) return index;

            ImageIcon icon = hysentials$currentIcon;

            if (icon.replacement.isEmpty()) {
                resetHysentialsState();
                return index;
            }

            if (icon.replacement.length() <= hysentials$renderedChars.length()) {
                resetHysentialsState();
                return index;
            }

            if (icon.replacement.charAt(hysentials$renderedChars.length()) == c0) {
                hysentials$renderedChars += c0;
                String current = hysentials$renderedChars;

                if (current.equals(icon.replacement)) {
                    resetHysentialsState();
                    hysentials$onemore = true;
                    float renderedWidth = icon.renderImage(posX, posY, hysentials$shadow, textColor, alpha);
                    doDraw(renderedWidth);
                }
            } else {
                hysentials$renderChars();
                resetHysentialsState();
            }

        } catch (Exception e) {
            System.err.println("Error during rendering string replacement: " + e.getMessage());
            e.printStackTrace();
            hysentials$renderChars();
            resetHysentialsState();
            hysentials$onemore = false;
        }

        return index;
    }

    // Helper method to reset state
    private void resetHysentialsState() {
        hysentials$working = false;
        hysentials$renderedChars = null;
        hysentials$currentIcon = null;
    }

    @Inject(method = "renderChar", at = @At("HEAD"), cancellable = true)
    private void renderCharReplace(char ch, boolean italic, CallbackInfoReturnable<Float> cir) {
        if (hysentials$working || hysentials$onemore) {
            cir.setReturnValue(0f);
            System.out.println("onemore: " + hysentials$onemore + " working: " + hysentials$working);
            if (hysentials$onemore) {
                hysentials$onemore = false;
                resetHysentialsState();
                hysentials$working = false;
            }
        }
    }

    @Unique
    private void hysentials$renderChars() {
        if (!hysentials$working) return;
        for (char c : hysentials$renderedChars.toCharArray()) {
            float f = this.renderChar(c, false);
            doDraw(f);
        }
    }


    // Is there a better way to do these two?
    @ModifyVariable(method = "renderStringAtPos", at = @At("HEAD"), argsOnly = true, index = 2)
    private boolean renderStringAtPosReplace2(boolean value) {
        hysentials$shadow = value;
        return value;
    }

    @Inject(method = "renderStringAtPos", at = @At("RETURN"))
    private void renderStringAtPosReplaceReturn(String text, boolean shadow, CallbackInfo ci) {
        FancyFormattingKt.setCurrentText(null);
        hysentials$shadow = null;
    }

}
