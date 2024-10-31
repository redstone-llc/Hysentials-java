package llc.redstone.hysentials.util;

import com.google.common.collect.Lists;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.cosmetic.CosmeticManager;
import llc.redstone.hysentials.handlers.imageicons.ImageIcon;
import llc.redstone.hysentials.hook.FontRendererAcessor;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import llc.redstone.hysentials.renderer.text.FancyFormatting2;
import llc.redstone.hysentials.renderer.text.FancyFormattingKt;
import llc.redstone.hysentials.renderer.text.Format;
import llc.redstone.hysentials.renderer.text.FormatType;
import llc.redstone.hysentials.renderer.text.Number;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static llc.redstone.hysentials.util.C.toHex;
import static net.minecraft.client.gui.GuiUtilRenderComponents.func_178909_a;

public class ImageIconRenderer extends FontRenderer {
    FontRendererAcessor accessor = (FontRendererAcessor) this;
    public static HysentialsFontRenderer mcFive;

    public ImageIconRenderer() {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().getTextureManager(), false);
        super.onResourceManagerReload(null);
        mcFive = new HysentialsFontRenderer("Minecraft Five", 12f);
    }

    public static HysentialsFontRenderer getMcFive() {
        if (mcFive == null) {
            mcFive = new HysentialsFontRenderer("Minecraft Five", 12f);
        }
        return mcFive;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        super.onResourceManagerReload(resourceManager);
        ImageIcon.reloadIcons();
    }

    private List<String> renderedText = new ArrayList<>();

    private void renderStringAtPosA(String text, boolean shadow) {
        superRenderStringAtPos(text, shadow);
        return;


//        text = FancyFormatting2.Companion.replaceString(text);
//        List<Format> formats = FancyFormatting2.Companion.getFormats(text, false);
//        if (!renderedText.contains(text)) {
//            renderedText.add(text);
//            System.out.println("Text: " + text);
//            System.out.println("Formats: " + formats);
//        }
//        for (Format format: formats) {
//            switch (format.getType()) {
//                case STRING:
//                    superRenderStringAtPos(format.getValue().toString(), shadow);
//                    break;
//                case HEX: {
//                    int hex = (int) format.getValue();
//                    reset();
//                    if (shadow) {
//                        hex = (hex & 16579836) >> 2 | hex & -16777216;
//                    }
//                    accessor.setTextColor(hex);
//                    this.setColor((float) (hex >> 16) / 255.0F, (float) (hex >> 8 & 255) / 255.0F, (float) (hex & 255) / 255.0F, accessor.alpha());
//                    break;
//                }
//                case IMAGE_ICON: {
//                    ImageIcon icon = (ImageIcon) format.getValue();
//                    float y = this.posY - 1;
//                    float positionAdd = icon.renderImage(this.posX, y, shadow, accessor.getTextColor(), accessor.alpha());
//                    if (positionAdd > 0) {
//                        this.posX += positionAdd;
//                    }
//                    break;
//                }
//                default: {
//                    return;
//                }
//            }
//        }
    }

    private void reset() {
        accessor.setRandomStyle(false);
        accessor.setBoldStyle(false);
        accessor.setStrikethroughStyle(false);
        accessor.setUnderlineStyle(false);
        accessor.setItalicStyle(false);
    }

    public static int renderNumberedString(String num, String hex, float x, float y, int oldColor, boolean dropShadow, float alpha) {
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
        int textColor = (int) (alpha * 255f) << 24 | c.getRed() << 16 | c.getGreen() << 8 | c.getBlue();
        setColorA((float) (bgColor >> 16) / 255.0F, (float) (bgColor >> 8 & 255) / 255.0F, (float) (bgColor & 255) / 255.0F, alpha);
        float width = mcFive.getWidth(num) + 2;
        Renderer.drawRect(x, y, width, 7);
        setColorA((float) (oldColor >> 16) / 255.0F, (float) (oldColor >> 8 & 255) / 255.0F, (float) (oldColor & 255) / 255.0F, alpha);
        mcFive.drawString(num, x + 1, y, textColor);
        setColorA((float) (oldColor >> 16) / 255.0F, (float) (oldColor >> 8 & 255) / 255.0F, (float) (oldColor & 255) / 255.0F, alpha);
        return (int) width;
    }

    public static void setColorA(float r, float g, float b, float a) {
        GlStateManager.color(r, g, b, a);
    }

    public int getStringWidth(String text) {
        return super.getStringWidth(FancyFormatting2.Companion.replaceString(text, true));
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
        } catch (ArabicShapingException e) {
            e.printStackTrace();
        }
        return text;
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
                char c0 = text.charAt(i + 1);
                if (!isFormatColor(c0) && i + 3 < j - 1 && isFormatColor(text.charAt(i + 3))) {
                    continue;
                }
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

    public static List<IChatComponent> splitText(IChatComponent text, int maxWidth, FontRenderer fontRenderer, boolean preserveFormatting, boolean useUnicode) {
        int currentWidth = 0;
        IChatComponent currentComponent = new ChatComponentText("");
        List<IChatComponent> resultComponents = Lists.newArrayList();
        List<IChatComponent> componentsToProcess = new ArrayList<>(Collections.singletonList(text));

        int i = 0;
        while (!componentsToProcess.isEmpty()) {
            IChatComponent component = componentsToProcess.get(i);
            String formattedText = func_178909_a(component.getFormattedText(), useUnicode);

            if (formattedText.contains("\n")) {
                String[] splitText = formattedText.split("\n");
                for (String split : splitText) {
                    ChatComponentText newComponent = new ChatComponentText(split);
                    newComponent.setChatStyle(component.getChatStyle().createShallowCopy());
                    componentsToProcess.add(i + 1, newComponent);
                }
                continue;
            }

            String color = FancyFormatting2.Companion.getLastFormat(formattedText);
            for (Format format: FancyFormatting2.Companion.getFormats(formattedText, false)) {
                switch (format.getType()) {
                    case STRING:
                        ChatComponentText newComponent = new ChatComponentText(color + format.getValue().toString());
                        newComponent.setChatStyle(component.getChatStyle().createShallowCopy());
                        String s = format.getValue().toString();
                        int textWidth = fontRenderer.getStringWidth(s);
                        if (currentWidth + textWidth > maxWidth) {
                            String[] splitText = s.split(" ");
                            for (String split : splitText) {
                                int splitWidth = fontRenderer.getStringWidth(split);
                                if (currentWidth + splitWidth > maxWidth) {
                                    resultComponents.add(currentComponent);
                                    currentComponent = new ChatComponentText("");
                                    currentWidth = 0;
                                }
                                currentComponent.appendSibling(new ChatComponentText(color + split + " "));
                                currentWidth += splitWidth + 4; // 4 is the width of a space
                            }
                        } else {
                            currentComponent.appendSibling(newComponent);
                            currentWidth += textWidth;
                        }
                        break;
                    case HEX:
                        currentComponent.appendSibling(new ChatComponentText("<#" + Integer.toHexString((int) format.getValue()) + ">"));
                        color = "<#" + Integer.toHexString((int) format.getValue()) + ">";
                        break;
                    case IMAGE_ICON:
                        ImageIcon icon = (ImageIcon) format.getValue();
                        int width = icon.getWidth();
                        int height = icon.getHeight();
                        float scaledHeight = 9.0f / height;
                        int updatedWidth = (int) (width * scaledHeight);
                        if (currentWidth + updatedWidth > maxWidth) {
                            resultComponents.add(currentComponent);
                            currentComponent = new ChatComponentText("");
                            currentWidth = 0;
                        }
                        currentComponent.appendSibling(new ChatComponentText(":" + icon.getName() + ":"));
                        currentWidth += updatedWidth;
                        break;

                    case NUMBER:
                        Number number = (Number) format.getValue();
                        String num = number.getNumber();
                        String hex = number.getHex();
                        HysentialsFontRenderer mcFive = getMcFive();
                        if (currentWidth + mcFive.getWidth(num) + 2 > maxWidth) {
                            resultComponents.add(currentComponent);
                            currentComponent = new ChatComponentText("");
                            currentWidth = 0;
                        }
                        currentComponent.appendSibling(new ChatComponentText("<" + hex + ":" + num + ">"));
                        currentWidth += (int) (mcFive.getWidth(num) + 2);
                }
            }
            componentsToProcess.remove(i);
        }

        resultComponents.add(currentComponent);
        return resultComponents;
    }

    private void superRenderStringAtPos(String text, boolean shadow) {
        for(int i = 0; i < text.length(); ++i) {
            char c0 = text.charAt(i);
            int i1;
            int j1;
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
                    accessor.setTextColor(j1);
                    this.setColor((float)(j1 >> 16) / 255.0F, (float)(j1 >> 8 & 255) / 255.0F, (float)(j1 & 255) / 255.0F, accessor.alpha());
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
                    reset();
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
                    } while(j1 != this.getCharWidth(c1));

                    c0 = c1;
                }

                float f1 = i1 != -1 && !accessor.isUnicodeFlag() ? 1.0F : 0.5F;
                boolean flag = (c0 == 0 || i1 == -1 || accessor.isUnicodeFlag()) && shadow;
                if (flag) {
                    this.posX -= f1;
                    this.posY -= f1;
                }

                float f = this.renderChar(c0, accessor.isItalicStyle());
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

                    this.renderChar(c0, accessor.isItalicStyle());
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
