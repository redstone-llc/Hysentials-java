package llc.redstone.hysentials.util;

import cc.polyfrost.oneconfig.renderer.NanoVGHelper;
import cc.polyfrost.oneconfig.renderer.font.Font;

import java.util.Locale;
import java.util.Random;

public class NVGUtils {
    private static NanoVGHelper nvg = NanoVGHelper.INSTANCE;
    private static final int[] colorCodes = new int[32];
    static {
        for(int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i >> 0 & 1) * 170 + j;
            if (i == 6) {
                k += 85;
            }
            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }
            colorCodes[i] = (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
        }
    }
    private static float posX;
    private static float posY;
    private static Random fontRandom = new Random();

    private static boolean isBold = false;
    private static boolean isItalic = false;
    private static boolean isUnderlined = false;
    private static boolean isStrikethrough = false;
    private static boolean isObfuscated = false;
    private static boolean isUnicode = false;
    private static int color = 0xFFFFFF;

    public static void drawText(long vg, String text, float x, float y, float size, Font font, boolean shadow) {
        posX = x;
        posY = y;
        for (int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);
            int i1;
            float j1;
            int textColor = color;
            if (c0 == 167 && i + 1 < text.length()) {
                i1 = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if (i1 < 16) {
                    isBold = false;
                    isItalic = false;
                    isUnderlined = false;
                    isStrikethrough = false;
                    isObfuscated = false;

                    if (i1 < 0 || i1 > 15) {
                        i1 = 15;
                    }

                    if (shadow) {
                        i1 += 16;
                    }

                    j1 = colorCodes[i1];
                    textColor = Math.round(j1);
                    color = textColor;
//                    nvg.color(vg, textColor);
                } else if (i1 == 16) {
                    isObfuscated = true;
                } else if (i1 == 17) {
                    isBold = true;
                } else if (i1 == 18) {
                    isStrikethrough = true;
                } else if (i1 == 19) {
                    isUnderlined = true;
                } else if (i1 == 20) {
                    isItalic = true;
                } else if (i1 == 21) {
                    isBold = false;
                    isItalic = false;
                    isUnderlined = false;
                    isStrikethrough = false;
                    isObfuscated = false;
                    textColor = 0xFFFFFF;
                    color = textColor;
//                    nvg.color(vg, textColor);
                }

                ++i;
            } else {
                i1 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(c0);
                if (isObfuscated &&  i1 != -1) {
                    j1 = nvg.getTextWidth(vg, c0 + "", size, font);

                    char c1;
                    do {
                        i1 = fontRandom.nextInt("ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".length());
                        c1 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".charAt(i1);
                    } while (j1 != nvg.getTextWidth(vg, c1 + "", size, font));
                    c0 = c1;
                }

                float f1 = i1 != -1 && !isUnicode ? 1.0F : 0.5F;
                boolean flag = (c0 == 0 || i1 == -1 || isUnicode) && shadow;
                if (flag) {
                    posX -= f1;
                    posY -= f1;
                }
                nvg.drawText(vg, c0 + "", posX, posY, textColor, size, font);
                float f = nvg.getTextWidth(vg, c0 + "", size, font);
                if (flag) {
                    posX += f1;
                    posY += f1;
                }

                if (isBold) {
                    posX += f1;
                    if (flag) {
                        posX -= f1;
                        posY -= f1;
                    }

                    nvg.drawText(vg, c0 + "", posX, posY, textColor, size, font);
                    posX -= f1;
                    if (flag) {
                        posX += f1;
                        posY += f1;
                    }

                    ++f;
                }
                posX += (float)((int)f);
            }
        }
    }
}
