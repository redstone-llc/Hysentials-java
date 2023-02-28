package cc.woverflow.hysentials.handlers.imageicons;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageIcon {
    public static HashMap<String, ImageIcon> imageIcons = new HashMap<>();
    public static Random random = new Random();

    private final ResourceLocation resourceLocation;
    private final String name;
    private final int width;
    private final int height;

    public ImageIcon(String name, ResourceLocation resourceLocation) {
        this.name = name;
        this.resourceLocation = resourceLocation;
        try {
            InputStream stream = Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(resourceLocation);
            if (stream == null) {
                throw new RuntimeException("ImageIcon " + name + " does not exist in the resource pack!");
            }
            BufferedImage image = javax.imageio.ImageIO.read(stream);
            this.width = image.getWidth();
            this.height = image.getHeight();
            if (height != 9) {
                throw new RuntimeException("ImageIcon " + name + " has an invalid size! Expected height to be 9");
            }
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageIcon.imageIcons.put(name, this);
    }


    public String getName() {
        return name;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static Pattern stringPattern = Pattern.compile(":([a-z_\\-0-9]+):", 2);

    public static String shiftRenderText(FontRenderer instance, String text, float x, float y, int color, boolean shadow) {
        Matcher matcher = stringPattern.matcher(text);
        if (matcher.find()) {
            String group = matcher.group(1);
            ImageIcon imageIcon = getIcon(group);
            if (imageIcon == null) {
                if (shadow) {
                    instance.drawStringWithShadow(text, x, y, color);
                } else {
                    instance.drawString(text, (int) x, (int) y, color);
                }
                return text;
            }
            int i = imageIcon.getWidth();

            String first = text.substring(0, text.indexOf(":" + group + ":"));
            String second = text.substring(text.indexOf(":" + group + ":") + group.length() + 2);

            int i1 = (Minecraft.getMinecraft().fontRendererObj.getStringWidth(first));
            instance.drawStringWithShadow(first, x, y, color);
            if (color != -1) {
                GlStateManager.color((float) (color >> 16 & 255) / 255.0F, (float) (color >> 8 & 255) / 255.0F, (float) (color & 255) / 255.0F, (float) (color >> 24 & 255) / 255.0F);
            } else {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }
            Minecraft.getMinecraft().getTextureManager().bindTexture(imageIcon.getResourceLocation());
            drawModalRectWithCustomSizedTexture((x + i1), y - 1, 0, 0, imageIcon.getWidth(), imageIcon.getHeight(), imageIcon.getWidth(), imageIcon.getHeight());
            Matcher nextMatcher = stringPattern.matcher(second);
            String chatColor = first.lastIndexOf("§") != -1 ? first.substring(first.lastIndexOf("§"), first.lastIndexOf("§") + 2) : "";
            String lastHex = "";
            int j = first.lastIndexOf('<');
            char c0 = j > -1 ? first.charAt(j) : 0;
            if (c0 == '<' && j + 8 < text.length()) {
                String s = text.substring(j, j + 9);
                if (s.matches("<#([0-9a-fA-F]){6}>")) {
                    lastHex = s;
                }
            }
            if (chatColor.isEmpty() || first.indexOf(lastHex) > first.indexOf(chatColor)) {
                chatColor = lastHex;
            }
            if (nextMatcher.find()) {
                return shiftRenderText(instance, chatColor + second, x + i1 + i, y, color, shadow);
            } else {
                if (shadow) {
                    instance.drawStringWithShadow(chatColor + second, x + i1 + i, y, color);
                } else {
                    instance.drawString(chatColor + second, (int) (x + i1 + i), (int) y, color);
                }
            }
            return chatColor + second;
        } else {
            if (shadow) {
                instance.drawStringWithShadow(text, x, y, color);
            } else {
                instance.drawString(text, (int) x, (int) y, color);
            }
            return text;
        }
    }

    public static void drawModalRectWithCustomSizedTexture(double x, double y, float u, float v, int width, int height, float textureWidth, float textureHeight) {
        float f = 1.0F / textureWidth;
        float g = 1.0F / textureHeight;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos((double) x, (double) (y + height), 0.0).tex((double) (u * f), (double) ((v + (float) height) * g)).endVertex();
        worldRenderer.pos((double) (x + width), (double) (y + height), 0.0).tex((double) ((u + (float) width) * f), (double) ((v + (float) height) * g)).endVertex();
        worldRenderer.pos((double) (x + width), (double) y, 0.0).tex((double) ((u + (float) width) * f), (double) (v * g)).endVertex();
        worldRenderer.pos((double) x, (double) y, 0.0).tex((double) (u * f), (double) (v * g)).endVertex();
        tessellator.draw();
    }

    public static ImageIcon getIcon(String name) {
        return imageIcons.get(name);
    }
}
