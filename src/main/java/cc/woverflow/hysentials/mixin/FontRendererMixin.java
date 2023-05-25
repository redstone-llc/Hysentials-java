package cc.woverflow.hysentials.mixin;

import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.hook.FontRendererAcessor;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Random;

@Mixin(value = FontRenderer.class, priority = Integer.MIN_VALUE)
public abstract class FontRendererMixin implements FontRendererAcessor {

    @Shadow
    private boolean randomStyle;

    @Shadow
    private boolean boldStyle;

    @Shadow
    private boolean strikethroughStyle;

    @Shadow
    private boolean underlineStyle;

    @Shadow
    private boolean italicStyle;

    @Shadow
    private int[] colorCode;

    @Shadow
    private int textColor;

    @Shadow
    protected abstract void setColor(float r, float g, float b, float a);

    @Shadow
    private float red;

    @Shadow
    private float blue;

    @Shadow
    private float green;

    @Shadow
    private float alpha;

    @Shadow
    public abstract int getCharWidth(char character);

    @Shadow
    public Random fontRandom;

    @Shadow
    private boolean unicodeFlag;

    @Shadow
    protected float posX;

    @Shadow
    protected float posY;

    @Shadow
    public abstract float renderChar(char ch, boolean italic);

    @Shadow
    protected abstract void doDraw(float f);

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getStringWidth(String text) {
        if (Hysentials.INSTANCE != null && Hysentials.INSTANCE.imageIconRenderer != null) {
            return Hysentials.INSTANCE.imageIconRenderer.getStringWidth(text);
        } else {
            if (text == null) {
                return 0;
            } else {
                int i = 0;
                boolean flag = false;

                for(int j = 0; j < text.length(); ++j) {
                    char c0 = text.charAt(j);
                    int k = this.getCharWidth(c0);
                    if (k < 0 && j < text.length() - 1) {
                        ++j;
                        c0 = text.charAt(j);
                        if (c0 != 'l' && c0 != 'L') {
                            if (c0 == 'r' || c0 == 'R') {
                                flag = false;
                            }
                        } else {
                            flag = true;
                        }

                        k = 0;
                    }

                    i += k;
                    if (flag && k > 0) {
                        ++i;
                    }
                }

                return i;
            }
        }
    }

    @Override
    public int getTextColor() {
        return textColor;
    }

    @Override
    public void setTextColor(int color) {
        this.textColor = color;
    }

    @Override
    public void setBoldStyle(boolean boldStyle) {
        this.boldStyle = boldStyle;
    }

    @Override
    public void setItalicStyle(boolean italicStyle) {
        this.italicStyle = italicStyle;
    }

    @Override
    public void setRandomStyle(boolean randomStyle) {
        this.randomStyle = randomStyle;
    }

    @Override
    public void setUnderlineStyle(boolean underlineStyle) {
        this.underlineStyle = underlineStyle;
    }

    @Override
    public void setStrikethroughStyle(boolean strikethroughStyle) {
        this.strikethroughStyle = strikethroughStyle;
    }

    @Override
    public boolean isBoldStyle() {
        return boldStyle;
    }

    @Override
    public boolean isItalicStyle() {
        return italicStyle;
    }

    @Override
    public boolean isRandomStyle() {
        return randomStyle;
    }

    @Override
    public boolean isStrikethroughStyle() {
        return strikethroughStyle;
    }

    @Override
    public boolean isUnderlineStyle() {
        return underlineStyle;
    }

    public boolean isUnicodeFlag() {
        return unicodeFlag;
    }

    @Override
    public int[] getColorCode() {
        return colorCode;
    }

    @Override
    public float red() {
        return red;
    }

    @Override
    public float green() {
        return green;
    }

    @Override
    public float blue() {
        return blue;
    }

    @Override
    public float alpha() {
        return alpha;
    }

    @Override
    public void setRed(float red) {
        this.red = red;
    }

    @Override
    public void setGreen(float green) {
        this.green = green;
    }

    @Override
    public void setBlue(float blue) {
        this.blue = blue;
    }

    @Override
    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }
}
