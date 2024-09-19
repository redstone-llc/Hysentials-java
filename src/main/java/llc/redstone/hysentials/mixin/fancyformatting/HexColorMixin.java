package llc.redstone.hysentials.mixin.fancyformatting;

import llc.redstone.hysentials.config.hysentialmods.FormattingConfig;
import llc.redstone.hysentials.renderer.text.FancyFormattingKt;
import llc.redstone.hysentials.util.ImageIconRenderer;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = FontRenderer.class, priority = 1004)
public abstract class HexColorMixin {
    @Shadow
    private int textColor;
    @Shadow
    private float alpha;
    @Shadow
    protected float posX;
    @Shadow
    protected float posY;

    @Shadow
    protected abstract float renderChar(char ch, boolean italic);

    @Shadow
    protected abstract void doDraw(float f);


    @Shadow
    protected abstract void setColor(float r, float g, float b, float a);

    @Unique
    Boolean hysentials2$shadow = null;
    @Unique
    Boolean hysentials2$working = false;
    @Unique
    Boolean hysentials2$onemore = false;
    @Unique
    String hysentials2$current = null;

    @ModifyVariable(method = "renderStringAtPos", at = @At(value = "INVOKE", target = "Ljava/lang/String;charAt(I)C", ordinal = 0, shift = At.Shift.AFTER))
    private int doSomeHexify(int index) {
        if (!FormattingConfig.hexRendering()) return index;
        if (FancyFormattingKt.getCurrentText() == null) return index;
        String text = FancyFormattingKt.getCurrentText();
        char c0 = text.charAt(index);

        if (c0 == '<' && text.substring(index).length() > 8 && text.charAt(index + 1) == '#' && text.charAt(index + 8) == '>') {
            hysentials2$current = "<#";
            hysentials2$working = true;
            return index;
        }
        if (!hysentials2$working || hysentials2$current == null) return index;
        if (hysentials$isHex(c0) || c0 == '>') {
            hysentials2$current += c0;
        }

        if (hysentials2$current.length() == 9 && hysentials2$current.charAt(8) == '>') {
            String hex = hysentials2$current.substring(2, 8);
            if (!ImageIconRenderer.checkIfHexadecimal(hex)) return index;
            int color = Integer.parseInt(hex, 16);
            if (hysentials2$shadow) {
                color = (color & 16579836) >> 2 | color & -16777216;
            }
            textColor = color;
            this.setColor((float)(color >> 16) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F, this.alpha);

            hysentials2$onemore = true;
            resetHysentialsState();
            return index;
        }
        return index;
    }

    @Unique
    private void resetHysentialsState() {
        hysentials2$working = false;
        hysentials2$current = null;
    }

    @Unique
    private boolean hysentials$isHex(char ch) {
        return ('0' <= ch && ch <= '9') ||
            ('a' <= ch && ch <= 'f') ||
            ('A' <= ch && ch <= 'F');
    }

    @ModifyVariable(method = "renderStringAtPos", at = @At("HEAD"), argsOnly = true, index = 2)
    private boolean renderStringAtPosReplace2(boolean value) {
        hysentials2$shadow = value;
        return value;
    }

    @Inject(method = "renderChar", at = @At("HEAD"), cancellable = true)
    private void renderCharReplace2(char ch, boolean italic, CallbackInfoReturnable<Float> cir) {
        if (hysentials2$working || hysentials2$onemore) {
            cir.setReturnValue(0f);
            if (hysentials2$onemore) hysentials2$onemore = false;
        }
    }
}
