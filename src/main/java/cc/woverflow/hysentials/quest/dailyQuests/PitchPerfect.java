package cc.woverflow.hysentials.quest.dailyQuests;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.handlers.misc.QuestHandler;
import cc.woverflow.hysentials.quest.Quest;
import cc.woverflow.hysentials.util.C;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PitchPerfect extends Quest {
    public PitchPerfect() {
        super("Pitch Perfect", "Win a game of wool wars with exactly {kills} kills, {blockb} blocks broken and {blockp} wool placed.", "PITCH_PERFECT", true);
    }

    @Override
    public void onTick() {
        if (LocrawUtil.INSTANCE.getLocrawInfo() == null || !LocrawUtil.INSTANCE.getLocrawInfo().getGameType().equals(LocrawInfo.GameType.SKYWARS) || LocrawUtil.INSTANCE.getLocrawInfo().getGameMode().equals("lobby")) {
            return;
        }
        if (!isActive) return;
    }

    @Override
    public void onMessageReceive(String message) {
        super.onMessageReceive(message);
        if (LocrawUtil.INSTANCE.getLocrawInfo() == null || !LocrawUtil.INSTANCE.getLocrawInfo().getGameType().equals(LocrawInfo.GameType.DUELS) || LocrawUtil.INSTANCE.getLocrawInfo().getGameMode().equals("lobby")) {
            return;
        }
        if (!isActive) return;

        Matcher matcher = Pattern.compile("( .+ )§aYour team won!").matcher(message.replace(C.RESET, ""));
        if (matcher.find()) {
            Multithreading.schedule(() -> {
                UChat.chat("&a&lQuest Progress: &7" + progress + "&a/1");
                UChat.chat("&7We will validate your progress in 60 seconds once the api has updated...");
                Multithreading.schedule(() -> {
                    UChat.chat("&7Validating progress for Pitch Perfect...");
                    QuestHandler.checkQuest(this);
                }, 60, TimeUnit.SECONDS);
            }, 2, TimeUnit.SECONDS);
        }
    }

    @Override
    public String getDescription() {
        if (!data.has("kills")) return super.getDescription().replace("{kills}", "x").replace("{blockb}", "x").replace("{blockp}", "x");
        return super.getDescription()
            .replace("{kills}", String.valueOf(data.getInt("kills")))
            .replace("{blockb}", String.valueOf(data.getInt("blocksBroken")))
            .replace("{blockp}", String.valueOf(data.getInt("blocksPlaced")));
    }

    @Override
    public void onQuestStart() {

    }

    @Override
    public void onQuestEnd() {

    }
}
