package llc.redstone.hysentials.util;

import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.cosmetic.CosmeticManager;
import llc.redstone.hysentials.handlers.imageicons.ImageIcon;
import llc.redstone.hysentials.hook.FontRendererAcessor;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.*;

import static llc.redstone.hysentials.util.C.toHex;

public class ImageIconRenderer extends FontRenderer {
    FontRendererAcessor accessor = (FontRendererAcessor) this;
    HysentialsFontRenderer mcFive;

    public ImageIconRenderer() {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
        super.onResourceManagerReload(null);
        mcFive = new HysentialsFontRenderer("Minecraft Five", 12f);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        super.onResourceManagerReload(resourceManager);
        ImageIcon.reloadIcons();
    }

    private void renderStringAtPosA(String text, boolean shadow) {
        UUID uuid = null;
        if (text.startsWith("§aHymojis: \n")) {
            uuid = UUID.fromString("ad80d7cf-8115-4e2a-b15d-e5cc0bf6a9a2");
        }
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
            uuid = (Minecraft.getMinecraft().thePlayer == null) ? null : Minecraft.getMinecraft().thePlayer.getUniqueID();
        }

        try {
//            if (BwRanks.replacementMap.size() > 0) {
//                String finalText = text.replace("§r", "");
//                for (Map.Entry<String, DuoVariable<UUID, String>> entry : BwRanks.replacementMap.entrySet()) {
//                    if (finalText.startsWith(entry.getKey())) {
//                        text = text.replace(entry.getKey(), entry.getValue().second);
//                        uuid = entry.getValue().first;
//                    }
//                }
//            }

            HashMap<String, String> allActiveReplacements = Hysentials.INSTANCE.getConfig().replaceConfig.getAllActiveReplacements();
            for (String key : allActiveReplacements.keySet()) {
                String finalText = text.replace("§r", "");
                String value = allActiveReplacements.get(key);
                if (value == null) {
                    continue;
                }
                if (value.isEmpty() || key.isEmpty()) {
                    continue;
                }
                value = value.replace("&", "§");
                key = key.replace("&", "§");
                if (Hysentials.INSTANCE.getConfig().replaceConfig.isRegexEnabled()) {
                    text = finalText.replaceAll(key, value);
                } else {
                    text = finalText.replace(key, value);
                }
            }

        } catch (ConcurrentModificationException ignored) {
        }
        boolean lookingForQuestionMark = false;
        for (int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);
            int i1;
            int j1;
            int textColor = accessor.getTextColor();

            String sub = text.substring(i + 1);
            //Image Icons
            if (c0 == ':' && sub.contains(":") && !sub.substring(0, sub.indexOf(":")).isEmpty()) {
                String str = sub.substring(0, sub.indexOf(":"));
                if (!str.isEmpty()) {
                    if (str.endsWith("?")) { //If the icon ends with a question mark then we don't render it
                        lookingForQuestionMark = true;
                    } else {
                        ImageIcon icon = ImageIcon.getIcon(str);
                        if (icon != null) {
                            float y = this.posY - 1;
                            int positionAdd = icon.renderImage(this.posX, y, shadow, textColor, uuid, accessor.alpha());
                            if (positionAdd > 0) {
                                this.posX += positionAdd;
                                i += str.length() + 1;
                                continue;
                            }
                        }
                    }
                }
            }
            if (c0 == 60 && i + 8 < text.length()) {
                String hex = text.substring(i + 2, i + 8);
                if (text.charAt(i + 1) == '#' && checkIfHexadecimal(hex)) {
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

            //Numbered strings
            // <{HexColor}:{Number}>
            if (c0 == 60 && sub.contains(":") && sub.contains(">")) {
                String hex = sub.substring(0, sub.indexOf(":"));
                if (checkIfHexadecimal(hex)) {
                    String num = sub.substring(sub.indexOf(":") + 1, sub.indexOf(">"));
                    if (num.matches("\\d+") && !num.isEmpty()) {
                        this.posX += renderNumberedString(num, hex, (int) this.posX, (int) this.posY, textColor, shadow);
                        i += hex.length() + num.length() + 2;
                        continue;
                    }
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
                if (lookingForQuestionMark && c0 == '?') {
                    lookingForQuestionMark = false;
                    continue;
                }
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

    private int renderNumberedString(String num, String hex, int x, int y, int oldColor, boolean dropShadow) {
        int bgColor = Integer.parseInt(hex, 16);
        if (dropShadow) {
            bgColor = (bgColor & 16579836) >> 2 | bgColor & -16777216;
        }
        Color c;
        if (bgColor < 0x7F7F7F) {
            c = Color.WHITE;
        } else {
            c = Color.DARK_GRAY;
        }
        int textColor = (int) (accessor.alpha() * 255f) << 24 | c.getRed() << 16 | c.getGreen() << 8 | c.getBlue();
        this.setColor((float) (bgColor >> 16) / 255.0F, (float) (bgColor >> 8 & 255) / 255.0F, (float) (bgColor & 255) / 255.0F, accessor.alpha());
        float width = mcFive.getWidth(num) + 2;
        Renderer.drawRect(x, y, width, 7);
        this.setColor((float) (oldColor >> 16) / 255.0F, (float) (oldColor >> 8 & 255) / 255.0F, (float) (oldColor & 255) / 255.0F, accessor.alpha());
        mcFive.drawString(num, x + 1, y, textColor);
        this.setColor((float) (oldColor >> 16) / 255.0F, (float) (oldColor >> 8 & 255) / 255.0F, (float) (oldColor & 255) / 255.0F, accessor.alpha());
        return (int) width;
    }

//    private float renderNumberedColorString(String fromFormat, float x, int y, int oldColor, boolean dropShadow) {
//        // <{bgColors}:{textColors}:{Number}>
//        float originalX = x;
//        String[] split = fromFormat.split(":");
//        if (split.length != 3) {
//            return 0;
//        }
//        String bgColors = split[0];
//        String textColors = split[1];
//        String num = split[2].replace(">", "");
//        if (bgColors.length() != textColors.length()) {
//            return 0;
//        }
//        for (int i = 0; i < bgColors.length(); i++) {
//            int adjustedI = i - 1;
//            int bgColor = Integer.parseInt(C.toHex("§" + bgColors.charAt(i), false), 16);
//            if (dropShadow) {
//                bgColor = (bgColor & 16579836) >> 2 | bgColor & -16777216;
//            }
//            String numChar = null;
//            if (i != 0 && i != bgColors.length() - 1) {
//                numChar = String.valueOf(num.charAt(adjustedI));
//            }
//            float width = (numChar == null) ? 1 : mcFive.getWidth(numChar) + 1;
//            this.setColor((float) (bgColor >> 16) / 255.0F, (float) (bgColor >> 8 & 255) / 255.0F, (float) (bgColor & 255) / 255.0F, accessor.alpha());
//            Renderer.drawRect(x, y, width, 7);
//            if (i != 0 && i != bgColors.length() - 1) {
//                int textColor = Integer.parseInt(C.toHex("§" + textColors.charAt(i), false), 16);
//                mcFive.drawString(numChar, x + 0.5f, y, textColor);
//            }
//            x += width;
//        }
//        this.setColor((float) (oldColor >> 16) / 255.0F, (float) (oldColor >> 8 & 255) / 255.0F, (float) (oldColor & 255) / 255.0F, accessor.alpha());
//        return x - originalX;
//    }

    private String trimStringNewline(String text) {
        while (text != null && text.endsWith("\n")) {
            text = text.substring(0, text.length() - 1);
        }

        return text;
    }

    public void drawSplitString(String str, int x, int y, int wrapWidth, boolean addShadow) {
        this.resetStyles();
        str = this.trimStringNewline(str);
        this.renderSplitString(str, x, y, wrapWidth, addShadow);
    }

    private void renderSplitString(String str, int x, int y, int wrapWidth, boolean addShadow) {
        for (Iterator var6 = this.listFormattedStringToWidth(str, wrapWidth).iterator(); var6.hasNext(); y += this.FONT_HEIGHT) {
            String s = (String) var6.next();
            this.renderStringAligned(s, x, y, wrapWidth, addShadow);
        }
    }

    private int renderStringAligned(String text, int x, int y, int width, boolean dropShadow) {
        if (getBidiFlag()) {
            int i = this.getStringWidth(this.bidiReorder(text));
            x = x + width - i;
        }

        return this.renderString(text, (float) x, (float) y, 0xFFFFFF, dropShadow);
    }

    public int getStringWidth(String text) {
        UUID uuid = null;
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
            uuid = (Minecraft.getMinecraft().thePlayer == null) ? null : Minecraft.getMinecraft().thePlayer.getUniqueID();
        }
        try {
            HashMap<String, String> allActiveReplacements = Hysentials.INSTANCE.getConfig().replaceConfig.getAllActiveReplacements();
            for (String key : allActiveReplacements.keySet()) {
                String finalText = text.replace("§r", "");
                String value = allActiveReplacements.get(key);
                if (value == null) {
                    continue;
                }
                if (value.isEmpty() || key.isEmpty()) {
                    continue;
                }
                value = value.replace("&", "§");
                key = key.replace("&", "§");
                if (Hysentials.INSTANCE.getConfig().replaceConfig.isRegexEnabled()) {
                    text = finalText.replaceAll(key, value);
                } else {
                    text = finalText.replace(key, value);
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
                    if (c0 == ':' && sub.contains(":") && !sub.substring(0, sub.indexOf(":")).isEmpty()) {
                        String str = sub.substring(0, sub.indexOf(":"));
                        if (!str.isEmpty()) {
                            if (str.endsWith("?")) {
                                str = str.substring(0, str.length() - 1);
                                j += str.length() + 2;
                                i += super.getStringWidth(str) + super.getStringWidth(":") * 2;
                                continue;
                            } else {
                                ImageIcon icon = ImageIcon.getIcon(str);
                                if (icon != null) {
                                    int width = icon.getWidth();
                                    int height = icon.getHeight();
                                    float scaledHeight = (float) 9 / height;
                                    int scaledWidth = (int) (width * scaledHeight);
                                    if (icon.emoji && uuid != null && CosmeticManager.hasCosmetic(uuid, "hymojis")) {
                                        j += str.length() + 1;
                                        i += scaledWidth;
                                        continue;
                                    } else if (!icon.emoji) {
                                        j += str.length() + 1;
                                        i += scaledWidth;
                                        continue;
                                    }
                                }
                            }
                        }
                    }
                    if (c0 == 60 && j + 7 < text.length()) {
                        String s = text.substring(j, j + 9);
                        if (s.charAt(0) == '<' && s.charAt(1) == '#' && s.charAt(8) == '>') {
                            String potentialHex = s.substring(2, 8);
                            if (checkIfHexadecimal(potentialHex)) {
                                j += 8;
                                continue;
                            }
                        }
                    }
                    //Numbered strings
                    // <{HexColor}:{Number}>
                    sub = text.substring(j + 1);
                    if (c0 == 60 && sub.contains(":") && sub.contains(">")) {
                        String hex = sub.substring(0, sub.indexOf(":"));
                        if (checkIfHexadecimal(hex)) {
                            String num = sub.substring(sub.indexOf(":") + 1, sub.indexOf(">"));
                            if (num.matches("\\d+") && !num.isEmpty()) {
                                i += ((int) mcFive.getWidth(num)) + 2;
                                j += hex.length() + num.length() + 2;
                                continue;
                            }
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

    public static boolean checkIfHexadecimal(String potentialHex) {
        for (char ch : potentialHex.toCharArray()) {
            boolean isHex =
                ('0' <= ch && ch <= '9') ||
                    ('a' <= ch && ch <= 'f') ||
                    ('A' <= ch && ch <= 'F');

            if (!isHex) {
                return false;
            }
        }
        return true;
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
        int i = -1;
        int j = text.length();

        while ((i = text.indexOf(167, i + 1)) != -1) {
            if (i < j - 1) {
                if (i + 3 < j - 1 && isFormatColor(text.charAt(i + 3))) {
                    continue;
                }
                char c0 = text.charAt(i + 1);
                if (isFormatColor(c0)) {
                    s = "§" + c0;
                } else if (isFormatSpecial(c0)) {
                    s = s + "§" + c0;
                }
            }
        }

        i = -1;
        j = text.length();

        while ((i = text.indexOf('<', i + 1)) != -1) {
            if (i < j - 1) {
                char c0 = text.charAt(i + 1);
                if (c0 != '#') continue;
                if (i + 8 < text.length()) {
                    String hex = text.substring(i + 2, i + 8);
                    if (text.charAt(i + 1) == '#' && checkIfHexadecimal(hex)) {
                        s = "<#" + hex + ">";
                    }
                    i += 8;
                }
            }
        }


        return s;
    }

    public static String getHexFromString(String text, boolean getFirst) {
        int i = 0;
        String color = "";
        while (i < text.length()) {
            if (text.charAt(i) == '§' && "0123456789abcdef".indexOf(text.charAt(i + 1)) > -1) {
                if (i + 3 < text.length() && text.charAt(i + 2) == '§' && "0123456789abcdef".indexOf(text.charAt(i + 3)) > -1) {
                    i += 2; //If there is a double color code, skip the first one so it doesn't get added to the color
                    continue;
                }
                color = toHex("§" + text.charAt(i + 1));
                if (getFirst) {
                    break;
                }
            }
            i++;
        }

        i = -1;
        int j = text.length();

        while ((i = text.indexOf('<', i + 1)) != -1) {
            if (i < j - 1) {
                char c0 = text.charAt(i + 1);
                if (c0 != '#') continue;
                if (i + 8 < text.length()) {
                    String hex = text.substring(i + 2, i + 8);
                    if (text.charAt(i + 1) == '#' && checkIfHexadecimal(hex)) {
                        color = "#" + hex;
                    }
                    i += 8;

                    if (getFirst) {
                        break;
                    }
                }
            }
        }

        return color;
    }

    public String trimStringToWidth(String text, int width, boolean reverse) {
        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        int j = reverse ? text.length() - 1 : 0;
        int k = reverse ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;

        for (int l = j; l >= 0 && l < text.length() && i < width; l += k) {
            char c0 = text.charAt(l);
            try {
                String sub = text.substring(l + 1);
                if (c0 == ':' && sub.contains(":") && !sub.substring(0, sub.indexOf(":")).isEmpty()) {
                    String str = sub.substring(0, sub.indexOf(":"));
                    if (!str.isEmpty()) {
                        ImageIcon icon = ImageIcon.getIcon(str);
                        if (icon != null) {
                            int w = icon.getWidth();
                            int height = icon.getHeight();
                            float scaledHeight = (float) 9 / height;
                            int scaledWidth = (int) (w * scaledHeight);

                            l += (str.length() + 1) * k;
                            i += scaledWidth;
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
                if (c0 == 60 && l + 7 < text.length()) {
                    String s = text.substring(l, l + 9);
                    if (s.matches("<#([0-9a-fA-F]){6}>")) {
                        l += 8 * k;
                        if (reverse) {
                            stringbuilder.insert(0, s);
                        } else {
                            stringbuilder.append(s);
                        }
                        continue;
                    }
                }

                //Numbered strings
                // <{HexColor}:{Number}>
                if (c0 == 60 && sub.contains(":") && sub.contains(">")) {
                    String hex = sub.substring(0, sub.indexOf(":"));
                    if (checkIfHexadecimal(hex)) {
                        String num = sub.substring(sub.indexOf(":") + 1, sub.indexOf(">"));
                        if (num.matches("\\d+") && !num.isEmpty()) {
                            int i1 = ((int) mcFive.getWidth(num)) + 2;
                            l += hex.length() + num.length() + 2;
                            i += i1;
                            if (i > width) {
                                break;
                            }
                            if (reverse) {
                                stringbuilder.insert(0, "<" + hex + ":" + num + ">");
                            } else {
                                stringbuilder.append("<" + hex + ":" + num + ">");
                            }
                            continue;
                        }
                    }
                }
            } catch (Exception ignored) {
                ignored.printStackTrace();
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
        return stringbuilder.toString();
    }
}
