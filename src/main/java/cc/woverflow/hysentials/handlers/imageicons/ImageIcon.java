package cc.woverflow.hysentials.handlers.imageicons;

import cc.woverflow.hysentials.util.MUtils;
import cc.woverflow.hysentials.util.ImageIconRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageIcon {
    public static HashMap<String, ImageIcon> imageIcons = new HashMap<>();
    public static Random random = new Random();

    private ResourceLocation resourceLocation;
    private DynamicTexture dynamicTexture;
    private final String name;
    public int width;
    public int height;

    public ImageIcon(String name, ResourceLocation resourceLocation) {
        this.name = name;
        this.resourceLocation = resourceLocation;
        try {
            File file = new File("./config/hysentials/imageicons/" + name + ".png");
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
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
                this.dynamicTexture = new DynamicTexture(image);
                stream.close();
                ImageIO.write(image, "png", file);
                this.resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(this.name, this.dynamicTexture);
            } else {
                BufferedImage image = javax.imageio.ImageIO.read(file);
                this.width = image.getWidth();
                this.height = image.getHeight();
                if (height != 9) {
                    throw new RuntimeException("ImageIcon " + name + " has an invalid size! Expected height to be 9");
                }
                this.dynamicTexture = new DynamicTexture(image);
                this.resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(this.name, this.dynamicTexture);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageIcon.imageIcons.put(name, this);
    }

    public static void reloadIcons() {
        try {
            for (ImageIcon icon : ImageIcon.imageIcons.values()) {
                File file = new File("./config/hysentials/imageicons/" + icon.name + ".png");
                if (file.exists()) {
                    BufferedImage image = javax.imageio.ImageIO.read(file);
                    if (image.getHeight() != 9) {
                        MUtils.chat("&cImageIcon " + icon.name + " has an invalid size! Expected height to be 9");
                        ImageIcon.imageIcons.remove(icon.name);
                        continue;
                    }
                    icon.width = image.getWidth();
                    icon.height = image.getHeight();
                    icon.dynamicTexture = new DynamicTexture(image);
                    icon.resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(icon.name, icon.dynamicTexture);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    public static List<ImageIcon> getIconsFromText(String text) {
        List<ImageIcon> icons = new ArrayList<>();
        Matcher matcher = stringPattern.matcher(text);
        if (matcher.find()) {
            String group = matcher.group(1);
            ImageIcon imageIcon = getIcon(group);
            if (imageIcon != null) {
                icons.add(imageIcon);
            }
        }
        return icons;
    }

    public int renderImage(float x, float y) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.resourceLocation);
        drawModalRectWithCustomSizedTexture((x), y, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
        return getWidth();
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
