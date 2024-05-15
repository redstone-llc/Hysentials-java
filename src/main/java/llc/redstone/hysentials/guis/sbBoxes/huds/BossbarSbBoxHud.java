package llc.redstone.hysentials.guis.sbBoxes.huds;

import cc.polyfrost.oneconfig.config.annotations.Exclude;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.hud.Hud;
import cc.polyfrost.oneconfig.libs.universal.UGraphics;
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import llc.redstone.hysentials.hook.BossStatusHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.boss.BossStatus;

// Taken from Polyfrost's VanillaHUD mod
// LGPL-3.0 license (https://github.com/Polyfrost/VanillaHUD)
public class BossbarSbBoxHud extends SBBoxesTextHud {
    @Switch(
        name = "Render Text"
    )
    public boolean renderText = true;

    @Switch(
        name = "Render Health"
    )
    public boolean renderHealth = true;

    @Switch(
        name = "Smooth Health",
        description = "Lerps the health bar to make it smoother. Similar to the boss bar progress in modern versions of Minecraft."
    )
    public boolean smoothHealth = true;

    @Slider(
        name = "Lerp Speed",
        min = 1,
        max = 1000
    )
    public float lerpSpeed = 100;

    @Slider(
        name = "Bar Position",
        min = 0,
        max = 100
    )
    public float barPosition = 50;

    @Exclude
    public static final int BAR_WIDTH = 182;
    @Exclude
    public static final Minecraft mc = UMinecraft.getMinecraft();
    @Exclude
    public static final FontRenderer fontRenderer = UMinecraft.getFontRenderer();


    public BossbarSbBoxHud() {
        super(true, 1920f / 2, 2f, 1, true, 2, new OneColor(0, 0, 0, 125), true, new OneColor(0, 0, 0, 52), new OneColor(255, 255, 255));
    }

    @Override
    protected boolean shouldShow() {
        return drawingBossBar();
    }

    public boolean drawingBossBar() {
        return this.isBossActive() && super.shouldShow();
    }

    @Override
    protected String getText(boolean example) {
        return this.isBossActive() ? BossStatus.bossName : "Wither";
    }

    private boolean isBossActive() {
        return BossStatus.bossName != null && BossStatus.statusBarTime > 0;
    }

    public void drawHealth(String bossName, float healthScale, float x, float y) {
        if (this.isBossActive()) {
            --BossStatus.statusBarTime;
        }

        UGraphics.enableBlend();
        UGraphics.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(Gui.icons);

        if (this.renderText && fontRenderer.getStringWidth(bossName) > BAR_WIDTH) {
            x += (fontRenderer.getStringWidth(bossName) - BAR_WIDTH) * this.barPosition / 100.0F;
        }

        float remainingHealth = healthScale * BAR_WIDTH;
        if (this.renderHealth) {
            mc.ingameGUI.drawTexturedModalRect(x, y, 0, 74, BAR_WIDTH, 5);
            mc.ingameGUI.drawTexturedModalRect(x, y, 0, 74, BAR_WIDTH, 5);
            if (remainingHealth > 0) {
                mc.ingameGUI.drawTexturedModalRect(x, y, 0, 79, (int) remainingHealth, 5);
            }
        }
        UGraphics.disableBlend();
    }

    @Override
    public void draw(UMatrixStack matrices, float x, float y, float scale, boolean example) {

        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        GlStateManager.translate(x / scale, y / scale, 1);
        this.drawHealth(this.getText(example), this.isBossActive() ? smoothHealth ? BossStatusHook.getPercent() : BossStatus.healthScale : 0.8f, 5f, (this.renderText ? 10 : 0) + 2.5f);
        GlStateManager.popMatrix();


        GlStateManager.scale(scale, scale, 1);
        GlStateManager.translate(x / scale, y / scale, 1);
        if (this.renderText) {
            super.draw(matrices, this.getWidth(1, example) / 2 - (float) (fontRenderer.getStringWidth(this.getText(example)) / 2), 2.5f, 1, example);
        }
    }

    @Override
    protected float getWidth(float scale, boolean example) {
        float textWidth = this.renderText ? UMinecraft.getFontRenderer().getStringWidth(getText(example)) : 0.0f;
        float healthWidth = this.renderHealth ? BAR_WIDTH : 0.0f;
        return Math.max(textWidth, healthWidth) * scale + 10f * scale;
    }

    @Override
    protected float getHeight(float scale, boolean example) {
        float height = 0.0f;

        if (this.renderHealth) {
            height += 5.0F;
        }

        if (this.renderText) {
            height += 9.0F;
        }

        if (this.renderText && this.renderHealth) {
            height += 1.0F;
        }

        return height * scale + 5.0F * scale;
    }
}
