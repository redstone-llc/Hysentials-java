package cc.woverflow.hysentials.guis.sbBoxes;

import cc.polyfrost.oneconfig.gui.OneConfigGui;
import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.EditorConfig;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.guis.HysentialsGui;
import cc.woverflow.hysentials.util.Renderer;
import cc.woverflow.hysentials.util.ScoreboardWrapper;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.GuiIngameForge;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SBBoxesEditor extends HysentialsGui {
    static boolean scroll = true;
    int scrollAmount = 0;
    static Renderer.IconButton collapseIcon;
    static Renderer.IconButton expandIcon;
    static JSONObject data;
    public static OneConfigGui configGui;
    public static boolean isConfigOpen = false;
    private static RegexCreator regexCreator;

    public SBBoxesEditor() {
        data = Hysentials.INSTANCE.sbBoxes.jsonObject;

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

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution res = new ScaledResolution(mc);
        int radius = new Integer[]{0, 2, 4}[HysentialsConfig.scoreboardBoxesBorderRadius];
        drawRect(0, 0, res.getScaledWidth(), res.getScaledHeight(), (int) Renderer.color(0, 0, 0, 75));

        if (scroll) {
            drawRect(0, 0, 200, res.getScaledHeight(), (int) Renderer.color(0, 0, 0, 125));
            fontRendererObj.drawString("§eScoreboard Boxes", 100 - fontRendererObj.getStringWidth("§eScoreboard Boxes") / 2, 5 + this.scrollAmount, (int) Renderer.color(255, 255, 255, 255));
            collapseIcon.draw(200, res.getScaledHeight() / 2 - 16);
            JSONArray lines = data.getJSONArray("lines");
            for (int i = 0; i < lines.length(); i++) {
                JSONObject line = (JSONObject) lines.get(i);
                drawRect(0, 15 + 30 * i + this.scrollAmount, 200, 30, (int) Renderer.color(0, 0, 0, 150));
                if (mouseX > 0 && mouseX < 200 && mouseY > 15 + 30 * i + 3 * i + this.scrollAmount && mouseY < 15 + 30 * i + 3 * i + 30 + this.scrollAmount) {
                    drawRect(0, 15 + 30 * i + 3 * i + this.scrollAmount, 200, 30, (int) Renderer.color(0, 0, 0, 175));
                    fontRendererObj.drawString("§eClick to edit", mouseX + 5, mouseY - 10, (int) Renderer.color(255, 255, 255, 255));
                }
                fontRendererObj.drawString("§eText: §r" + line.getString("text"), 100 - fontRendererObj.getStringWidth("§eText: §r" + line.getString("text")) / 2, 20 + 30 * i + 3 * i + this.scrollAmount, (int) Renderer.color(255, 255, 255, 255));
                fontRendererObj.drawString("§ePosition: §r(" + line.getInt("x") + ", " + line.getInt("y") + ")", 100 - fontRendererObj.getStringWidth("§ePosition: §r(" + line.getInt("x") + ", " + line.getInt("y") + ")") / 2, 30 + 30 * i + 3 * i + this.scrollAmount, (int) Renderer.color(255, 255, 255, 255));
            }
        } else {
            expandIcon.draw(0, res.getScaledHeight() / 2 - 16);
        }

        int[] pos = getScoreboardPosition();
        int startX = pos[0];
        int startY = pos[1];
        int endX = pos[2];
        ArrayList<ScoreboardWrapper.ScoreWrapper> lines = ScoreboardWrapper.getScoreboard().getSortedScores(ScoreboardWrapper.getSidebar()).stream().map(ScoreboardWrapper.ScoreWrapper::new).collect(Collectors.toCollection(ArrayList::new));


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
    }

    static boolean dragging = false;
    static JSONObject draggingLine = null;

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (isClosed) return;
        if (!dragging && draggingLine == null) return;
        if (state != 0) return;
        dragging = false;
        draggingLine = null;
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if (isClosed) return;
        if (!dragging && draggingLine == null) return;
        moveLine(draggingLine, mouseX, mouseY);
    }

    public static void moveLine(JSONObject line, double mouseX, double mouseY) {
        double scale = (double) line.getDouble("scale");
        double width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(line.getString("text")) * scale + 10 * scale;
        double height = 15 * scale;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            line.put("x", mouseX - (int) width / 2);
        } else if (Keyboard.isKeyDown(Keyboard.KEY_RCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            line.put("y", mouseY - (int) height / 2);
        } else {
            line.put("x", mouseX - (int) width / 2);
            line.put("y", mouseY - (int) height / 2);
        }
        snap(line, (int) width, (int) height);

    }

    public static void snap(JSONObject line, int width, int height) {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        int sw = res.getScaledWidth();
        int sh = res.getScaledHeight();
        int lineX = line.getInt("x");
        int lineY = line.getInt("y");

        if (lineX < 0) line.put("x", 0);
        if (lineY < 0) line.put("y", 0);
        if (lineX + width > sw) line.put("x", sw - width);
        if (lineY + height > sh) line.put("y", sh - height);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 1) {
            JSONArray lines = data.getJSONArray("lines");
            for (Object l : lines) {
                JSONObject line = (JSONObject) l;
                double width = fontRendererObj.getStringWidth(line.getString("text")) * line.getDouble("scale") + 10 * line.getDouble("scale");
                double height = 15 * line.getDouble("scale");
                if (mouseX > line.getInt("x") && mouseX < line.getInt("x") + width && mouseY > line.getInt("y") && mouseY < line.getInt("y") + height) {
                    configGui = new EditorConfig(line).openGuI();
                    Multithreading.schedule(() -> {
                        isConfigOpen = true;
                    }, 1000, TimeUnit.MILLISECONDS);
                }
            }
            return;
        }

        if (mouseButton == 0) {
            { // Dragging
                JSONArray lines = data.getJSONArray("lines");
                ArrayList<ScoreboardWrapper.ScoreWrapper> ls = ScoreboardWrapper.getScoreboard().getSortedScores(ScoreboardWrapper.getSidebar()).stream().map(ScoreboardWrapper.ScoreWrapper::new).collect(Collectors.toCollection(ArrayList::new));

                for (Object l : lines) {
                    JSONObject line = (JSONObject) l;
                    if (ls.stream().noneMatch(s -> removeHiddenCharacters(s.toString()).matches(line.getString("regex"))))
                        continue;
                    double scale = (double) line.getDouble("scale");
                    double width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(line.getString("text")) * scale + 10 * scale;
                    double height = 15 * scale;
                    int lineX = line.getInt("x");
                    int lineY = line.getInt("y");
                    if (mouseX > lineX && mouseX < lineX + width && mouseY > lineY && mouseY < lineY + height && line.getBoolean("enabled")) {
                        dragging = true;
                        draggingLine = line;
                    }
                }
            }

            if (scroll & mouseX < 200) {
                for (int i = 0; i < data.getJSONArray("lines").length(); i++) {
                    JSONObject line = (JSONObject) data.getJSONArray("lines").get(i);
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
            ArrayList<ScoreboardWrapper.ScoreWrapper> lines = ScoreboardWrapper.getScoreboard().getSortedScores(ScoreboardWrapper.getSidebar()).stream().map(ScoreboardWrapper.ScoreWrapper::new).collect(Collectors.toCollection(ArrayList::new));

            for (int i = 0; i < lines.size(); i++) {
                if (isMouseOverLine(mouseX, mouseY, i)) {
                    ScoreboardWrapper.ScoreWrapper line = lines.get(lines.size() - i - 1);
                    String s = removeHiddenCharacters(line.toString());
                    JSONArray ls = data.getJSONArray("lines");
                    Optional<Object> thing = ls.toList().stream().filter(o -> s.matches((String) ((HashMap) o).get("regex"))).findFirst();
                    if (thing.isPresent()) {
                        HashMap l = (HashMap) thing.get();
                        int index = ls.toList().indexOf(l);
                        JSONObject object = (JSONObject) ls.get(index);
                        object.put("enabled", !object.getBoolean("enabled"));
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
                        ls.put(l);
                    }
                    Hysentials.INSTANCE.sbBoxes.save();
                }
            }
        }
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
            GuiIngameForge.renderObjective = false;
            int x = res.getScaledWidth();
            int radius = new Integer[]{0, 2, 4}[HysentialsConfig.scoreboardBorderRadius];
            List<ScoreboardWrapper.ScoreWrapper> lines = ScoreboardWrapper.getLines(true);
            List<String[]> formattedLines = new ArrayList<>();

            for (ScoreboardWrapper.ScoreWrapper line : lines) {
                String[] formattedLine = new String[]{line.toString(), "§c" + line.getPoints()};
                formattedLines.add(formattedLine);
            }

            Collections.reverse(formattedLines);
            Collections.reverse(lines);
            String title = ScoreboardWrapper.getTitle();
            int titleWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(title);
            int width = Math.max(titleWidth, formattedLines.stream()
                .mapToInt(line ->
                    Minecraft.getMinecraft().fontRendererObj.getStringWidth(line[0])
                        + (HysentialsConfig.redNumbers ? 9 + Minecraft.getMinecraft().fontRendererObj.getStringWidth(line[1]) : 0)
                ).max().getAsInt()) + 4 + 2 * radius;
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
