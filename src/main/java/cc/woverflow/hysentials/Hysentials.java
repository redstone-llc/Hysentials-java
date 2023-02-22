/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package cc.woverflow.hysentials;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import cc.woverflow.hysentials.command.GlobalChatCommand;
import cc.woverflow.hysentials.command.GroupChatCommand;
import cc.woverflow.hysentials.command.HypixelChatCommand;
import cc.woverflow.hysentials.command.HysentialsCommand;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.event.events.HysentialsLoadedEvent;
import cc.woverflow.hysentials.handlers.cache.HeightHandler;
import cc.woverflow.hysentials.handlers.chat.ChatHandler;
import cc.woverflow.hysentials.handlers.groupchats.GroupChat;
import cc.woverflow.hysentials.handlers.imageicons.ImageIcon;
import cc.woverflow.hysentials.handlers.language.LanguageHandler;
import cc.woverflow.hysentials.handlers.lobby.LobbyChecker;
import cc.woverflow.hysentials.handlers.redworks.BwRanks;
import cc.woverflow.hysentials.handlers.redworks.NeighborInstall;
import cc.woverflow.hysentials.pets.cubit.CubitCompanion;
import cc.woverflow.hysentials.pets.hamster.HamsterCompanion;
import cc.woverflow.hysentials.util.HypixelAPIUtils;
import cc.woverflow.hysentials.util.HypixelRanks;
import cc.woverflow.hysentials.util.blockw.OnlineCache;
import cc.woverflow.hysentials.util.friends.FriendCache;
import cc.woverflow.hysentials.util.skyblock.SkyblockChecker;
import cc.woverflow.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(
    modid = Hysentials.MOD_ID,
    name = Hysentials.MOD_NAME,
    version = Hysentials.VERSION
)
public class Hysentials {
    public static final String MOD_ID = "@ID@";
    public static final String MOD_NAME = "@NAME@";
    public static final String VERSION = "@VER@";

    @Mod.Instance(MOD_ID)
    public static Hysentials INSTANCE;

    public File modDir = new File("OVERFLOW", MOD_NAME);

    private HysentialsConfig config;
    private final Logger logger = LogManager.getLogger("Hytils Reborn");

    private final LanguageHandler languageHandler = new LanguageHandler();
    private final SkyblockChecker skyblockChecker = new SkyblockChecker();
    private final FriendCache friendCache = new FriendCache();
    private final OnlineCache onlineCache = new OnlineCache();

    private final LobbyChecker lobbyChecker = new LobbyChecker();
    private final ChatHandler chatHandler = new ChatHandler();

    public boolean isPatcher;
    public boolean isChatting;
    private boolean loadedCall;

    public String rank;

    public HamsterCompanion hamsterCompanion;

    public CubitCompanion cubitCompanion;

    @Mod.EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event) {
        if (!modDir.exists() && !modDir.mkdirs()) {
            throw new RuntimeException("Failed to create mod directory! Please report this to Ender#9967");
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        config = new HysentialsConfig();

        CommandManager.INSTANCE.registerCommand(new HysentialsCommand());
        CommandManager.INSTANCE.registerCommand(new GroupChatCommand());
        ClientCommandHandler.instance.registerCommand(new GlobalChatCommand());
        ClientCommandHandler.instance.registerCommand(new HypixelChatCommand());

        HeightHandler.INSTANCE.initialize();

        registerHandlers();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        isPatcher = Loader.isModLoaded("patcher");
        isChatting = Loader.isModLoaded("chatting");
        chatHandler.init();

        rank = HypixelAPIUtils.getRank(Minecraft.getMinecraft().getSession().getUsername());

        Socket.createSocket();
        registerImages();

        MinecraftForge.EVENT_BUS.post(new HysentialsLoadedEvent());
    }

    @Mod.EventHandler
    public void finishedStarting(FMLLoadCompleteEvent event) {
        this.loadedCall = true;
    }


    private void registerImages() {
        new ImageIcon("front", new ResourceLocation("textures/icons/front.png"));
        new ImageIcon("globalchat", new ResourceLocation("textures/icons/globalchat.png"));

        for (HypixelRanks rank : HypixelRanks.values()) {
            try {
                new ImageIcon(rank.getIconName(), new ResourceLocation("textures/icons/" + rank.getIconName() + ".png"));
            } catch (Exception ignored) {
            }
        }
    }
    private void registerHandlers() {
        final EventBus eventBus = MinecraftForge.EVENT_BUS;

        // general stuff
        eventBus.register(languageHandler);
        if (isChatting) {
            eventBus.register(new GroupChat());
        }
        // chat
        eventBus.register(chatHandler);
        // lobby
        eventBus.register(lobbyChecker);
        eventBus.register(new BwRanks());

        // height overlay
        EventManager.INSTANCE.register(HeightHandler.INSTANCE);

        cc.woverflow.hysentials.event.EventBus.INSTANCE.register(hamsterCompanion = new HamsterCompanion());
        cc.woverflow.hysentials.event.EventBus.INSTANCE.register(cubitCompanion = new CubitCompanion());

        eventBus.register(new HypixelAPIUtils());
        eventBus.register(new NeighborInstall());
    }

    public void sendMessage(String message) {
        UChat.chat(ChatColor.GOLD + "[" + MOD_NAME + "] " + ChatColor.Companion.translateAlternateColorCodes('&', message));
    }

    public HysentialsConfig getConfig() {
        return config;
    }

    public LobbyChecker getLobbyChecker() {
        return lobbyChecker;
    }

    public boolean isLoadedCall() {
        return loadedCall;
    }

    public void setLoadedCall(boolean loadedCall) {
        this.loadedCall = loadedCall;
    }

    public ChatHandler getChatHandler() {
        return chatHandler;
    }

    public LanguageHandler getLanguageHandler() {
        return languageHandler;
    }

    public Logger getLogger() {
        return logger;
    }

    public SkyblockChecker getSkyblockChecker() {
        return skyblockChecker;
    }

    public FriendCache getFriendCache() {
        return friendCache;
    }

    public OnlineCache getOnlineCache() {
        return onlineCache;
    }
}
