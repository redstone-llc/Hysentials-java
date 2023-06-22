package cc.woverflow.hysentials.handlers.sbb;

import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.woverflow.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.guis.sbBoxes.SBBoxesEditor;
import cc.woverflow.hysentials.handlers.redworks.HousingScoreboard;
import cc.woverflow.hysentials.util.Renderer;
import cc.woverflow.hysentials.util.ScoreboardWrapper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import scala.Int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static cc.woverflow.hysentials.guis.actionLibrary.ClubActionViewer.toList;
import static net.minecraft.client.Minecraft.getMinecraft;

public class SbbRenderer {
    public static HousingScoreboard housingScoreboard;

    public SbbRenderer() {
        housingScoreboard = new HousingScoreboard();
    }

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        GL11.glPushMatrix();
        Actionbar.actionBar();
        if (HysentialsConfig.scoreboard) {
            Scoreboard.scoreboard();
        } else {
            GuiIngameForge.renderObjective = true;
        }
        ArrayList<ScoreboardWrapper.ScoreWrapper> lines = ScoreboardWrapper.getScoreboard().getSortedScores(ScoreboardWrapper.getSidebar()).stream().map(ScoreboardWrapper.ScoreWrapper::new).collect(Collectors.toCollection(ArrayList::new));
        for (ScoreboardWrapper.ScoreWrapper line : lines) {

            JSONArray ls = Hysentials.INSTANCE.sbBoxes.jsonObject.getJSONArray("lines");
            String s = SBBoxesEditor.removeHiddenCharacters(line.toString());
            Optional<Object> l = ls.toList().stream().filter(o -> s.matches((String) ((HashMap<String, Object>) o).get("regex"))).findFirst();
            if (!l.isPresent()) continue;
            JSONObject lineData = new JSONObject((Map) l.get());
            try {
                Matcher matcher = Pattern.compile(lineData.getString("regex")).matcher(s);
                String display = lineData.getString("display");
                if (matcher.find()) {
                    for (int i = 1; i <= matcher.groupCount(); i++) {
                        display = display.replace("{$" + i + "}", matcher.group(i));
                    }
                }
                if (!lineData.getBoolean("enabled")) continue;
                if (!lineData.getString("title").equals("") && !ChatColor.Companion.stripControlCodes(ScoreboardWrapper.getTitle()).equals(lineData.getString("title")))
                    continue;
                lineData.put("text", display);
                ls.put(ls.toList().indexOf(l.get()), lineData);

                double scale = lineData.getDouble("scale");
                double width = (getMinecraft().fontRendererObj.getStringWidth(display)) * scale;
                double height = 15 * scale;

                double scaledX = lineData.getInt("x") + (10 * scale) / 2;
                double scaledY = lineData.getInt("y") + (6 * scale) / 2;

                drawBox(
                    lineData.getInt("x"),
                    lineData.getInt("y"),
                    (float) (width + 10 * scale),
                    (float) height,
                    HysentialsConfig.boxColor,
                    HysentialsConfig.boxShadows,
                    new Integer[]{0, 2, 4}[HysentialsConfig.scoreboardBoxesBorderRadius]
                );
                GL11.glPushMatrix();
                GL11.glScaled(lineData.getDouble("scale"), lineData.getDouble("scale"), 1);
                Renderer.drawString(display, (float) (scaledX / scale), (float) (scaledY / scale));
                GL11.glScalef(1, 1, 1);
                GL11.glPopMatrix();
            } catch (Exception e) {
                MUtils.chat("&cError occured while rendering scoreboard box: &e" + e.getMessage());
                MUtils.chat("&cRemoving this line from the config file to prevent further issues...");
                MUtils.chat("&cPlease report this issue to the developer if it continues!");
                int i = toList(Hysentials.INSTANCE.sbBoxes.jsonObject.getJSONArray("lines")).indexOf(lineData);
                Hysentials.INSTANCE.sbBoxes.jsonObject.getJSONArray("lines").remove(i);
            }
        }
        GL11.glPopMatrix();
    }

    int tick = 0;

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (event.type != TickEvent.Type.CLIENT) return;
        ScoreboardWrapper.resetCache();
        if (++tick % (60 * 20 * 5) == 0) {
            Hysentials.INSTANCE.sbBoxes.save();
        }

        if (SBBoxesEditor.configGui != null && SBBoxesEditor.configGui.isClosed && SBBoxesEditor.isConfigOpen) {
            SBBoxesEditor.configGui = null;
            SBBoxesEditor.isConfigOpen = false;
            Multithreading.schedule(() -> new SBBoxesEditor().show(), 100, TimeUnit.MILLISECONDS);
        }
    }

    @SubscribeEvent
    public void onMouseClick(MouseEvent event) {
        // add new dragged
        if (Mouse.isButtonDown(event.button)) {
            draggedState.put(event.button, new State(getMouseX(), getMouseY()));
        } else draggedState.remove(event.button);
    }

    @SubscribeEvent
    public void onGuiMouseInput(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (getMinecraft().theWorld == null) {
            draggedState.clear();
            return;
        }
        // add new dragged
        if (Mouse.isButtonDown(Mouse.getEventButton())) {
            draggedState.put(Mouse.getEventButton(), new State(getMouseX(), getMouseY()));
        } else draggedState.remove(Mouse.getEventButton());
    }

    public static void drawBox(float x, float y, float width, float height, OneColor color, boolean boxShadows, int radius) {
        x = (float) Math.round(x);
        y = (float) Math.round(y);
        width = (float) Math.round(width);
        height = (float) Math.round(height);
        long boxColor = Renderer.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        Renderer.drawRect(boxColor, x + radius, y, width - (radius * 2), height);
        Renderer.drawRect(boxColor, x, y + radius, radius, height - (2 * radius));
        Renderer.drawRect(boxColor, x + width - radius, y + radius, radius, height - (2 * radius));

        if (boxShadows) {
            long shadowColor = Renderer.color(color.getRed(), color.getGreen(), color.getBlue(), (long) (color.getAlpha() * 0.42F));
            Renderer.drawRect(shadowColor, x + width, y + (2 * radius), radius, height - (2 * radius));
            if (radius != 0) {
                Renderer.drawRect(shadowColor, x + width - radius, y + height - radius, radius, radius);
            }
            Renderer.drawRect(shadowColor, x + (2 * radius), y + height, width - (2 * radius), radius);
        }
    }


    private static Map<Integer, State> draggedState = new HashMap<>();

    public static class State {
        private final double x;
        private final double y;

        public State(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }

    public static float getMouseX() {
        float mx = Mouse.getX();
        float rw = new ScaledResolution(getMinecraft()).getScaledWidth();
        float dw = getMinecraft().displayWidth;
        return mx * rw / dw;
    }

    public static float getMouseY() {
        float my = Mouse.getY();
        float rh = new ScaledResolution(getMinecraft()).getScaledHeight();
        float dh = getMinecraft().displayHeight;
        return rh - my * rh / dh - 1f;
    }
}
