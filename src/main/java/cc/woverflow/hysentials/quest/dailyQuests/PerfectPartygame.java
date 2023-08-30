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

public class PerfectPartygame extends Quest {
    public PerfectPartygame() {
        super("Perfect Party Game", "Win a game of party games with at least 18 stars", "PERFECT_PARTYGAME", true);
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
        if (LocrawUtil.INSTANCE.getLocrawInfo() == null || !LocrawUtil.INSTANCE.getLocrawInfo().getGameMode().equals("PARTY") || LocrawUtil.INSTANCE.getLocrawInfo().getGameMode().equals("lobby")) {
            return;
        }
        Matcher matcher = Pattern.compile("( .+ )§e§l1st Place §7- (.+ |§7)(.+) - (.+)§8 x ").matcher(message.replace(C.RESET, ""));
        if (matcher.find()) {
            String username = C.removeColor(matcher.group(3));
            String stars = matcher.group(4);
            if (username.equals(Minecraft.getMinecraft().getSession().getUsername()) && Integer.parseInt(stars) >= 18) {
                UChat.chat("&a&lQuest Completed! &7You have completed the &e" + getName() + " &7quest!");
                UChat.chat("&7We will validate your completion in 60 seconds once the api has updated...");
                Multithreading.schedule(() -> {
                    QuestHandler.checkQuest(this);
                }, 60, TimeUnit.SECONDS);
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
