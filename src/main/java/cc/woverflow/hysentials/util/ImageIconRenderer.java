package cc.woverflow.hysentials.util;

import cc.woverflow.hysentials.handlers.imageicons.ImageIcon;
import cc.woverflow.hysentials.handlers.redworks.BwRanks;
import cc.woverflow.hysentials.hook.FontRendererAcessor;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.Sys;

import java.util.ConcurrentModificationException;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;

import static cc.woverflow.hysentials.handlers.imageicons.ImageIcon.stringPattern;

public class ImageIconRenderer extends FontRenderer {
    FontRendererAcessor accessor = (FontRendererAcessor) this;

    public ImageIconRenderer() {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
        super.onResourceManagerReload(null);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        super.onResourceManagerReload(resourceManager);
        ImageIcon.reloadIcons();
    }

    private void renderStringAtPosA(String text, boolean shadow) {
        try {
            if (BwRanks.replacementMap.size() > 0) {
                String finalText = text.replace("§r", "");
                for (Map.Entry<String, String> entry : BwRanks.replacementMap.entrySet()) {
                    if (finalText.startsWith(entry.getKey())) {
                        text = text.replace(entry.getKey(), entry.getValue());
                    }
                }
            }
        } catch (ConcurrentModificationException ignored) {
        }
        for (int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);
            int i1;
            int j1;
            int textColor = accessor.getTextColor();
            String sub = text.substring(i + 1);
            if (c0 == ':' && sub.contains(":") && sub.substring(0, sub.indexOf(":")).matches("[a-z_\\-0-9]+")) {
                Matcher matcher = stringPattern.matcher(text);
                if (matcher.find(i)) {
                    String str = matcher.group(1);
                    if (str != null) {
                        ImageIcon icon = ImageIcon.getIcon(str);
                        if (icon != null) {
                            float y = this.posY - 1;
                            this.posX += icon.renderImage(this.posX, y);
                            i += str.length() + 1;
                            continue;
                        }
                    }
                }
            }
            if (c0 == 60 && i + 8 < text.length()) {
                String hex = text.substring(i + 2, i + 8);
                if (text.charAt(i + 1) == '#' && hex.matches("[0-9a-fA-F]+")) {
                    accessor.setRandomStyle(false);
                    accessor.setBoldStyle(false);
                    accessor.setStrikethroughStyle(false);
                    accessor.setUnderlineStyle(false);
                    accessor.setItalicStyle(false);
                    textColor = Integer.parseInt(hex, 16);
                    if (shadow) {
                        textColor = (textColor & 16579836) >> 2 | textColor & -16777216;
                    }
                    accessor.setTextColor(textColor);
                    this.setColor((float) (textColor >> 16) / 255.0F, (float) (textColor >> 8 & 255) / 255.0F, (float) (textColor & 255) / 255.0F, accessor.alpha());
                    i += 8;
                    continue;
                }
            }
            if (c0 == 167 && i + 1 < text.length()) {
                i1 = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if (i1 < 16) {
                    accessor.setRandomStyle(false);
                    accessor.setBoldStyle(false);
                    accessor.setStrikethroughStyle(false);
                    accessor.setUnderlineStyle(false);
                    accessor.setItalicStyle(false);
                    if (i1 < 0 || i1 > 15) {
                        i1 = 15;
                    }

                    if (shadow) {
                        i1 += 16;
                    }

                    j1 = accessor.getColorCode()[i1];
                    textColor = j1;
                    accessor.setTextColor(textColor);
                    this.setColor((float) (j1 >> 16) / 255.0F, (float) (j1 >> 8 & 255) / 255.0F, (float) (j1 & 255) / 255.0F, accessor.alpha());
                } else if (i1 == 16) {
                    accessor.setRandomStyle(true);
                } else if (i1 == 17) {
                    accessor.setBoldStyle(true);
                } else if (i1 == 18) {
                    accessor.setStrikethroughStyle(true);
                } else if (i1 == 19) {
                    accessor.setUnderlineStyle(true);
                } else if (i1 == 20) {
                    accessor.setItalicStyle(true);
                } else if (i1 == 21) {
                    accessor.setRandomStyle(false);
                    accessor.setBoldStyle(false);
                    accessor.setStrikethroughStyle(false);
                    accessor.setUnderlineStyle(false);
                    accessor.setItalicStyle(false);
                    textColor = -1;
                    accessor.setTextColor(textColor);
                    this.setColor(accessor.red(), accessor.blue(), accessor.green(), accessor.alpha());
                }

                ++i;
            } else {
                i1 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(c0);
                if (accessor.isRandomStyle() && i1 != -1) {
                    j1 = this.getCharWidth(c0);

                    char c1;
                    do {
                        i1 = this.fontRandom.nextInt("ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".length());
                        c1 = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".charAt(i1);
                    } while (j1 != this.getCharWidth(c1));

                    c0 = c1;
                }

                float f1 = i1 != -1 && !accessor.isUnicodeFlag() ? 1.0F : 0.5F;
                boolean flag = (c0 == 0 || i1 == -1 || accessor.isUnicodeFlag()) && shadow;
                if (flag) {
                    this.posX -= f1;
                    this.posY -= f1;
                }

                float f = renderChar(c0, accessor.isItalicStyle());
                if (flag) {
                    this.posX += f1;
                    this.posY += f1;
                }

                if (accessor.isBoldStyle()) {
                    this.posX += f1;
                    if (flag) {
                        this.posX -= f1;
                        this.posY -= f1;
                    }

                    renderChar(c0, accessor.isItalicStyle());
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


    public int getStringWidth(String text) {
        try {
            if (BwRanks.replacementMap.size() > 0) {
                String finalText = text.replace("§r", "");
                for (Map.Entry<String, String> entry : BwRanks.replacementMap.entrySet()) {
                    if (finalText.startsWith(entry.getKey())) {
                        text = text.replace(entry.getKey(), entry.getValue());
                    }
                }
            }
        } catch (ConcurrentModificationException ignored) {
        }
        if (text == null) {
            return 0;
        } else {
            int i = 0;
            boolean flag = false;

            for (int j = 0; j < text.length(); ++j) {
                char c0 = text.charAt(j);
                try {
                    String sub = text.substring(j + 1);
                    if (c0 == ':' && sub.contains(":") && sub.substring(0, sub.indexOf(":")).matches("[a-z_\\-0-9]+")) {
                        Matcher matcher = stringPattern.matcher(text);
                        if (matcher.find(j)) {
                            String str = matcher.group(1);
                            if (str != null) {
                                ImageIcon icon = ImageIcon.getIcon(str);
                                if (icon != null) {
                                    j += str.length() + 2;
                                    i += icon.getWidth() + 4;
                                    continue;
                                }
                            }
                        }
                    }
                    if (c0 == 60 && j + 7 < text.length()) {
                        String s = text.substring(j, j + 9);
                        if (s.matches("<#([0-9a-fA-F]){6}>")) {
                            j += 7;
                            i -= 5;
                            continue;
                        }
                    }
                } catch (Exception ignored) {
                }
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

    public int getCharWidth(char character) {
        if (character == 167) {
            return -1;
        } else if (character == ' ') {
            return 4;
        } else {
            int i = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(character);
            if (character > 0 && i != -1 && !getUnicodeFlag()) {
                return this.charWidth[i];
            } else if (this.glyphWidth[character] != 0) {
                int j = this.glyphWidth[character] >>> 4;
                int k = this.glyphWidth[character] & 15;
                ++k;
                return (k - j) / 2 + 1;
            } else {
                return 0;
            }
        }
    }

    private float renderChar(char ch, boolean italic) {
        if (ch == ' ') {
            return 4.0F;
        } else {
            int i = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(ch);
            return i != -1 && !getUnicodeFlag() ? this.renderDefaultChar(i, italic) : this.renderUnicodeChar(ch, italic);
        }
    }

    public void resetStyles() {
        accessor.setRandomStyle(false);
        accessor.setBoldStyle(false);
        accessor.setStrikethroughStyle(false);
        accessor.setUnderlineStyle(false);
        accessor.setItalicStyle(false);
    }

    public int drawStringWithShadow(String text, float x, float y, int color) {
        return this.drawString(text, x, y, color, true);
    }

    public int drawString(String text, int x, int y, int color) {
        return this.drawString(text, (float) x, (float) y, color, false);
    }

    public int drawString(String text, float x, float y, int color) {
        return this.drawString(text, x, y, color, false);
    }

    public int drawString(String text, float x, float y, int color, boolean dropShadow) {
        this.enableAlpha();
        this.resetStyles();
        int i;
        if (dropShadow) {
            i = this.renderString(text, x + 1.0F, y + 1.0F, color, true);
            i = Math.max(i, this.renderString(text, x, y, color, false));
        } else {
            i = this.renderString(text, x, y, color, false);
        }

        return i;
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        drawString(text, x - ((int) getStringWidth(text) >> 1), y, color);
    }

    private int renderString(String text, float x, float y, int color, boolean dropShadow) {
        if (text == null) {
            return 0;
        } else {
            if (this.getBidiFlag()) {
                text = this.bidiReorder(text);
            }

            if ((color & -67108864) == 0) {
                color |= -16777216;
            }

            if (dropShadow) {
                color = (color & 16579836) >> 2 | color & -16777216;
            }

            accessor.setRed((float) (color >> 16 & 255) / 255.0F);
            accessor.setBlue((float) (color >> 8 & 255) / 255.0F);
            accessor.setGreen((float) (color & 255) / 255.0F);
            this.accessor.setAlpha((float) (color >> 24 & 255) / 255.0F);
            this.setColor(accessor.red(), accessor.blue(), accessor.green(), accessor.alpha());
            this.posX = x;
            this.posY = y;
            this.renderStringAtPosA(text, dropShadow);
            return (int) this.posX;
        }
    }

    private String bidiReorder(String text) {
        try {
            Bidi bidi = new Bidi((new ArabicShaping(8)).shape(text), 127);
            bidi.setReorderingMode(0);
            return bidi.writeReordered(2);
        } catch (ArabicShapingException var3) {
            return text;
        }
    }

    private static boolean isFormatColor(char colorChar) {
        return colorChar >= '0' && colorChar <= '9' || colorChar >= 'a' && colorChar <= 'f' || colorChar >= 'A' && colorChar <= 'F';
    }

    private static boolean isFormatSpecial(char formatChar) {
        return formatChar >= 'k' && formatChar <= 'o' || formatChar >= 'K' && formatChar <= 'O' || formatChar == 'r' || formatChar == 'R';
    }

    public static String getFormatFromString(String text) {
        String s = "";
        int j = text.length();

        for (int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);
            int i1;
            int j1;
            if (c0 == 60 && i + 8 < text.length()) {
                String hex = text.substring(i + 2, i + 8);
                if (text.charAt(i + 1) == '#' && hex.matches("[0-9a-fA-F]+")) {
                    s = "<#" + hex + ">";
                }
                i += 8;
                continue;
            }
            if (c0 == 167 && i + 1 < text.length()) {
                char c1 = text.charAt(i + 1);
                i1 = "0123456789abcdefklmnor".indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if (isFormatColor(c1)) {
                    s = "§" + c1;
                } else if (isFormatSpecial(c1)) {
                    s = s + "§" + c1;
                }
            }
        }

//        int i = -1;
//
//        while ((i = text.indexOf(167, i + 1)) != -1 || (i = text.indexOf('<', i + 1)) != -1) {
//            if ((i = text.indexOf('<', i+ 7)) != -1) {
//                String hex = text.substring(i + 2, i + 8);
//                if (text.charAt(i + 1) == '#' && hex.matches("[0-9a-fA-F]+")) {
//                    s = "<#" + hex + ">";
//                }
//                continue;
//            }
//            if (i < j - 1) {
//                char c0 = text.charAt(i + 1);
//                if (isFormatColor(c0)) {
//                    s = "§" + c0;
//                } else if (isFormatSpecial(c0)) {
//                    s = s + "§" + c0;
//                }
//            }
//        }

        return s;
    }

    public String trimStringToWidth(String text, int width, boolean reverse) {
        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        int j = reverse ? text.length() - 1 : 0;
        int k = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;
//        System.out.println("Text: " + text + " With max: " + width);

        for (int l = j; l >= 0 && l < text.length() && i < width; l += k) {
            char c0 = text.charAt(l);
            try {
                String sub = text.substring(l + 1);
                if (c0 == ':' && sub.contains(":") && sub.substring(0, sub.indexOf(":")).matches("[a-z_\\-0-9]+")) {
                    Matcher matcher = stringPattern.matcher(text);
                    if (matcher.find(l)) {
                        String str = matcher.group(1);
//                        System.out.println(str);
                        if (str != null) {
                            ImageIcon icon = ImageIcon.getIcon(str);
                            if (icon != null) {
                                l += (str.length() + 2)*k;
                                i += icon.getWidth() + 4;
                                if (i > width) {
                                    break;
                                }

                                if (reverse) {
                                    stringbuilder.insert(0, ":" + str + ":");
                                } else {
                                    stringbuilder.append(":" + str + ":");
                                }
                                continue;
                            }
                        }
                    }
                }
                if (c0 == 60 && l + 7 < text.length()) {
                    String s = text.substring(l, l + 9);
//                    System.out.println(s);
//                    System.out.println(text);
                    if (s.matches("<#([0-9a-fA-F]){6}>")) {
                        l += 8 * k;
//                        i += 5;
                        if (i > width) {
                            break;
                        }

                        if (reverse) {
                            stringbuilder.insert(0, s);
                        } else {
                            stringbuilder.append(s);
                        }
                        continue;
                    }
                }
            } catch (Exception ignored) {
            }
            int i1 = getCharWidth(c0);
            if (flag) {
                flag = false;
                if (c0 != 'l' && c0 != 'L') {
                    if (c0 == 'r' || c0 == 'R') {
                        flag1 = false;
                    }
                } else {
                    flag1 = true;
                }
            } else if (i1 < 0) {
                flag = true;
            } else {
                i += i1;
                if (flag1) {
                    ++i;
                }
            }

            if (i > width) {
                break;
            }

            if (reverse) {
                stringbuilder.insert(0, c0);
            } else {
                stringbuilder.append(c0);
            }
        }
//        System.out.println("Result: " + stringbuilder.toString());
        return stringbuilder.toString();
    }
}
