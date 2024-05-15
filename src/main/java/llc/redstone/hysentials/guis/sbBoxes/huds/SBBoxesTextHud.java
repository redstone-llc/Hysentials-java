package llc.redstone.hysentials.guis.sbBoxes.huds;

import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.events.event.Stage;
import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.polyfrost.oneconfig.hud.Hud;
import cc.polyfrost.oneconfig.hud.SingleTextHud;
import cc.polyfrost.oneconfig.hud.TextHud;
import cc.polyfrost.oneconfig.internal.hud.HudCore;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack;
import cc.polyfrost.oneconfig.platform.Platform;
import cc.polyfrost.oneconfig.renderer.NanoVGHelper;
import cc.polyfrost.oneconfig.renderer.TextRenderer;
import cc.polyfrost.oneconfig.utils.color.ColorUtils;
import llc.redstone.hysentials.handlers.sbb.SbbRenderer;
import llc.redstone.hysentials.util.Renderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.List;

import static llc.redstone.hysentials.guis.sbBoxes.huds.BossbarSbBoxHud.mc;

public abstract class SBBoxesTextHud extends Hud{
    protected transient String text;
    public SBBoxesTextHud(boolean enabled, float x, float y, float scale, boolean background, float cornerRadius, OneColor bgColor, boolean shadow, OneColor shadowColor, OneColor textColor) {
        super(enabled, x, y, scale);
        this.background = background;
        this.cornerRadius = cornerRadius;
        this.bgColor = bgColor;
        this.shadow = shadow;
        this.shadowColor = shadowColor;
        this.textColor = textColor;
    }

    @Override
    protected void draw(UMatrixStack matrices, float x, float y, float scale, boolean example) {
        if (text == null || text.isEmpty()) return;

        float textY = y;
        for (String line : text.split("\n")) {
            drawLine(line, x, textY, scale);
            textY += 12 * scale;
        }
    }

    @Override
    public void drawAll(UMatrixStack matrices, boolean example) {
        if (!example && !shouldShow()) return;
        preRender(example);
        text = getText(example);
        position.setSize(getWidth(scale, example), getHeight(scale, example));
        GlStateManager.pushMatrix();
        if (background && shouldDrawBackground()) {
            drawBackground(position.getX(), position.getY(), position.getWidth(), position.getHeight(), scale, example);
        }
        draw(matrices, position.getX(), position.getY(), scale, example);
        GlStateManager.popMatrix();
    }

    protected void drawBackground(float x, float y, float width, float height, float scale, boolean example) {
        int bgColor = ColorUtils.setAlpha(this.bgColor.getRGB(), this.bgColor.getAlpha());
        int style = this.cornerType;
        switch (style) {
            case 0: {
                NanoVGHelper.INSTANCE.setupAndDraw(true, (vg) -> {
                    NanoVGHelper.INSTANCE.drawRect(vg, x, y, width, height, bgColor);
                });
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
                NanoVGHelper.INSTANCE.setupAndDraw(true, (vg) -> {
                    SbbRenderer.drawBox(vg, x, y, width, height, this.bgColor, this.shadow, (int) this.cornerRadius);
                });
//                if (this.shadow) {
//                    SbbRenderer.drawBox(x, y, width, height, this.bgColor, this.shadowColor, (int) this.cornerRadius);
//                } else {
//                    SbbRenderer.drawBox(x, y, width, height, this.bgColor, false, (int) this.cornerRadius);
//                }
            }
        }
    }

    protected void drawLine(String line, float x, float y, float scale) {
        TextRenderer.drawScaledString(line, x, y, textColor.getRGB(), TextRenderer.TextType.toType(textType), scale);
    }

    protected float getLineWidth(String line, float scale) {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(line) * scale;
    }

    @Override
    protected float getWidth(float scale, boolean example) {
        if (text == null) return 0;
        float width = 0;
        for (String line : text.split("\n")) {
            width = Math.max(width, getLineWidth(line, scale));
        }
        return width;
    }

    @Override
    protected float getHeight(float scale, boolean example) {
        return text == null ? 0 : (text.split("\n").length * 12 - 4) * scale;
    }

    protected abstract String getText(boolean example);

    public boolean shouldDrawBackground() {
        return text != null && text.split("\n").length > 0;
    }

    @Checkbox(
        name = "Enabled",
        description = "If the background of the HUD is enabled."
    )
    protected boolean background = true;
    @Color(
        name = "Background color:",
        description = "The color of the background.",
        allowAlpha = true
    )
    protected OneColor bgColor = new OneColor(0, 0, 0, 125);
    @Checkbox(
        name = "Shadow",
        description = "If the background has a shadow."
    )
    protected boolean shadow = true;
    @Color(
        name = "Shadow Background color:",
        description = "The color of the background.",
        allowAlpha = true
    )
    protected OneColor shadowColor = new OneColor(0, 0, 0, 52);

    @Dropdown(
        name = "Corner Type:",
        options = {"Square", "Rounded", "Boxes"}
    )
    protected int cornerType = 2;

    @Slider(
        name = "Corner radius:",
        description = "The corner radius of the background.",
        min = 0,
        max = 10
    )
    protected float cornerRadius = 4;

    @Color(
        name = "Text Color"
    )
    protected OneColor textColor = new OneColor(255, 255, 255);

    @Dropdown(
        name = "Text Type",
        options = {"No Shadow", "Shadow", "Full Shadow"}
    )
    protected int textType = 0;
}
