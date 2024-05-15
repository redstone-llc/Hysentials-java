package llc.redstone.hysentials.guis.sbBoxes.huds;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.hud.SingleTextHud;
import cc.polyfrost.oneconfig.libs.universal.*;
import cc.polyfrost.oneconfig.renderer.*;
import cc.polyfrost.oneconfig.utils.color.ColorUtils;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.guis.utils.SBBoxesHud;
import llc.redstone.hysentials.handlers.sbb.SbbRenderer;
import llc.redstone.hysentials.mixin.GuiIngameAccessor;
import llc.redstone.hysentials.mixin.MinecraftAccessor;
import llc.redstone.hysentials.util.Renderer;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.Color;

// Taken from Polyfrost's VanillaHUD mod
// LGPL-3.0 license (https://github.com/Polyfrost/VanillaHUD)
public class ActionBarHUD extends SBBoxesTextHud {

    @Exclude
    private float hue;
    @Exclude
    private int opacity;

    @Exclude
    private static final String EXAMPLE_TEXT = "Action Bar";

    @Switch(
        name = "Use Jukebox Rainbow Timer Color",
        description = "Use the rainbow timer color when a jukebox begins playing."
    )
    private boolean rainbowTimer = true;

    public ActionBarHUD() {
        super(true, 1920f / 2, 1080f - 62f, 1, true, 4, new OneColor(0, 0, 0, 125), true, new OneColor(0, 0, 0, 52), new OneColor(255, 255, 255));
    }

    @Override
    protected void drawLine(String line, float x, float y, float scale) {
        GuiIngameAccessor ingameGUI = (GuiIngameAccessor) UMinecraft.getMinecraft().ingameGUI;
        int color = this.rainbowTimer && ingameGUI.getRecordIsPlaying() ? Color.HSBtoRGB(this.hue / 50.0F, 0.7F, 0.6F) & 16777215 : ColorUtils.setAlpha(this.textColor.getRGB(), Math.min(this.textColor.getAlpha(), this.opacity));
        UGraphics.enableBlend();

        TextRenderer.drawScaledString(line, x + ((getWidth(scale, false) - getLineWidth(line, scale)) / 2), y + ((getHeight(scale, false) - (8 * scale)) / 2), color | this.opacity << 24, TextRenderer.TextType.toType(this.textType), scale);
        UGraphics.disableBlend();
    }

    @Override
    protected boolean shouldShow() {
        if (Hysentials.INSTANCE.isApec()) { // I love Apec Mod Minecraft
            return false;
        }
        GuiIngameAccessor ingameGUI = (GuiIngameAccessor) UMinecraft.getMinecraft().ingameGUI;
        if (ingameGUI.getRecordPlayingUpFor() <= 0 || ingameGUI.getRecordPlaying() == null || ingameGUI.getRecordPlaying().isEmpty()) {
            return false;
        }

        this.hue = (float) ingameGUI.getRecordPlayingUpFor() - ((MinecraftAccessor)UMinecraft.getMinecraft()).getTimer().renderPartialTicks;
        this.opacity = (int) (this.hue * 256.0f / 20.0f);
        if (this.opacity > 255) {
            this.opacity = 255;
        }

        return opacity > 0 && super.shouldShow();
    }


    @Override
    protected void drawBackground(float x, float y, float width, float height, float scale, boolean example) {
        int bgColor = ColorUtils.setAlpha(this.bgColor.getRGB(), Math.min(this.bgColor.getAlpha(), this.opacity));
        int style = this.cornerType;
        switch (style) {
            case 0: {
                GlStateManager.pushMatrix();
                Renderer.drawRect(bgColor, x, y, width, height);
                GlStateManager.popMatrix();
                break;
            }
            case 1: {
                float finalY = y;
                float finalX = x;
                NanoVGHelper.INSTANCE.setupAndDraw(true, (vg) -> {
                    NanoVGHelper.INSTANCE.drawRoundedRect(vg, finalX, finalY, width, height, bgColor, this.cornerRadius * scale);
                });
                break;
            }
            case 2: {
                GlStateManager.pushMatrix();
                if (this.shadow) {
                    SbbRenderer.drawBox(x, y, width, height, this.bgColor, this.shadowColor, (int) this.cornerRadius);
                } else {
                    SbbRenderer.drawBox(x, y, width, height, this.bgColor, false, (int) this.cornerRadius);
                }
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    protected float getHeight(float scale, boolean example) {
        return super.getHeight(scale, example) + 5 * scale;
    }

    @Override
    protected float getWidth(float scale, boolean example) {
        return super.getWidth(scale, example) + 10 * scale;
    }

    @Override
    protected String getText(boolean example) {
        GuiIngameAccessor ingameGUI = (GuiIngameAccessor) UMinecraft.getMinecraft().ingameGUI;

        if (ingameGUI == null || ingameGUI.getRecordPlaying() == null || ingameGUI.getRecordPlaying().isEmpty() || !this.shouldShow() && example) {
            this.opacity = 255;
            return EXAMPLE_TEXT;
        }

        return ingameGUI.getRecordPlaying();
    }
}
