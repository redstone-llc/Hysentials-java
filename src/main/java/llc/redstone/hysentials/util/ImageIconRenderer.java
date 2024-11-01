package llc.redstone.hysentials.util;

import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
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

public class ImageIconRenderer {
    FontRendererAcessor accessor = (FontRendererAcessor) this;
    public static HysentialsFontRenderer mcFive;

    public ImageIconRenderer() {
        mcFive = new HysentialsFontRenderer("Minecraft Five", 12f);
    }

    public static HysentialsFontRenderer getMcFive() {
        if (mcFive == null) {
            mcFive = new HysentialsFontRenderer("Minecraft Five", 12f);
        }
        return mcFive;
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
        float width = getMcFive().getWidth(num) + 2;
        Renderer.drawRect(x, y, width, 7);
        setColorA((float) (oldColor >> 16) / 255.0F, (float) (oldColor >> 8 & 255) / 255.0F, (float) (oldColor & 255) / 255.0F, alpha);
        getMcFive().drawString(num, x + 1, y, textColor);
        setColorA((float) (oldColor >> 16) / 255.0F, (float) (oldColor >> 8 & 255) / 255.0F, (float) (oldColor & 255) / 255.0F, alpha);
        return (int) width;
    }

    public static void setColorA(float r, float g, float b, float a) {
        GlStateManager.color(r, g, b, a);
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

    private static boolean checkIfHexadecimal(String hex) {
        try {
            Integer.parseInt(hex, 16);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static List<IChatComponent> splitTextOld(IChatComponent chatComponent, int maxWidth, FontRenderer fontRenderer, boolean addNewLine, boolean keepFormatting) {
        int currentWidth = 0;
        IChatComponent currentLine = new ChatComponentText("");
        List<IChatComponent> lines = Lists.newArrayList();
        List<IChatComponent> components = Lists.newArrayList(chatComponent);

        for (int i = 0; i < components.size(); ++i) {
            IChatComponent component = components.get(i);
            String text = component.getUnformattedTextForChat();
            boolean newLine = false;
            String remainingText;

            if (text.contains("\n")) {
                int newLineIndex = text.indexOf(10);
                remainingText = text.substring(newLineIndex + 1);
                text = text.substring(0, newLineIndex + 1);
                ChatComponentText newComponent = new ChatComponentText(remainingText);
                newComponent.setChatStyle(component.getChatStyle().createShallowCopy());
                components.add(i + 1, newComponent);
                newLine = true;
            }

            TriVariable<Integer, String, String> duoFormatted = formatString(func_178909_a(component.getChatStyle().getFormattingCode() + text, keepFormatting));
            String formattedText = duoFormatted.getSecond();
            remainingText = formattedText.endsWith("\n") ? formattedText.substring(0, formattedText.length() - 1) : formattedText;
            TriVariable<Integer, String, String> duoRemaining = formatString(remainingText);
            int textWidth = duoRemaining.getFirst();
            ChatComponentText newComponentText = new ChatComponentText(duoRemaining.getSecond());
            newComponentText.setChatStyle(component.getChatStyle().createShallowCopy());

            String color = duoFormatted.getThird();

            if (currentWidth + textWidth > maxWidth) {
                String trimmedText = fontRenderer.trimStringToWidth(formattedText, maxWidth - currentWidth, false);
                TriVariable<Integer, String, String> duoTrimmed = formatString(trimmedText);
                trimmedText = duoTrimmed.getSecond();
                String overflowText = trimmedText.length() < formattedText.length() ? formattedText.substring(trimmedText.length()) : null;
                color = duoTrimmed.getThird();
                if (overflowText != null && overflowText.length() > 0) {
                    int lastSpaceIndex = trimmedText.lastIndexOf(" ");
                    if (lastSpaceIndex >= 0 && formatString(formattedText.substring(0, lastSpaceIndex)).first > 0) {
                        duoTrimmed = formatString(formattedText.substring(0, lastSpaceIndex));
                        trimmedText = duoTrimmed.second;
                        color = duoTrimmed.third;
                        if (addNewLine) {
                            ++lastSpaceIndex;
                        }
                        overflowText = formattedText.substring(lastSpaceIndex);
                    } else if (currentWidth > 0 && !formattedText.contains(" ")) {
                        trimmedText = "";
                        overflowText = formattedText;
                    }

                    overflowText = color + overflowText;
                    ChatComponentText overflowComponent = new ChatComponentText(overflowText);
                    overflowComponent.setChatStyle(component.getChatStyle().createShallowCopy());
                    components.add(i + 1, overflowComponent);
                }

                duoTrimmed = formatString(trimmedText);
                textWidth = duoTrimmed.getFirst();
                newComponentText = new ChatComponentText(duoTrimmed.second);
                newComponentText.setChatStyle(component.getChatStyle().createShallowCopy());
                newLine = true;
            }

            if (currentWidth + textWidth <= maxWidth) {
                currentWidth += textWidth;
                currentLine.appendSibling(newComponentText);
            } else {
                newLine = true;
            }

            if (newLine) {
                lines.add(currentLine);
                currentWidth = 0;
                currentLine = new ChatComponentText("");
            }
        }

        lines.add(currentLine);
        return lines;
    }

    private static TriVariable<Integer, String, String> formatString(String formattedText) {
        String color = FancyFormatting2.Companion.getLastFormat(formattedText, "");
        List<Format> formats = FancyFormatting2.Companion.getFormats(formattedText, false);
        int textWidth = 0;

        StringBuilder result = new StringBuilder();
        for (Format format: formats) {
            switch (format.getType()) {
                case STRING:
                    String s = format.getValue().toString();
                    textWidth += UMinecraft.getFontRenderer().getStringWidth(s);

                    result.append(color).append(s);
                    color = FancyFormatting2.Companion.getLastFormat(s, color);
                    break;
                case HEX:
                    color = "<#" + Integer.toHexString((int) format.getValue()) + ">";
                    result.append("<#").append(Integer.toHexString((int) format.getValue())).append(">");
                    break;
                case IMAGE_ICON:
                    ImageIcon icon = (ImageIcon) format.getValue();
                    int width = icon.getWidth();
                    int height = icon.getHeight();
                    float scaledHeight = 9.0f / height;
                    int updatedWidth = (int) (width * scaledHeight);
                    textWidth += updatedWidth;
                    result.append(":").append(icon.getName()).append(":");
                    break;
                case NUMBER:
                    Number number = (Number) format.getValue();
                    String num = number.getNumber();
                    textWidth += (int) (getMcFive().getWidth(num) + 2);
                    result.append("<").append(number.getHex()).append(":").append(num).append(">");
                    break;
            }
        }
        return new TriVariable<>(textWidth, result.toString(), color);
    }

    public static List<IChatComponent> splitText(IChatComponent text, int maxWidth, FontRenderer fontRenderer, boolean preserveFormatting, boolean useUnicode) {
        int currentWidth = 0;
        List<IChatComponent> resultComponents = Lists.newArrayList();
        List<IChatComponent> componentsToProcess = new ArrayList<>(Collections.singletonList(text));

        while (!componentsToProcess.isEmpty()) {
            IChatComponent currentComponent = new ChatComponentText("");
            IChatComponent component = componentsToProcess.get(0);
            String formattedText = func_178909_a(component.getFormattedText(), useUnicode);

            if (formattedText.contains("\n")) {
                String[] splitText = formattedText.split("\n");
                for (String split : splitText) {
                    ChatComponentText newComponent = new ChatComponentText(split);
                    newComponent.setChatStyle(component.getChatStyle().createShallowCopy());
                    componentsToProcess.add(newComponent);
                }
                componentsToProcess.remove(0);
                continue;
            }

            String color = FancyFormatting2.Companion.getLastFormat(formattedText, "");
            List<Format> formats = FancyFormatting2.Companion.getFormats(formattedText, false);
            for (Format format: formats) {
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
                                color = FancyFormatting2.Companion.getLastFormat(split, color);
                                currentComponent.appendSibling(new ChatComponentText(color + split + " "));
                                currentWidth += splitWidth + 4; // 4 is the width of a space
                            }
                        } else {
                            color = FancyFormatting2.Companion.getLastFormat(s, color);
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
            resultComponents.add(currentComponent);
            componentsToProcess.remove(0);
        }

        return resultComponents;
    }
}
