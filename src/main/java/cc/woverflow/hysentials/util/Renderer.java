package cc.woverflow.hysentials.util;

import cc.woverflow.hysentials.guis.ResolutionUtil;
import cc.woverflow.hysentials.user.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;

public class Renderer {
    public Renderer() {
    }

    private static Long colorized = null;
    private static Long drawMode = null;
    private static boolean retainTransforms = false;

    private static final Tessellator tessellator = Tessellator.getInstance();
    private static final WorldRenderer worldRenderer = tessellator.getWorldRenderer();

    public static void drawString(String text, float x, float y) {
        text = text.replace("&", "§");
        Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, (int) color(255, 255, 255, 255), false);
    }

    public static void drawString(FontRenderer renderer, String text, float x, float y) {
        text = text.replace("&", "§");
        renderer.drawString(text, x, y, (int) color(255, 255, 255, 255), false);
    }

    public static void drawString(String text, int x, int y) {
        text = text.replace("&", "§");
        Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, (int) color(255, 255, 255, 255), false);
    }

    public static void drawString(FontRenderer renderer, String text, float x, float y, boolean shadow) {
        text = text.replace("&", "§");
        renderer.drawString(text, x, y, (int) color(255, 255, 255, 255), shadow);
    }

    public static void drawString(String text, int x, int y, boolean shadow) {
        text = text.replace("&", "§");
        Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, (int) color(255, 255, 255, 255), shadow);
    }

    public static void drawRect(long color, float x, float y, float width, float height) {
        float[] pos = {x, y, x + width, y + height};
        if (pos[0] > pos[2])
            Collections.swap(Arrays.asList(pos), 0, 2);
        if (pos[1] > pos[3])
            Collections.swap(Arrays.asList(pos), 1, 3);

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        doColor(color);

        worldRenderer.begin(drawMode != null ? Math.toIntExact(drawMode) : 7, DefaultVertexFormats.POSITION);
        worldRenderer.pos((double) pos[0], (double) pos[3], 0.0).endVertex();
        worldRenderer.pos((double) pos[2], (double) pos[3], 0.0).endVertex();
        worldRenderer.pos((double) pos[2], (double) pos[1], 0.0).endVertex();
        worldRenderer.pos((double) pos[0], (double) pos[1], 0.0).endVertex();

        tessellator.draw();

        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();

        finishDraw();
    }

    public static void drawImage(DynamicTexture image, double x, double y, double width, double height) {
        if (colorized == null)
            GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.enableBlend();
        GlStateManager.scale(1f, 1f, 50f);
        GlStateManager.bindTexture(image.getGlTextureId());
        GlStateManager.enableTexture2D();

        worldRenderer.begin(drawMode != null ? Math.toIntExact(drawMode) : 7, DefaultVertexFormats.POSITION_TEX);

        worldRenderer.pos(x, y + height, 0.0).tex(0.0, 1.0).endVertex();
        worldRenderer.pos(x + width, y + height, 0.0).tex(1.0, 1.0).endVertex();
        worldRenderer.pos(x + width, y, 0.0).tex(1.0, 0.0).endVertex();
        worldRenderer.pos(x, y, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();

        finishDraw();
    }

    public static void drawImage(ResourceLocation image, int x, int y, int width, int height) {
        drawImage(image, x, y, width, Double.valueOf(height));
    }

    public static void drawImage(ResourceLocation image, double x, double y, double width, double height) {
        if (colorized == null)
            GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.enableBlend();
        GlStateManager.scale(1f, 1f, 50f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        GlStateManager.enableTexture2D();

        worldRenderer.begin(drawMode != null ? Math.toIntExact(drawMode) : 7, DefaultVertexFormats.POSITION_TEX);

        worldRenderer.pos(x, y + height, 0.0).tex(0.0, 1.0).endVertex();
        worldRenderer.pos(x + width, y + height, 0.0).tex(1.0, 1.0).endVertex();
        worldRenderer.pos(x + width, y, 0.0).tex(1.0, 0.0).endVertex();
        worldRenderer.pos(x, y, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();

        finishDraw();
    }

    private static void doColor(long longColor) {
        int color = (int) longColor;

        if (colorized == null) {
            float a = ((color >> 24) & 255) / 255.0f;
            float r = ((color >> 16) & 255) / 255.0f;
            float g = ((color >> 8) & 255) / 255.0f;
            float b = (color & 255) / 255.0f;
            GlStateManager.color(r, g, b, a);
        }
    }

    public static void finishDraw() {
        if (!retainTransforms) {
            colorized = null;
            drawMode = null;
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
        }
    }

    public static long color(long red, long green, long blue) {
        return color(red, green, blue, 255);
    }

    public static long color(long red, long green, long blue, long alpha) {
        return (clamp((int) alpha) * 0x1000000L
            + clamp((int) red) * 0x10000L
            + clamp((int) green) * 0x100L
            + clamp((int) blue));
    }

    static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    public static URLConnection makeWebRequest(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (ChatTriggers)");
        connection.setConnectTimeout(3000);
        connection.setReadTimeout(3000);
        return connection;
    }

    public static BufferedImage getImageFromUrl(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) makeWebRequest(url);
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);
        return ImageIO.read(conn.getInputStream());
    }

    private static void translate(int x, int y, float z) {
        GlStateManager.translate(x, y, z);
    }

    private static void scale(float scaleX, float scaleY) {
        GlStateManager.scale(scaleX, scaleY, 1.0f);
    }

    public static class IconButton {
        DynamicTexture texture;
        Consumer<Integer> callback;
        int width, height;
        int x, y;

        public IconButton(String url, int width, int height, Consumer<Integer> callback) {
            try {
                this.texture = new DynamicTexture(getImageFromUrl(url));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.callback = callback;
            this.width = width;
            this.height = height;
        }

        public void draw(int x, int y) {
            drawImage(texture, x, y, width, height);
            this.x = x;
            this.y = y;
        }

        public void click(int mouseX, int mouseY) {
            if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height)
                callback.accept(0);
        }
    }

    public static class screen {
        public static int getWidth() {
            return ResolutionUtil.current().getScaledWidth();
        }

        public static int getHeight() {
            return ResolutionUtil.current().getScaledHeight();
        }
    }
}
