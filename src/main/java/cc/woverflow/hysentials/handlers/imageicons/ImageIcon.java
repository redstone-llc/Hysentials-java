package cc.woverflow.hysentials.handlers.imageicons;

import cc.woverflow.hysentials.cosmetic.CosmeticGui;
import cc.woverflow.hysentials.util.MUtils;
import cc.woverflow.hysentials.util.ImageIconRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
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
    public boolean emoji;

    public ImageIcon(String name, ResourceLocation resourceLocation, boolean emoji) {
        this.name = name;
        this.resourceLocation = resourceLocation;
        this.emoji = emoji;
        try {
            handleImageIcon();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ImageIcon.imageIcons.put(name, this);
    }

    public ImageIcon(String name, ResourceLocation resourceLocation) {
        this(name, resourceLocation, false);
    }

    private void handleImageIcon() throws IOException {
        File file = new File("./config/hysentials/" + (emoji ? "emojis" : "imageicons") + "/" + name + ".png");
        if (!file.exists() || (name.equals("party") && javax.imageio.ImageIO.read(file).getWidth() != 48)) {
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

            this.dynamicTexture = new DynamicTexture(image);
            stream.close();
            ImageIO.write(image, "png", file);
            this.resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(this.name, this.dynamicTexture);
        } else {
            BufferedImage image = javax.imageio.ImageIO.read(file);
            this.width = image.getWidth();
            this.height = image.getHeight();
            this.dynamicTexture = new DynamicTexture(image);
            this.resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(this.name, this.dynamicTexture);
        }
    }

    public static void reloadIcons() {
        try {
            for (ImageIcon icon : ImageIcon.imageIcons.values()) {
                File file = new File("./config/hysentials/imageicons/" + icon.name + ".png");
                if (file.exists()) {
                    BufferedImage image = javax.imageio.ImageIO.read(file);
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

    public static Pattern stringPattern = Pattern.compile(":([a-z_\\-0-9?]+):", 2);

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

    public int renderImage(float x, float y, boolean shadow, int oldColor, UUID uuid, float alpha) {
        if (emoji && uuid != null && !CosmeticGui.Companion.hasCosmetic(uuid, "hymojis")) {
            return -1;
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(this.resourceLocation);
        int width = this.getWidth();
        int height = this.getHeight();
        float scaledHeight = (float) 9/height;

        GlStateManager.pushMatrix();
        GlStateManager.scale(scaledHeight, scaledHeight, scaledHeight);
        int textColor = Integer.parseInt("FFFFFF", 16);
        if (shadow) {
            textColor = (textColor & 16579836) >> 2 | textColor & -16777216;
        }
        GlStateManager.color((float) (textColor >> 16) / 255.0F, (float) (textColor >> 8 & 255) / 255.0F, (float) (textColor & 255) / 255.0F, alpha);
        drawModalRectWithCustomSizedTexture(x * (1 / scaledHeight), y * (1 / scaledHeight), 0, 0, width, height, width, height);
        GlStateManager.color((float) (oldColor >> 16) / 255.0F, (float) (oldColor >> 8 & 255) / 255.0F, (float) (oldColor & 255) / 255.0F, alpha);

        GlStateManager.popMatrix();
        return (int) (getWidth() * scaledHeight);
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
