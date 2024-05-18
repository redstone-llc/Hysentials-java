package llc.redstone.hysentials.config.hysentialmods.utils;

import cc.polyfrost.oneconfig.gui.elements.BasicElement;
import cc.polyfrost.oneconfig.renderer.NanoVGHelper;
import cc.polyfrost.oneconfig.renderer.asset.SVG;
import cc.polyfrost.oneconfig.utils.InputHandler;

public class DeleteElement extends BasicElement {
    public static final SVG DELETE_ICON = new SVG("/assets/hysentials/gui/delete.svg");
    public DeleteElement(int width, int height) {
        super(width, height, true);
    }

    @Override
    public void draw(long vg, float x, float y, InputHandler inputHandler) {
        update(x, y, inputHandler);
        final NanoVGHelper nanoVGHelper = NanoVGHelper.INSTANCE;

        nanoVGHelper.drawRoundedRect(vg, x, y, width, height, new java.awt.Color(255, 48, 48, 255).getRGB(), 12f);
        nanoVGHelper.drawSvg(vg, DELETE_ICON, x + width /16f, y + height /16f, width * 7/8f, height * 7/8f);
    }
}
