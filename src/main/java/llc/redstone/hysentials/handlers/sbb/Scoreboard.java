package llc.redstone.hysentials.handlers.sbb;

import llc.redstone.hysentials.config.hysentialmods.HousingConfig;
import llc.redstone.hysentials.config.hysentialmods.ScorebarsConfig;
import llc.redstone.hysentials.util.C;
import llc.redstone.hysentials.util.Renderer;
import llc.redstone.hysentials.util.ScoreboardWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.GuiIngameForge;

import java.util.ArrayList;
import java.util.List;

import static llc.redstone.hysentials.handlers.sbb.SbbRenderer.housingScoreboard;

public class Scoreboard {
    public static void scoreboard() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        GuiIngameForge.renderObjective = false;
        int x = res.getScaledWidth();
        int radius = new Integer[]{0, 2, 4}[ScorebarsConfig.scoreboardBorderRadius];
        List<ScoreboardWrapper.ScoreWrapper> lines = ScoreboardWrapper.getLines(false);
        if (lines == null) return;
        List<String[]> formattedLines = new ArrayList<>();

        for (ScoreboardWrapper.ScoreWrapper line : lines) {
            String[] formattedLine = new String[]{line.toString(), "§c" + line.getPoints()};
            formattedLines.add(formattedLine);
        }

//        Collections.reverse(formattedLines);
//        Collections.reverse(lines);
        if (lines.size() == 0) return;
        String title = ScoreboardWrapper.getTitle();
        String housingName = housingScoreboard.getHousingName();
        if (housingName != null && HousingConfig.housingNameScoreboard) {
            if (HousingConfig.housingNameColors) {
                title = "§e" + housingScoreboard.removeFormatting(housingName).toUpperCase();
            } else {
                title = "§e§l" + C.removeColor(housingScoreboard.removeFormatting(housingName).toUpperCase());
            }
        }
        int titleWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(title);
        int width = Math.max(titleWidth, formattedLines.stream()
            .mapToInt(line ->
                Minecraft.getMinecraft().fontRendererObj.getStringWidth(line[0])
                    + (ScorebarsConfig.redNumbers ? 9 + Minecraft.getMinecraft().fontRendererObj.getStringWidth(line[1]) : 0)
            ).max().getAsInt()) + 4 + 2 * radius;
        int height = 11 + 2 * radius + 10 * lines.size();
        int originalX = x;
        x = x - width + radius;
        int y = res.getScaledHeight() / 2 - height / 2;

        SbbRenderer.drawBox(
            x,
            y,
            width,
            height,
            ScorebarsConfig.boxColor,
            ScorebarsConfig.boxShadows,
            radius
        );
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(title, (x + width / 2 - titleWidth / 2), y + radius + 1, (int) Renderer.color(255, 255, 255, 255));
        for (int i = 0; i < formattedLines.size(); i++) {
            String[] line = formattedLines.get(i);
            Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(line[0], x + 2 + radius / 2, y + 11 + radius + i*10, (int) Renderer.color(255, 255, 255, 255));
            if (ScorebarsConfig.redNumbers) {
                int charWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(line[1]);
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(line[1], x + width - radius - charWidth - 1, y + radius + 11 + i*10, (int) Renderer.color(255, 255, 255, 255));
            }
        }
    }
}
