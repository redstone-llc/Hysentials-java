package cc.woverflow.hysentials.util;

import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreboardWrapper {
    private static boolean needsUpdate = true;
    private static List<ScoreWrapper> scoreboardNames = new ArrayList<>();
    private static String scoreboardTitle = "";

    public static Scoreboard getScoreboard() {
        return Minecraft.getMinecraft().theWorld.getScoreboard();
    }

    public static ScoreObjective getSidebar() {
        return getScoreboard().getObjectiveInDisplaySlot(1);
    }

    public static String getTitle() {
        if (needsUpdate) {
            updateNames();
            needsUpdate = false;
        }
        return scoreboardTitle;
    }

    public static List<ScoreWrapper> getLines(boolean descending) {
        if (needsUpdate) {
            updateNames();
            needsUpdate = false;
        }
        ArrayList<ScoreWrapper> scores = new ArrayList<>(scoreboardNames);
        Collections.reverse(scores);
        return descending ? scoreboardNames : scores;
    }

    private static void updateNames() {
        scoreboardNames.clear();
        scoreboardTitle = "";

        Scoreboard scoreboard = getScoreboard();
        ScoreObjective sidebarObjective = getSidebar();

        if (scoreboard == null || sidebarObjective == null)
            return;

        scoreboardTitle = sidebarObjective.getDisplayName();

        List<ScoreWrapper> scores = scoreboard.getSortedScores(sidebarObjective).stream().map(ScoreWrapper::new).collect(Collectors.toCollection(ArrayList::new));
        for (ScoreWrapper score : scores) {
            if (score.getName().startsWith("Rank: ") && HysentialsConfig.futuristicRanks) {
                BlockWAPIUtils.Rank rank = null;
                if (Hysentials.INSTANCE.getOnlineCache().getOnlinePlayers().containsKey(Minecraft.getMinecraft().thePlayer.getGameProfile().getId())) {
                    try {
                        rank = BlockWAPIUtils.Rank.valueOf(Hysentials.INSTANCE.getOnlineCache().getRankCache().get(Minecraft.getMinecraft().thePlayer.getGameProfile().getId()).toUpperCase());
                    } catch (Exception ignored) {
                        rank = BlockWAPIUtils.Rank.DEFAULT;
                    }
                }
                if (rank != null && !rank.equals(BlockWAPIUtils.Rank.DEFAULT)) {
                    Score score1 = new Score(scoreboard, sidebarObjective, "Rank: " + rank.getColor() + rank.name());
                    score1.setScorePoints(score.getPoints());
                    scores.set(scores.indexOf(score), new ScoreWrapper(score1));
                }
            }
        }

        scoreboardNames.addAll(scores);
    }

    public static void resetCache() {
        needsUpdate = true;
    }

    public static class ScoreWrapper {
        public Score score;
        public ScoreWrapper(Score score) {
            this.score = score;
        }

        public int getPoints() {
            return score.getScorePoints();
        }

        public String getName() {
            return ScorePlayerTeam.formatPlayerName(getScoreboard().getPlayersTeam(score.getPlayerName()), score.getPlayerName());
        }

        public String toString() {
            return getName();
        }
    }
}
