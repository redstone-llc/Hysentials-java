package llc.redstone.hysentials.mixin.fancyformatting;

import llc.redstone.hysentials.config.hysentialmods.FormattingConfig;
import llc.redstone.hysentials.handlers.imageicons.ImageIcon;
import llc.redstone.hysentials.renderer.text.FancyFormatting2;
import llc.redstone.hysentials.renderer.text.Format;
import llc.redstone.hysentials.renderer.text.Number;
import llc.redstone.hysentials.util.ImageIconRenderer;
import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Locale;
import java.util.Random;

@Mixin(value = FontRenderer.class, priority = 1004)
public abstract class ImageIconsMixin {
    @Shadow
    protected abstract void doDraw(float f);

    @Shadow
    protected abstract float renderChar(char ch, boolean italic);

    @Shadow
    private boolean unicodeFlag;
    @Shadow
    public Random fontRandom;

    @Shadow
    public abstract int getCharWidth(char character);

    @Shadow
    private float green;
    @Shadow
    private float blue;
    @Shadow
    private float red;
    @Shadow
    private boolean italicStyle;
    @Shadow
    private boolean underlineStyle;
    @Shadow
    private boolean strikethroughStyle;
    @Shadow
    private boolean boldStyle;
    @Shadow
    private boolean randomStyle;
    @Shadow
    private int[] colorCode;
    @Shadow
    private int textColor;
    @Shadow
    private float alpha;
    @Shadow
    protected float posX;
    @Shadow
    protected float posY;
    @Shadow
    protected abstract void resetStyles();
    @Shadow
    protected abstract void setColor(float r, float g, float b, float a);

    @Inject(method = "renderStringAtPos", at = @At(value = "HEAD"), cancellable = true)
    private void renderStringAtPosReplace(String text, boolean shadow, CallbackInfo ci) {
        if (!FormattingConfig.fancyRendering()) {
            return;
        }
        List<Format> formats = FancyFormatting2.Companion.getFormats(text, false);
        for (Format format : formats) {
            switch (format.getType()) {
                case STRING:
                    hysentials$renderStringAtPos(format.getValue().toString(), shadow);
                    break;
                case HEX: {
                    int hex = (int) format.getValue();
                    resetStyles();
                    if (shadow) {
                        hex = (hex & 16579836) >> 2 | hex & -16777216;
                    }
                    setColor((float) (hex >> 16) / 255.0F, (float) (hex >> 8 & 255) / 255.0F, (float) (hex & 255) / 255.0F, alpha);
                    break;
                }
                case IMAGE_ICON: {
                    ImageIcon icon = (ImageIcon) format.getValue();
                    float y = this.posY - 1;
                    float positionAdd = icon.renderImage(this.posX, y, shadow, textColor, alpha);
                    if (positionAdd > 0) {
                        this.posX += positionAdd;
                    }
                    break;
                }
                case NUMBER: {
                    Number number = (Number) format.getValue();
                    float y = this.posY - 1;
                    float positionAdd = ImageIconRenderer.renderNumberedString(number.getNumber(), number.getHex(), this.posX, y, textColor, shadow, alpha);
                    if (positionAdd > 0) {
                        this.posX += positionAdd;
                    }
                    break;
                }
                default: {
                    return;
                }
            }
        }
        ci.cancel();
    }

    @Unique
    private void hysentials$renderStringAtPos(String text, boolean shadow) {
        for(int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);
            int i1;
            int j1;
            if (c0 == 167 && i + 1 < text.length()) {
                i1 = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if (i1 < 16) {
                    resetStyles();
                    if (i1 < 0 || i1 > 15) {
                        i1 = 15;
                    }

                    if (shadow) {
                        i1 += 16;
                    }

                    j1 = this.colorCode[i1];
                    this.textColor = j1;
                    this.setColor((float)(j1 >> 16) / 255.0F, (float)(j1 >> 8 & 255) / 255.0F, (float)(j1 & 255) / 255.0F, this.alpha);
                } else if (i1 == 16) {
                    this.randomStyle = true;
                } else if (i1 == 17) {
                    this.boldStyle = true;
                } else if (i1 == 18) {
                    this.strikethroughStyle = true;
                } else if (i1 == 19) {
                    this.underlineStyle = true;
                } else if (i1 == 20) {
                    this.italicStyle = true;
                } else if (i1 == 21) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    this.setColor(this.red, this.blue, this.green, this.alpha);
                }

                ++i;
            } else {
                i1 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(c0);
                if (this.randomStyle && i1 != -1) {
                    j1 = this.getCharWidth(c0);

                    char c1;
                    do {
                        i1 = this.fontRandom.nextInt("ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".length());
                        c1 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".charAt(i1);
                    } while(j1 != this.getCharWidth(c1));

                    c0 = c1;
                }

                float f1 = i1 != -1 && !this.unicodeFlag ? 1.0F : 0.5F;
                boolean flag = (c0 == 0 || i1 == -1 || this.unicodeFlag) && shadow;
                if (flag) {
                    this.posX -= f1;
                    this.posY -= f1;
                }

                float f = this.renderChar(c0, this.italicStyle);
                if (flag) {
                    this.posX += f1;
                    this.posY += f1;
                }

                if (this.boldStyle) {
                    this.posX += f1;
                    if (flag) {
                        this.posX -= f1;
                        this.posY -= f1;
                    }

                    this.renderChar(c0, this.italicStyle);
                    this.posX -= f1;
                    if (flag) {
                        this.posX += f1;
                        this.posY += f1;
                    }

                    ++f;
                }

                this.doDraw(f);
            }
        }
    }

}
