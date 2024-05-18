package llc.redstone.hysentials.guis.utils;

import cc.polyfrost.oneconfig.hud.Hud;
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack;
import llc.redstone.hysentials.config.EditorConfig;
import llc.redstone.hysentials.event.events.GuiMouseClickEvent;
import net.minecraft.client.renderer.GlStateManager;

public class SBBoxesHud extends Hud {
    public SBBoxes box;

    public SBBoxesHud(SBBoxes box) {
        super(box.isEnabled(), box.getX(), box.getY());
        this.box = box;
        scale = box.getScale();
        position.setSize(getWidth(scale, true), getHeight(scale, true));
        box.setDisplay(box.getText());
    }


    @Override
    public void draw(UMatrixStack matrices, float x, float y, float scale, boolean example) {
        box.setX((int) x);
        box.setY((int) y);
        box.setScale(scale);
        GlStateManager.pushMatrix();
        box.draw();
        GlStateManager.popMatrix();
    }

    public void mouseClick(GuiMouseClickEvent event) {
        // This is a custom method that is called when the mouse is clicked on the HUD
        new EditorConfig(box).openGuI();
    }

    @Override
    protected boolean shouldShow() {
        return super.shouldShow() && box.canRender();
    }

    @Override
    public boolean isEnabled() {
        return box.canRender();
    }

    @Override
    protected float getWidth(float scale, boolean example) {
        if (box == null) {
            return 0; // Grr polyfrost (love you guys though)
        }
        return box.getWidth(box.getDisplay()) + 1;
    }

    @Override
    protected float getHeight(float scale, boolean example) {
        if (box == null) {
            return 0; // Grr polyfrost (love you guys though)
        }
        return box.getHeight(null) + 1;
    }
}
