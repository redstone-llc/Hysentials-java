package cc.woverflow.hysentials.quest.dailyQuests;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.handlers.misc.QuestHandler;
import cc.woverflow.hysentials.quest.Quest;
import cc.woverflow.hysentials.util.C;
import net.minecraft.client.Minecraft;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlitzDuelsRandom extends Quest {
    public BlitzDuelsRandom() {
        super("Random Kit Blitz", "Win &a5 &7games of blitz duels whilst using a random kit.", "RANDOM_BLITZ", 5, true);
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
        if(!isActive) return;

        Matcher matcher = Pattern.compile("( .+ )(.+ |§7)(.+)§f§e §e§lWINNER!").matcher(message.replace(C.RESET, ""));
        if (matcher.find()) {
            String winner = matcher.group(3);
            if (winner.equals(Minecraft.getMinecraft().thePlayer.getName())) {
                Multithreading.schedule(() -> {
                    progress++;
                    UChat.chat("&a&lQuest Progress: &7" + progress + "&a/5");
                    UChat.chat("&7We will validate your progress in 60 seconds once the api has updated...");
                    Multithreading.schedule(() -> {
                        UChat.chat("&7Validating progress for Random Blitz Duels...");
                        QuestHandler.checkQuest(this);
                    }, 60, TimeUnit.SECONDS);
                }, 2, TimeUnit.SECONDS);

            }
        }
    }

    @Override
    public void onQuestStart() {

    }

    @Override
    public void onQuestEnd() {

    }
}
