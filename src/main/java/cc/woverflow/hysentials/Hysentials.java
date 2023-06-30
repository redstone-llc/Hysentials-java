package cc.woverflow.hysentials;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.gui.UpdateChecker;
import cc.woverflow.hysentials.guis.club.ClubDashboardHandler;
import cc.woverflow.hysentials.handlers.chat.modules.misc.Limit256;
import cc.woverflow.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import cc.woverflow.hysentials.command.*;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.event.events.HysentialsLoadedEvent;
import cc.woverflow.hysentials.guis.ResolutionUtil;
import cc.woverflow.hysentials.guis.actionLibrary.ActionLibrary;
import cc.woverflow.hysentials.guis.club.ClubDashboard;
import cc.woverflow.hysentials.guis.gameMenu.RevampedGameMenu;
import cc.woverflow.hysentials.guis.misc.PlayerInvHandler;
import cc.woverflow.hysentials.guis.sbBoxes.SBBoxesEditor;
import cc.woverflow.hysentials.handlers.cache.HeightHandler;
import cc.woverflow.hysentials.handlers.chat.ChatHandler;
import cc.woverflow.hysentials.handlers.chat.modules.bwranks.BWSReplace;
import cc.woverflow.hysentials.handlers.display.GuiDisplayHandler;
import cc.woverflow.hysentials.handlers.guis.GameMenuOpen;
import cc.woverflow.hysentials.handlers.htsl.*;
import cc.woverflow.hysentials.handlers.imageicons.ImageIcon;
import cc.woverflow.hysentials.handlers.language.LanguageHandler;
import cc.woverflow.hysentials.handlers.lobby.HousingLagReducer;
import cc.woverflow.hysentials.handlers.lobby.LobbyChecker;
import cc.woverflow.hysentials.handlers.npc.QuestNPC;
import cc.woverflow.hysentials.handlers.redworks.BwRanks;
import cc.woverflow.hysentials.handlers.redworks.NeighborInstall;
import cc.woverflow.hysentials.handlers.sbb.Actionbar;
import cc.woverflow.hysentials.handlers.sbb.SbbRenderer;
import cc.woverflow.hysentials.htsl.Cluster;
import cc.woverflow.hysentials.pets.cubit.CubitCompanion;
import cc.woverflow.hysentials.pets.hamster.HamsterCompanion;
import cc.woverflow.hysentials.util.*;
import cc.woverflow.hysentials.util.blockw.OnlineCache;
import cc.woverflow.hysentials.websocket.Socket;
import cc.woverflow.hytils.util.friends.FriendCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
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
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
    public static File jarFile;

    public static String modDir = "./config/hysentials";

    private HysentialsConfig config;
    private final Logger logger = LogManager.getLogger("Hysentials");

    private final LanguageHandler languageHandler = new LanguageHandler();
    private final OnlineCache onlineCache = new OnlineCache();

    private final LobbyChecker lobbyChecker = new LobbyChecker();
    private final ChatHandler chatHandler = new ChatHandler();

    public final GuiDisplayHandler guiDisplayHandler = new GuiDisplayHandler();

    public ImageIconRenderer imageIconRenderer;
    public JsonData sbBoxes;
    public JsonData rankColors;
    public boolean isPatcher;
    public boolean isChatting;
    public boolean isHytils;
    private boolean loadedCall;

    public DiscordRPC discordRPC;

    public String rank;

    public HamsterCompanion hamsterCompanion;
    public CubitCompanion cubitCompanion;



    @Mod.EventHandler
    public void onFMLPreInitialization(FMLPreInitializationEvent event) {
        if (!new File(modDir).exists() && !new File(modDir).mkdirs()) {
            throw new RuntimeException("Failed to create mod directory! Please report this to Ender#9967");
        }
        jarFile = event.getSourceFile();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        config = new HysentialsConfig();
        File file = new File(modDir, "./config/hysentials");
        if (!file.exists() && !file.mkdirs()) {
            throw new RuntimeException("Failed to create config directory! Please report this to sinender on Discord");
        }
        sbBoxes = new JsonData("./config/hysentials/lines.json", new JSONObject().put("lines", new JSONArray()));
        rankColors = new JsonData("/assets/minecraft/textures/icons/colors.json", "./config/hysentials/color.jsonn", true);

        try {
            System.setProperty("file.encoding", "UTF-8");
            Field charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            SSLStore store = new SSLStore();
            store.load("/ssl/hysentials.der");
            SSLContext context = store.finish();
            SSLContext.setDefault(context);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Socket.createSocket();

        CommandManager.INSTANCE.registerCommand(new HysentialsCommand());
        CommandManager.INSTANCE.registerCommand(new GroupChatCommand());
        ClientCommandHandler.instance.registerCommand(new GlobalChatCommand());
        ClientCommandHandler.instance.registerCommand(new HypixelChatCommand());
        ClientCommandHandler.instance.registerCommand(new VisitCommand());
        ClientCommandHandler.instance.registerCommand(new RemoveGlowCommand());
        ClientCommandHandler.instance.registerCommand(new GlowCommand());
        ClientCommandHandler.instance.registerCommand(new SetLoreLineCommand());
        ClientCommandHandler.instance.registerCommand(new RenameCommand());
        ClientCommandHandler.instance.registerCommand(new RemoveNameCommand());
        ClientCommandHandler.instance.registerCommand(new InsertLoreLineCommand());
        ClientCommandHandler.instance.registerCommand(new RemoveLoreLineCommand());
        ClientCommandHandler.instance.registerCommand(new OpenInvCommand());
        CommandManager.INSTANCE.registerCommand(new SBBoxesCommand());
        CommandManager.INSTANCE.registerCommand(new ActionLibraryCommand());
        CommandManager.INSTANCE.registerCommand(new ClubCommand());

        if (Socket.cachedServerData.has("rpc") && Socket.cachedServerData.getBoolean("rpc")) {
            try {
                DiscordCore.init();
                discordRPC = new DiscordRPC();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        HeightHandler.INSTANCE.initialize();

        registerHandlers();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        isPatcher = Loader.isModLoaded("patcher");
        isChatting = Loader.isModLoaded("chatting");
        isHytils = Loader.isModLoaded("hytils-reborn");
        chatHandler.init();

        registerImages();
        cc.woverflow.hysentials.htsl.Loader.registerLoaders();
        Cluster.registerClusters();

        MinecraftForge.EVENT_BUS.post(new HysentialsLoadedEvent());
        HysentialsKt.Companion.postInit();
    }

    @Mod.EventHandler
    public void finishedStarting(FMLLoadCompleteEvent event) {
        this.loadedCall = true;
    }

    public static FontRenderer minecraftFont;
    private void registerImages() {
        new ImageIcon("front", new ResourceLocation("textures/icons/front.png"));
        new ImageIcon("back", new ResourceLocation("textures/icons/back.png"));
        new ImageIcon("globalchat", new ResourceLocation("textures/icons/globalchat.png"));
        new ImageIcon("creator", new ResourceLocation("textures/icons/creator.png"));
        new ImageIcon("guild", new ResourceLocation("textures/icons/guild.png"));
        new ImageIcon("party", new ResourceLocation("textures/icons/party.png"));
        new ImageIcon("to", new ResourceLocation("textures/icons/to.png"));
        new ImageIcon("from", new ResourceLocation("textures/icons/from.png"));
        new ImageIcon("team", new ResourceLocation("textures/icons/team.png"));
        new ImageIcon("friend", new ResourceLocation("textures/icons/friend.png"));

        for (HypixelRanks rank : HypixelRanks.values()) {
            try {
                new ImageIcon(rank.getIconName(), new ResourceLocation("textures/icons/" + rank.getIconName() + ".png"));
            } catch (Exception ignored) {
            }
        }

        for (int i = 0; i < 10; i++) {
            new ImageIcon(String.valueOf(i), new ResourceLocation("textures/icons/" + i + ".png"));
        }
        imageIconRenderer = new ImageIconRenderer();
        minecraftFont = Minecraft.getMinecraft().fontRendererObj;
        Minecraft.getMinecraft().fontRendererObj = imageIconRenderer;
    }

    private void registerHandlers() {
        final EventBus eventBus = MinecraftForge.EVENT_BUS;
        final cc.woverflow.hysentials.event.EventBus hyBus = cc.woverflow.hysentials.event.EventBus.INSTANCE;
        RevampedGameMenu.initGUI();
        SBBoxesEditor.initGUI();

        // general stuff
        eventBus.register(languageHandler);
//        if (isChatting) {
//            eventBus.register(new GroupChat());
//        }
        // chat
        eventBus.register(chatHandler);

        eventBus.register(new HousingLagReducer());
        // lobby
        eventBus.register(lobbyChecker);
        eventBus.register(guiDisplayHandler);
        eventBus.register(new ResolutionUtil());
        eventBus.register(new BwRanks());
        eventBus.register(new GameMenuOpen());
        eventBus.register(new SbbRenderer());
        eventBus.register(new Actionbar());
        eventBus.register(new ActionLibrary());
        eventBus.register(new Queue());
        eventBus.register(new Navigator());
        eventBus.register(new ActionGUIHandler());
        eventBus.register(new FunctionsGUIHandler());
        eventBus.register(new Exporter());
        eventBus.register(new HousingMenuHandler());
        eventBus.register(new ClubDashboardHandler());
        eventBus.register(new PlayerInvHandler());
        eventBus.register(new BWSReplace());
        eventBus.register(new QuestNPC());
        eventBus.register(new MUtils());
        eventBus.register(new Limit256());
        eventBus.register(cubitCompanion = new CubitCompanion());
        new Renderer();

        // height overlay
        EventManager.INSTANCE.register(HeightHandler.INSTANCE);
        EventManager.INSTANCE.register(new BwRanks());

        eventBus.register(new HypixelAPIUtils());
        eventBus.register(new NeighborInstall());

        HysentialsKt.Companion.init();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            sbBoxes.save();
        }));
    }

    public void sendMessage(String message) {
        UChat.chat(HysentialsConfig.chatPrefix + " " + ChatColor.Companion.translateAlternateColorCodes('&', message));
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

    public OnlineCache getOnlineCache() {
        return onlineCache;
    }


    public static InputStream post(String url, JSONObject json) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.addRequestProperty("User-Agent", "Hysentials/" + VERSION);
        connection.setReadTimeout(15000);
        connection.setConnectTimeout(15000);

        OutputStream outStream = connection.getOutputStream();
        OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, StandardCharsets.UTF_8);
        outStreamWriter.write(json.toString());
        outStreamWriter.flush();
        outStreamWriter.close();
        outStream.close();

        return connection.getInputStream();
    }
}
