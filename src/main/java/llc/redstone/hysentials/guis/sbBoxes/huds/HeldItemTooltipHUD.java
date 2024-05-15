package llc.redstone.hysentials.guis.sbBoxes.huds;

import cc.polyfrost.oneconfig.config.annotations.Exclude;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.hud.TextHud;
import cc.polyfrost.oneconfig.libs.universal.UGraphics;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.renderer.NanoVGHelper;
import cc.polyfrost.oneconfig.renderer.TextRenderer;
import cc.polyfrost.oneconfig.utils.color.ColorUtils;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.handlers.sbb.SbbRenderer;
import llc.redstone.hysentials.mixin.GuiIngameAccessor;
import llc.redstone.hysentials.mixin.GuiSpectatorAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.util.EnumChatFormatting;

public class HeldItemTooltipHUD extends SBBoxesTextHud {

    @Switch(name = "Fade Out")
    private static boolean fadeOut = true;

    @Switch(name = "Instant Fade")
    private static boolean instantFade = false;

    @Exclude
    private int opacity;

    @Exclude
    private static String specText;

    @Exclude
    private static final String EXAMPLE_TEXT = "Item Tooltip";

    @Exclude
    private static final Minecraft mc = UMinecraft.getMinecraft();

    public HeldItemTooltipHUD() {
        super(true, 1920f / 2, 1080f - 51f, 1, true, 2, new OneColor(0, 0, 0, 125), true, new OneColor(0, 0, 0, 52), new OneColor(255, 255, 255));
        this.textType = 1;
    }

    @Override
    protected void drawLine(String line, float x, float y, float scale) {
        UGraphics.enableBlend();
        int color = ColorUtils.setAlpha(this.textColor.getRGB(), Math.min(this.textColor.getAlpha(), this.opacity));
        TextRenderer.drawScaledString(line, x + ((getWidth(scale, false) - getLineWidth(line, scale)) / 2), y + ((getHeight(scale, false) - (8 * scale)) / 2), color | this.opacity << 24, TextRenderer.TextType.toType(this.textType), scale);
        UGraphics.disableBlend();
    }

    @Override
    protected void drawBackground(float x, float y, float width, float height, float scale, boolean example) {
        int bgColor = ColorUtils.setAlpha(this.bgColor.getRGB(), Math.min(this.bgColor.getAlpha(), this.opacity));
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
            }
        }
    }

    @Override
    protected boolean shouldShow() {
        if (Hysentials.INSTANCE.isApec()) { // I love Apec Mod Minecraft
            return false;
        }
        GuiIngameAccessor ingameGUI = (GuiIngameAccessor) mc.ingameGUI;

        int o = fadeOut ? (int) ((float) ingameGUI.getRemainingHighlightTicks() * 256.0F / 10.0F) : 255;
        if (o > 255) {
            o = 255;
        }
        opacity = instantFade ? 255 : o;
        String spectatorText = null;
        if (mc.thePlayer != null && mc.thePlayer.isSpectator()) {
            GuiSpectatorAccessor spectatorAccessor = (GuiSpectatorAccessor) mc.ingameGUI.getSpectatorGui();
            int i = (int) (spectatorAccessor.alpha() * 255.0F);
            if (i > 3 && spectatorAccessor.getField_175271_i() != null) {
                ISpectatorMenuObject iSpectatorMenuObject = spectatorAccessor.getField_175271_i().func_178645_b();
                spectatorText = iSpectatorMenuObject != SpectatorMenu.field_178657_a ? iSpectatorMenuObject.getSpectatorName().getFormattedText() : spectatorAccessor.getField_175271_i().func_178650_c().func_178670_b().getFormattedText();
                if (spectatorText != null) {
                    specText = spectatorText;
                    opacity = instantFade ? 255 : i;
                }
            }
        }

        return (o > 0 || spectatorText != null) && super.shouldShow();
    }

    @Override
    protected String getText(boolean example) {
        GuiIngameAccessor ingameGUI = (GuiIngameAccessor) mc.ingameGUI;
        if (example) return EXAMPLE_TEXT;
        if (mc.thePlayer != null && mc.thePlayer.isSpectator()) {
            GuiSpectatorAccessor spectatorAccessor = (GuiSpectatorAccessor) mc.ingameGUI.getSpectatorGui();
            int i = (int) (spectatorAccessor.alpha() * 255.0F);
            if (i > 3 && spectatorAccessor.getField_175271_i() != null) {
                return specText;
            }
        } else if ((ingameGUI.getRemainingHighlightTicks() > 0 || !fadeOut) && ingameGUI.getHighlightingItemStack() != null) {
            String string = ingameGUI.getHighlightingItemStack().getDisplayName();
            if (ingameGUI.getHighlightingItemStack().hasDisplayName()) {
                string = EnumChatFormatting.ITALIC + string;
            }
            return string;

        }
        return "";
    }

    @Override
    protected float getHeight(float scale, boolean example) {
        return super.getHeight(scale, example) + 5 * scale;
    }

    @Override
    protected float getWidth(float scale, boolean example) {
        return super.getWidth(scale, example) + 10 * scale;
    }
}
