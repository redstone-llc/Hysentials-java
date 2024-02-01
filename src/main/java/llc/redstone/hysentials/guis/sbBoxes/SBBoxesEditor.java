package llc.redstone.hysentials.guis.sbBoxes;

import cc.polyfrost.oneconfig.gui.OneConfigGui;
import cc.polyfrost.oneconfig.hud.Hud;
import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import cc.polyfrost.oneconfig.libs.universal.UResolution;
import cc.polyfrost.oneconfig.utils.Multithreading;
import llc.redstone.hysentials.GuiIngameHysentials;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.EditorConfig;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.guis.HysentialsGui;
import llc.redstone.hysentials.guis.utils.GrabOffset;
import llc.redstone.hysentials.guis.utils.Position;
import llc.redstone.hysentials.guis.utils.SBBoxes;
import llc.redstone.hysentials.guis.utils.SnappingLine;
import llc.redstone.hysentials.util.Renderer;
import llc.redstone.hysentials.util.ScoreboardWrapper;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.EditorConfig;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.guis.HysentialsGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.GuiIngameForge;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static llc.redstone.hysentials.utils.StringUtilsKt.stripControlCodes;

public class SBBoxesEditor extends HysentialsGui {
    static boolean scroll = true;
    int scrollAmount = 0;
    static Renderer.IconButton collapseIcon;
    static Renderer.IconButton expandIcon;
    public static OneConfigGui configGui;
    public static boolean isConfigOpen = false;
    private static RegexCreator regexCreator;

    private static final int SNAPPING_DISTANCE = 10;
    private final HashMap<SBBoxes, GrabOffset> editingHuds = new HashMap<>();
    private boolean isDragging;

    public SBBoxesEditor() {

    }

    @Override
    protected void pack() {

    }

    public static void initGUI() {
        collapseIcon = new Renderer.IconButton("https://i.imgur.com/9KWjNaB.png", 20, 20, (i) -> {
            scroll = false;
        });
        expandIcon = new Renderer.IconButton("https://i.imgur.com/96VPpZ7.png", 20, 20, (i) -> {
            scroll = true;
        });
        regexCreator = new RegexCreator();
    }

    ArrayList<ScoreboardWrapper.ScoreWrapper> lines = ScoreboardWrapper.getScoreboard().getSortedScores(ScoreboardWrapper.getSidebar()).stream().map(ScoreboardWrapper.ScoreWrapper::new).collect(Collectors.toCollection(ArrayList::new));
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.pushMatrix();
        ScaledResolution res = new ScaledResolution(mc);
        int radius = new Integer[]{0, 2, 4}[HysentialsConfig.scoreboardBoxesBorderRadius];

        int lineWidth = Math.max(1, Math.round(Math.min(UResolution.getWindowWidth() / 1920f, UResolution.getWindowHeight() / 1080f)));
        if (isDragging) {
            setHudPositions(mouseX, mouseY, true, true, lineWidth);
        }

        drawRect(0, 0, res.getScaledWidth(), res.getScaledHeight(), (int) Renderer.color(0, 0, 0, 75));

        if (scroll) {
            drawRect(0, 0, 200, res.getScaledHeight(), (int) Renderer.color(0, 0, 0, 125));
            fontRendererObj.drawString("§eScoreboard Boxes", 100 - fontRendererObj.getStringWidth("§eScoreboard Boxes") / 2, 5 + this.scrollAmount, (int) Renderer.color(255, 255, 255, 255));
            collapseIcon.draw(200, res.getScaledHeight() / 2 - 16);
            for (int i = 0; i < SBBoxes.boxes.size(); i++) {
                SBBoxes sbBox = SBBoxes.boxes.get(i);
                drawRect(0, 15 + 30 * i + 3 * i + this.scrollAmount, 200, 30, (int) Renderer.color(0, 0, 0, 150));
                if (mouseX > 0 && mouseX < 200 && mouseY > 15 + 30 * i + 3 * i + this.scrollAmount && mouseY < 15 + 30 * i + 3 * i + 30 + this.scrollAmount) {
                    drawRect(0, 15 + 30 * i + 3 * i + this.scrollAmount, 200, 30, (int) Renderer.color(0, 0, 0, 175));
                    fontRendererObj.drawString("§eClick to edit", mouseX + 5, mouseY - 10, (int) Renderer.color(255, 255, 255, 255));
                }
                fontRendererObj.drawString("§eText: §r" + sbBox.getText(), 100 - fontRendererObj.getStringWidth("§eText: §r" + sbBox.getText()) / 2, 20 + 30 * i + 3 * i + this.scrollAmount, (int) Renderer.color(255, 255, 255, 255));
                fontRendererObj.drawString("§ePosition: §r(" + sbBox.getX() + ", " + sbBox.getY() + ")", 100 - fontRendererObj.getStringWidth("§ePosition: §r(" + sbBox.getX() + ", " + sbBox.getY() + ")") / 2, 30 + 30 * i + 3 * i + this.scrollAmount, (int) Renderer.color(255, 255, 255, 255));
            }
        } else {
            expandIcon.draw(0, res.getScaledHeight() / 2 - 16);
        }

        int[] pos = getScoreboardPosition();
        if (pos == null) {
            GlStateManager.popMatrix();
            return;
        }
        int startX = pos[0];
        int startY = pos[1];
        int endX = pos[2];

        if (HysentialsConfig.scoreboard) {
            for (int i = 0; i < lines.size(); i++) {
                if (isMouseOverLine(mouseX, mouseY, i)) {
                    ScoreboardWrapper.ScoreWrapper line = lines.get(lines.size() - i - 1);
                    drawRect(startX, startY + 10 * i + 3, endX - startX, 9, (int) Renderer.color(0, 0, 0, 125));
                    drawRect((int) (startX - fontRendererObj.getStringWidth("§eLeft Click to toggle") - 2.5), startY + 10 * i + 3, startX, 9, (int) Renderer.color(0, 0, 0, 125));
                    Renderer.drawString("§eLeft Click to toggle", startX - fontRendererObj.getStringWidth("§eLeft Click to toggle"), startY + 10 * i + 3.5f);
                    Renderer.drawString(line.toString(), (float) (startX + radius), startY + 10 * i + 3.5f);
                }
            }
        } else {
            for (int i = 0; i < lines.size(); i++) {
                if (isMouseOverLine(mouseX, mouseY, i)) {
                    ScoreboardWrapper.ScoreWrapper line = lines.get(lines.size() - i - 1);
                    drawRect(startX, startY + 10 * i + 1, endX - startX, 9, (int) Renderer.color(0, 0, 0, 125));
                    drawRect((int) (startX - fontRendererObj.getStringWidth("§eLeft Click to toggle") - 2.5), startY + 9 * i + 1, startX, 9, (int) Renderer.color(0, 0, 0, 125));
                    fontRendererObj.drawString("§eLeft Click to toggle", startX - fontRendererObj.getStringWidth("§eLeft Click to toggle"), startY + 9 * i + 2, (int) Renderer.color(255, 255, 255, 255));
                    fontRendererObj.drawString(line.toString(), startX + 2, (int) (startY + 9 * i + 0.5), (int) Renderer.color(255, 255, 255, 255));
                }
            }
        }
        GlStateManager.popMatrix();
    }

    static boolean dragging = false;
    static SBBoxes draggingLine = null;

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (isClosed) return;
        isDragging = false;
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (isDragging) return;
        editingHuds.forEach((hud, grabOffset) -> grabOffset.setOffset(-hud.position.getX(), -hud.position.getY()));
        if (keyCode == UKeyboard.KEY_UP) {
            setHudPositions(0f, -1f, false);
        } else if (keyCode == UKeyboard.KEY_DOWN) {
            setHudPositions(0f, 1f, false);
        } else if (keyCode == UKeyboard.KEY_LEFT) {
            setHudPositions(-1f, 0f, false);
        } else if (keyCode == UKeyboard.KEY_RIGHT) {
            setHudPositions(1f, 0f, false);
        }
    }

    public SBBoxes getBoxOverMouse(int mouseX, int mouseY) {
        ArrayList<ScoreboardWrapper.ScoreWrapper> ls = ScoreboardWrapper.getScoreboard().getSortedScores(ScoreboardWrapper.getSidebar()).stream().map(ScoreboardWrapper.ScoreWrapper::new).collect(Collectors.toCollection(ArrayList::new));

        for (ScoreboardWrapper.ScoreWrapper l : ls) {
            SBBoxes hud = SBBoxes.getFromMatch(l.getName());
            if (hud == null) continue;
            if (!hud.isEnabled()) continue;
            if (!hud.getTitle().equals("") && !stripControlCodes(ScoreboardWrapper.getTitle()).equals(hud.getTitle())) continue;
            if (mouseX >= hud.getX() && mouseX <= hud.getX() + hud.getWidth(l.getName()) && mouseY >= hud.getY() && mouseY <= hud.getY() + hud.getHeight(l.getName())) {
                return hud;
            }
        }
        return null;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 1) {
            SBBoxes box = getBoxOverMouse(mouseX, mouseY);
            if (box != null) {
                configGui = new EditorConfig(box).openGuI();
                Multithreading.schedule(() -> {
                    isConfigOpen = true;
                }, 1000, TimeUnit.MILLISECONDS);
            }
            return;
        }

        if (mouseButton == 0) {
            isDragging = false;
            { // Dragging
                ArrayList<ScoreboardWrapper.ScoreWrapper> ls = ScoreboardWrapper.getScoreboard().getSortedScores(ScoreboardWrapper.getSidebar()).stream().map(ScoreboardWrapper.ScoreWrapper::new).collect(Collectors.toCollection(ArrayList::new));

                for (ScoreboardWrapper.ScoreWrapper l : ls) {
                    SBBoxes hud = SBBoxes.getFromMatch(l.getName());
                    if (hud == null) continue;
                    if (!hud.isEnabled() || (getBoxOverMouse(mouseX, mouseY) != hud)) continue;
                    if (!editingHuds.containsKey(hud)) {
                        if (!UKeyboard.isCtrlKeyDown()) editingHuds.clear();
                        editingHuds.put(hud, new GrabOffset());
                    }
                    isDragging = true;
                    editingHuds.forEach((hud2, grabOffset) -> grabOffset.setOffset((float) (mouseX - hud2.position.getX()), (float) (mouseY - hud2.position.getY())));
                    return;
                }
            }

            if (scroll & mouseX < 200) {
                for (int i = 0; i < SBBoxes.boxes.size(); i++) {
                    SBBoxes line = SBBoxes.boxes.get(i);
                    if (mouseX > 0 && mouseY > 15 + 30 * i + 3 * i + this.scrollAmount && mouseY < 15 + 30 * i + 3 * i + 30 + this.scrollAmount) {
                        configGui = new EditorConfig(line).openGuI();
                        Multithreading.schedule(() -> {
                            isConfigOpen = true;
                        }, 1000, TimeUnit.MILLISECONDS);
                    }
                }
                return;
            }
            collapseIcon.click(mouseX, mouseY);
            expandIcon.click(mouseX, mouseY);

            for (int i = 0; i < lines.size(); i++) {
                if (isMouseOverLine(mouseX, mouseY, i)) {
                    ScoreboardWrapper.ScoreWrapper line = lines.get(lines.size() - i - 1);
                    String s = removeHiddenCharacters(line.toString());
                    SBBoxes hud = SBBoxes.getFromMatch(s);
                    if (hud != null) {
                        hud.setEnabled(!hud.isEnabled());
                    } else {
                        JSONObject l = new JSONObject();
                        String[] regexDisplay = regexCreator.createRegex(s);
                        l.put("text", s);
                        l.put("regex", regexDisplay[0]);
                        l.put("display", regexDisplay[1]);
                        l.put("enabled", true);
                        l.put("scale", 1);
                        l.put("title", ChatColor.Companion.stripControlCodes(ScoreboardWrapper.getTitle()));
                        l.put("x", 100);
                        l.put("y", 100);
                        SBBoxes.boxes.add(new SBBoxes(l));
                    }
                    JSONArray array = new JSONArray();
                    for (SBBoxes box : SBBoxes.boxes) {
                        array.put(box.save());
                    }
                    JSONObject object = new JSONObject();
                    object.put("lines", array);
                    Hysentials.INSTANCE.sbBoxes.jsonObject = object;
                    Hysentials.INSTANCE.sbBoxes.save();
                }
            }
        }
    }

    //scrolling
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (scroll) {
            int i = Mouse.getEventDWheel();
            if (i != 0) {
                if (i > 1) {
                    i = 1;
                }
                if (i < -1) {
                    i = -1;
                }
                if (i > 0) {
                    i = -1;
                } else {
                    i = 1;
                }
                this.scrollAmount += -i * 10;
                if (this.scrollAmount > 0) {
                    this.scrollAmount = 0;
                }
                if (this.scrollAmount < (-30 * SBBoxes.boxes.size() - 3 * SBBoxes.boxes.size() + 10) + 462) {
                    this.scrollAmount = (-30 * SBBoxes.boxes.size() - 3 * SBBoxes.boxes.size() + 10) + 462;
                }
            }
        }
    }

    private void setHudPositions(float mouseX, float mouseY, boolean locked) {
        setHudPositions(mouseX, mouseY, false, locked, 0);
    }

    private void setHudPositions(float mouseX, float mouseY, boolean snap, boolean locked, int lineWidth) {
        for (SBBoxes hud : editingHuds.keySet()) {
            GrabOffset grabOffset = editingHuds.get(hud);
            Position position = hud.position;
            float x = mouseX - grabOffset.getX();
            float y = mouseY - grabOffset.getY();

            if (editingHuds.size() == 1 && snap) {
                x = getXSnapping(lineWidth, x, position.getWidth(), true);
                y = getYSnapping(lineWidth, y, position.getHeight(), true);
            }

            if (locked) {
                if (x < 0) x = 0;
                else if (x + position.getWidth() > UResolution.getScaledWidth())
                    x = UResolution.getScaledWidth();
                if (y < 0) y = 0;
                else if (y + position.getHeight() > UResolution.getScaledHeight())
                    y = UResolution.getScaledHeight();
            }

            position.setPosition(x, y);
        }
    }

    private float getXSnapping(float lineWidth, float x, float width, boolean multipleSides) {
        ArrayList<Float> lines = getXSnappingLines();
        ArrayList<SnappingLine> snappingLines = new ArrayList<>();
        float closest = (float) (SNAPPING_DISTANCE / UResolution.getScaleFactor());
        for (Float line : lines) {
            SnappingLine snappingLine = new SnappingLine(line, x, width, multipleSides);
            if (Math.round(snappingLine.getDistance()) == Math.round(closest)) snappingLines.add(snappingLine);
            else if (snappingLine.getDistance() < closest) {
                closest = snappingLine.getDistance();
                snappingLines.clear();
                snappingLines.add(snappingLine);
            }
        }
        if (snappingLines.isEmpty()) return x;
        for (SnappingLine snappingLine : snappingLines) {
            snappingLine.drawLine(lineWidth, true);
        }
        return snappingLines.get(0).getPosition();
    }

    private ArrayList<Float> getXSnappingLines() {
        ArrayList<Float> lines = new ArrayList<>();
        lines.add(UResolution.getScaledWidth() / 2f);

        for (ScoreboardWrapper.ScoreWrapper l : this.lines) {
            SBBoxes hud = SBBoxes.getFromMatch(l.getName());
            if (hud == null) continue;
            if (!hud.isEnabled() || editingHuds.containsKey(hud)) continue;
            lines.add(hud.position.getX());
            lines.add(hud.position.getCenterX());
            lines.add(hud.position.getRightX());
        }
        return lines;
    }

    private float getYSnapping(float lineWidth, float y, float height, boolean multipleSides) {
        ArrayList<Float> lines = getYSnappingLines();
        ArrayList<SnappingLine> snappingLines = new ArrayList<>();
        float closest = (float) (SNAPPING_DISTANCE / UResolution.getScaleFactor());
        for (Float line : lines) {
            SnappingLine snappingLine = new SnappingLine(line, y, height, multipleSides);
            if (Math.round(snappingLine.getDistance()) == Math.round(closest)) snappingLines.add(snappingLine);
            else if (snappingLine.getDistance() < closest) {
                closest = snappingLine.getDistance();
                snappingLines.clear();
                snappingLines.add(snappingLine);
            }
        }
        if (snappingLines.isEmpty()) return y;
        for (SnappingLine snappingLine : snappingLines) {
            snappingLine.drawLine(lineWidth, false);
        }
        return snappingLines.get(0).getPosition();
    }

    private ArrayList<Float> getYSnappingLines() {
        ArrayList<Float> lines = new ArrayList<>();
        lines.add(UResolution.getScaledHeight() / 2f);

        for (ScoreboardWrapper.ScoreWrapper l : this.lines) {
            SBBoxes hud = SBBoxes.getFromMatch(l.getName());
            if (hud == null) continue;
            if (!hud.isEnabled() || editingHuds.containsKey(hud)) continue;
            lines.add(hud.position.getY());
            lines.add(hud.position.getCenterY());
            lines.add(hud.position.getBottomY());
        }
        return lines;
    }

    public static String removeHiddenCharacters(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Minecraft.getMinecraft().fontRendererObj.getStringWidth(String.valueOf(c)) == 0) continue;
            sb.append(c);
        }
        return sb.toString();
    }

    protected int[] getScoreboardPosition() {
        ScoreObjective objective = ScoreboardWrapper.getSidebar();
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());

        if (HysentialsConfig.scoreboard) {
            GuiIngameHysentials.renderObjective = false;
            int x = res.getScaledWidth();
            int radius = new Integer[]{0, 2, 4}[HysentialsConfig.scoreboardBorderRadius];
            List<ScoreboardWrapper.ScoreWrapper> lines = new ArrayList<>(ScoreboardWrapper.getLines(true));
            List<String[]> formattedLines = new ArrayList<>();

            for (ScoreboardWrapper.ScoreWrapper line : lines) {
                String[] formattedLine = new String[]{line.toString(), "§c" + line.getPoints()};
                formattedLines.add(formattedLine);
            }
            if (formattedLines.isEmpty()) return null;

            Collections.reverse(formattedLines);
            Collections.reverse(lines);
            String title = ScoreboardWrapper.getTitle();
            int titleWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(title);
            int width = Math.max(titleWidth, formattedLines.stream()
                .mapToInt(line ->
                    Minecraft.getMinecraft().fontRendererObj.getStringWidth(line[0])
                        + (HysentialsConfig.redNumbers ? 9 + Minecraft.getMinecraft().fontRendererObj.getStringWidth(line[1]) : 0)
                ).max().orElse(0)) + 4 + 2 * radius;
            int height = 11 + 2 * radius + 10 * lines.size();
            int originalX = x;
            x = x - width + radius;
            int y = res.getScaledHeight() / 2 - height / 2;
            return new int[]{x, y + 5 + radius * 2, x + width, y + height, lines.size()};
        }

        Scoreboard scoreboard = objective.getScoreboard();
        Collection<Score> collection = scoreboard.getSortedScores(objective);
        List<Score> list = collection.stream().filter(score -> score.getPlayerName() != null && !score.getPlayerName().startsWith("#")).collect(Collectors.toList());
        if (list.size() > 15) {
            collection = Lists.newArrayList(Iterables.skip(list, collection.size() - 15));
        } else {
            collection = list;
        }

        int i = Minecraft.getMinecraft().fontRendererObj.getStringWidth(objective.getDisplayName());

        String string;
        for (Iterator<Score> iterator = collection.iterator(); iterator.hasNext(); i = Math.max(i, Minecraft.getMinecraft().fontRendererObj.getStringWidth(string))) {
            Score score = iterator.next();
            ScorePlayerTeam scorePlayerTeam = scoreboard.getPlayersTeam(score.getPlayerName());
            string = ScorePlayerTeam.formatPlayerName(scorePlayerTeam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
        }

        int j = collection.size() * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;
        int k = res.getScaledHeight() / 2 + j / 3;
        int l = 3;

        int startX = res.getScaledWidth() - i - l;
        int startY = ((k - collection.size() * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT - 1);
        int endY = ((k - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT);
        int endX = res.getScaledWidth() - l + 2;

        return new int[]{startX, startY, endX, endY, collection.size()};
    }

    public boolean isMouseOverLine(int mouseX, int mouseY, int line) {
        int[] pos = getScoreboardPosition();
        int i = pos[1] + (HysentialsConfig.scoreboard ? 10 : 9) * line;
        return mouseX >= pos[0] && mouseX <= pos[2] && mouseY >= i && mouseY <= i + 9;
    }

    public static void drawRect(int x, int y, int width, int height, int colour) {
        Renderer.drawRect(colour, x, y, width, height);
    }
}
