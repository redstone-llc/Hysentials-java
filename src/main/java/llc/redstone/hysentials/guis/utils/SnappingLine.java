package llc.redstone.hysentials.guis.utils;

import cc.polyfrost.oneconfig.libs.universal.UResolution;
import cc.polyfrost.oneconfig.renderer.NanoVGHelper;
import llc.redstone.hysentials.util.Renderer;
import llc.redstone.hysentials.util.Renderer;

import java.awt.*;

public class SnappingLine {
    private static final int COLOR = new Color(138, 43, 226).getRGB();
    private final float line;
    private final float distance;
    private final float position;

    public SnappingLine(float line, float left, float size, boolean multipleSides) {
        this.line = line;
        float center = left + size / 2f;
        float right = left + size;
        float leftDistance = Math.abs(line - left);
        float centerDistance = Math.abs(line - center);
        float rightDistance = Math.abs(line - right);
        if (!multipleSides || leftDistance <= centerDistance && leftDistance <= rightDistance) {
            distance = leftDistance;
            position = line;
        } else if (centerDistance <= rightDistance) {
            distance = centerDistance;
            position = line - size / 2f;
        } else {
            distance = rightDistance;
            position = line - size;
        }
    }

    public void drawLine(float lineWidth, boolean isX) {
        float pos = (float) (line * UResolution.getScaleFactor() - lineWidth) / 2f;
        if (isX) {
            Renderer.drawLine(COLOR, pos, 0, pos, UResolution.getWindowHeight(), lineWidth, 7);
        } else {
            Renderer.drawLine(COLOR, 0, pos, UResolution.getWindowWidth(), pos, lineWidth, 7);
        }
    }

    public float getPosition() {
        return position;
    }

    public float getDistance() {
        return distance;
    }
}
