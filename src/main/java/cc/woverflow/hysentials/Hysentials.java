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
import cc.woverflow.hysentials.command.HysentialsCommand;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.handlers.bwranks.BwRanks;
import cc.woverflow.hysentials.handlers.cache.CosmeticsHandler;
import cc.woverflow.hysentials.handlers.cache.HeightHandler;
import cc.woverflow.hysentials.handlers.chat.ChatHandler;
import cc.woverflow.hysentials.handlers.groupchats.GroupChat;
import cc.woverflow.hysentials.handlers.lobby.LobbyChecker;
import cc.woverflow.hysentials.user.Player;
import cc.woverflow.hysentials.util.HypixelAPIUtils;
import cc.woverflow.hysentials.util.SplashProgress;
import cc.woverflow.hysentials.util.blockw.OnlineCache;
import cc.woverflow.hysentials.util.friends.FriendCache;
import cc.woverflow.hysentials.util.skyblock.SkyblockChecker;
import cc.woverflow.hysentials.websocket.Socket;
import cc.woverflow.hysentials.handlers.language.LanguageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
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
import org.lwjgl.Sys;

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

    public File modDir = new File(new File(Minecraft.getMinecraft().mcDataDir, "W-OVERFLOW"), MOD_NAME);

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

    @Mod.EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event) {
        if (!modDir.exists() && !modDir.mkdirs()) {
            throw new RuntimeException("Failed to create mod directory! Please report this to Ender#9967");
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        SplashProgress.setProgress(5, "Loading config");
        config = new HysentialsConfig();

        SplashProgress.setProgress(6, "Loading Commands");
        CommandManager.INSTANCE.registerCommand(new HysentialsCommand());
        CommandManager.INSTANCE.registerCommand(new GroupChatCommand());
        ClientCommandHandler.instance.registerCommand(new GlobalChatCommand());


        SplashProgress.setProgress(7, "Loading Events");
        CosmeticsHandler.INSTANCE.initialize();
        HeightHandler.INSTANCE.initialize();

        registerHandlers();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        SplashProgress.setProgress(8, "Finalizing");
        isPatcher = Loader.isModLoaded("patcher");
        isChatting = Loader.isModLoaded("chatting");
        System.out.println(isChatting);

        rank = HypixelAPIUtils.getRank(Minecraft.getMinecraft().getSession().getUsername());

        Socket.createSocket();
        SplashProgress.setProgress(9, "Complete");
    }

    @Mod.EventHandler
    public void finishedStarting(FMLLoadCompleteEvent event) {
        this.loadedCall = true;
        Player.CLIENT = new Player(Minecraft.getMinecraft().thePlayer.getName(), Minecraft.getMinecraft().thePlayer.getUniqueID().toString());
    }

    private void registerHandlers() {
        final EventBus eventBus = MinecraftForge.EVENT_BUS;

        // general stuff
        eventBus.register(languageHandler);
        eventBus.register(new GroupChat());
        // chat
        eventBus.register(chatHandler);
        // lobby
        eventBus.register(lobbyChecker);
        eventBus.register(new BwRanks());

        // height overlay
        EventManager.INSTANCE.register(HeightHandler.INSTANCE);

        eventBus.register(new HypixelAPIUtils());
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
