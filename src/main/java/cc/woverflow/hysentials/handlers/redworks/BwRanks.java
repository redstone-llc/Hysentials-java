package cc.woverflow.hysentials.handlers.redworks;

import cc.polyfrost.oneconfig.gui.OneConfigGui;
import cc.polyfrost.oneconfig.gui.pages.ModConfigPage;
import cc.polyfrost.oneconfig.gui.pages.ModsPage;
import cc.polyfrost.oneconfig.gui.pages.Page;
import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.config.HysentialsMods;
import cc.woverflow.hysentials.handlers.sbb.SbbRenderer;
import cc.woverflow.hysentials.util.*;
import cc.woverflow.hysentials.utils.StringUtilsKt;
import cc.woverflow.hysentials.websocket.Socket;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static cc.woverflow.hysentials.handlers.guis.GameMenuOpen.field_currentPage;
import static cc.woverflow.hysentials.handlers.guis.GameMenuOpen.field_prevPage;

public class BwRanks {
    private int tick;
    public static HashMap<NetworkPlayerInfo, DuoVariable<String, String>> playerTeamMap = new HashMap<>();

    public static HashMap<String, DuoVariable<UUID, String>> replacementMap = new HashMap<>();
    public static HashMap<UUID, ScorePlayerTeam> customTeamMap = new HashMap<>();
    public static boolean hasRank = true;


    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        this.tick = 79;
        playerTeamMap.clear();
        replacementMap.clear();
        customTeamMap.clear();
    }

    int tick2 = 0;
    public static boolean shouldClose = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START || Minecraft.getMinecraft().thePlayer == null || !BUtils.isHypixelOrSBX()) {
            return;
        }
        if (Minecraft.getMinecraft().currentScreen instanceof OneConfigGui) {
            try {
                OneConfigGui gui = (OneConfigGui) Minecraft.getMinecraft().currentScreen;
                Page page = (Page) field_currentPage.get(gui);
                if (page instanceof ModConfigPage) {
                    ModConfigPage modPage = (ModConfigPage) page;
                    if (modPage.getPage().mod.name.equals("Hysentials") && !shouldClose) {
                        gui.openPage(new HysentialsMods());
                        shouldClose = true;
                    } else if (modPage.getPage().mod.name.equals("Hysentials") && shouldClose) {
                        gui.openPage(new ModsPage());
                        shouldClose = false;
                    }
                }
            } catch (Exception e) {
            }

        }
        if (++this.tick == 80) {
            // Update online cache every 4 seconds
            Multithreading.runAsync(BlockWAPIUtils::getOnline);
            if (++tick2 == 30) { // Basically every 2 minutes
                replacementMap.clear();
                tick2 = 0;
            }
            this.tick = 0;
        }
    }

    //generate random string
    private static final Random rand = new Random();

    public static String randomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder result = new StringBuilder();
        while (length > 0) {
            result.append(characters.charAt(rand.nextInt(characters.length())));
            length--;
        }
        return result.toString();
    }
}
