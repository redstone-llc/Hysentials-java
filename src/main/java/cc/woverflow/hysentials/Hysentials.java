package cc.woverflow.hysentials;

import cc.polyfrost.oneconfig.events.EventManager;
import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.woverflow.hysentials.capes.CapeHandler;
import cc.woverflow.hysentials.config.hysentialMods.FormattingConfig;
import cc.woverflow.hysentials.cosmetic.CosmeticManager;
import cc.woverflow.hysentials.cosmetics.backpack.BackpackCosmetic;
import cc.woverflow.hysentials.cosmetics.hamster.HamsterCompanion;
import cc.woverflow.hysentials.cosmetics.hats.blackcat.BlackCat;
import cc.woverflow.hysentials.cosmetics.hats.blackcat.BlackCatModel;
import cc.woverflow.hysentials.cosmetics.hats.cat.CatHat;
import cc.woverflow.hysentials.cosmetics.kzero.KzeroBundle;
import cc.woverflow.hysentials.cosmetics.miya.MiyaCompanion;
import cc.woverflow.hysentials.cosmetics.pepper.PepperCompanion;
import cc.woverflow.hysentials.cosmetics.hats.technocrown.TechnoCrown;
import cc.woverflow.hysentials.guis.club.ClubDashboardHandler;
import cc.woverflow.hysentials.guis.container.ContainerHandler;
import cc.woverflow.hysentials.guis.hsplayerlist.GuiOnlineList;
import cc.woverflow.hysentials.guis.utils.SBBoxes;
import cc.woverflow.hysentials.handlers.chat.modules.misc.Limit256;
import cc.woverflow.hysentials.handlers.misc.QuestHandler;
import cc.woverflow.hysentials.handlers.redworks.FormatPlayerName;
import cc.woverflow.hysentials.macrowheel.MacroWheelData;
import cc.woverflow.hysentials.quest.Quest;
import cc.woverflow.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import cc.woverflow.hysentials.command.*;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.event.events.HysentialsLoadedEvent;
import cc.woverflow.hysentials.guis.ResolutionUtil;
import cc.woverflow.hysentials.guis.actionLibrary.ActionLibrary;
import cc.woverflow.hysentials.guis.gameMenu.RevampedGameMenu;
import cc.woverflow.hysentials.guis.misc.PlayerInvHandler;
import cc.woverflow.hysentials.guis.sbBoxes.SBBoxesEditor;
import cc.woverflow.hysentials.handlers.chat.ChatHandler;
import cc.woverflow.hysentials.handlers.chat.modules.bwranks.BWSReplace;
import cc.woverflow.hysentials.handlers.display.GuiDisplayHandler;
import cc.woverflow.hysentials.handlers.guis.GameMenuOpen;
import cc.woverflow.hysentials.handlers.htsl.*;
import cc.woverflow.hysentials.handlers.imageicons.ImageIcon;
import cc.woverflow.hysentials.handlers.language.LanguageHandler;
import cc.woverflow.hysentials.handlers.lobby.HousingLagReducer;
import cc.woverflow.hysentials.handlers.redworks.BwRanks;
import cc.woverflow.hysentials.handlers.sbb.Actionbar;
import cc.woverflow.hysentials.handlers.sbb.SbbRenderer;
import cc.woverflow.hysentials.htsl.Cluster;
import cc.woverflow.hysentials.cosmetics.cubit.CubitCompanion;
import cc.woverflow.hysentials.util.*;
import cc.woverflow.hysentials.util.blockw.OnlineCache;
import cc.woverflow.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
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
    public static GuiMainMenu guiMainMenu;
    public static File jarFile;

    public static String modDir = "./config/hysentials";

    private HysentialsConfig config;
    private final Logger logger = LogManager.getLogger("Hysentials");

    private final LanguageHandler languageHandler = new LanguageHandler();
    private final OnlineCache onlineCache = new OnlineCache();

    private final ChatHandler chatHandler = new ChatHandler();

    public final GuiDisplayHandler guiDisplayHandler = new GuiDisplayHandler();
    public final CosmeticManager cosmeticManager = new CosmeticManager();

    public ImageIconRenderer imageIconRenderer;
    public JsonData sbBoxes;
    public JsonData rankColors;
    public MacroWheelData.MacroJson macroJson;
    public boolean isPatcher;
    public boolean isChatting;
    public boolean isHytils;
    private boolean loadedCall;

    public DiscordRPC discordRPC;

    public String rank;

    public CubitCompanion cubitCompanion;
    public PepperCompanion pepperCompanion;
    public MiyaCompanion miyaCompanion;
    public HamsterCompanion hamsterCompanion;
    public TechnoCrown technoCrown;
    public BlackCat blackCat;
    public KzeroBundle kzeroBundle;



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
        sbBoxes = new SBBJsonData("./config/hysentials/lines.json", new JSONObject().put("lines", new JSONArray()));
        macroJson = new MacroWheelData.MacroJson();

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


        Socket.init();
        Socket.createSocket();

        CommandManager.INSTANCE.registerCommand(new GroupChatCommand());
        ClientCommandHandler.instance.registerCommand(new HysentialsCommand());
        ClientCommandHandler.instance.registerCommand(new GlobalChatCommand());
        ClientCommandHandler.instance.registerCommand(new HypixelChatCommand());
        ClientCommandHandler.instance.registerCommand(new VisitCommand());
        ClientCommandHandler.instance.registerCommand(new VisitPlayerCommand());
        ClientCommandHandler.instance.registerCommand(new RemoveGlowCommand());
        ClientCommandHandler.instance.registerCommand(new GlowCommand());
        ClientCommandHandler.instance.registerCommand(new SetLoreLineCommand());
        ClientCommandHandler.instance.registerCommand(new RenameCommand());
        ClientCommandHandler.instance.registerCommand(new RemoveNameCommand());
        ClientCommandHandler.instance.registerCommand(new InsertLoreLineCommand());
        ClientCommandHandler.instance.registerCommand(new RemoveLoreLineCommand());
        ClientCommandHandler.instance.registerCommand(new OpenInvCommand());
        ClientCommandHandler.instance.registerCommand(new SetTextureCommand());
        ClientCommandHandler.instance.registerCommand(new HymojiCommand());
        ClientCommandHandler.instance.registerCommand(new ClaimCommand());
        CommandManager.INSTANCE.registerCommand(new SBBoxesCommand());
        CommandManager.INSTANCE.registerCommand(new ActionLibraryCommand());
        CommandManager.INSTANCE.registerCommand(new ClubCommand());


        if (Socket.cachedServerData.has("rpc") && Socket.cachedServerData.getBoolean("rpc")) {
            //                DiscordCore.init();
            discordRPC = new DiscordRPC();
        }

        registerHandlers();
        Quest.registerQuests();
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
        HysentialsUtilsKt.postInit();

        LayerArmorBase armorBase;
        LayerBipedArmor bipedArmor;
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
        new ImageIcon("partyprefix", new ResourceLocation("textures/icons/party.png"));
        new ImageIcon("to", new ResourceLocation("textures/icons/to.png"));
        new ImageIcon("from", new ResourceLocation("textures/icons/from.png"));
        new ImageIcon("team", new ResourceLocation("textures/icons/team.png"));
        new ImageIcon("friend", new ResourceLocation("textures/icons/friend.png"));

        new ImageIcon("common", new ResourceLocation("textures/icons/common.png"));
        new ImageIcon("rare", new ResourceLocation("textures/icons/rare.png"));
        new ImageIcon("epic", new ResourceLocation("textures/icons/epic.png"));
        new ImageIcon("legendary", new ResourceLocation("textures/icons/legendary.png"));
        new ImageIcon("exclusive", new ResourceLocation("textures/icons/exclusive.png"));

        new ImageIcon("moan", new ResourceLocation("textures/emoji/1005491490027487333.png"), true);
        new ImageIcon("super_neutral", new ResourceLocation("textures/emoji/1013063272473317377.png"), true);
        new ImageIcon("neutral", new ResourceLocation("textures/emoji/1013063271143714866.png"), true);
        new ImageIcon("creeper", new ResourceLocation("textures/emoji/1013063267964432494.png"), true);
        new ImageIcon("speechless", new ResourceLocation("textures/emoji/1013063269688283186.png"), true);
        new ImageIcon("yawn", new ResourceLocation("textures/emoji/1013063264667697263.png"), true);
        new ImageIcon("weary", new ResourceLocation("textures/emoji/1005491488739827752.png"), true);
        new ImageIcon("plead", new ResourceLocation("textures/emoji/1005491491294167111.png"), true);
        new ImageIcon("hehe", new ResourceLocation("textures/emoji/1013063262868348958.png"), true);
        new ImageIcon("foggy", new ResourceLocation("textures/emoji/1013063247819186267.png"), true);
        new ImageIcon("mind_blown", new ResourceLocation("textures/emoji/1013063244480512050.png"), true);
        new ImageIcon("curse", new ResourceLocation("textures/emoji/1013063242970566696.png"), true);
        new ImageIcon("mad", new ResourceLocation("textures/emoji/1013063241280266292.png"), true);
        new ImageIcon("angry", new ResourceLocation("textures/emoji/1013063239967449128.png"), true);
        new ImageIcon("cool", new ResourceLocation("textures/emoji/1005491469244698666.png"), true);
        new ImageIcon("drool", new ResourceLocation("textures/emoji/1005491492699254886.png"), true);
        new ImageIcon("phew", new ResourceLocation("textures/emoji/1005491497254264912.png"), true);
        new ImageIcon("huff", new ResourceLocation("textures/emoji/1005491495689785464.png"), true);
        new ImageIcon("sob", new ResourceLocation("textures/emoji/1005491494066602188.png"), true);
        new ImageIcon("thinking", new ResourceLocation("textures/emoji/1013063261396156477.png"), true);
        new ImageIcon("hugs", new ResourceLocation("textures/emoji/1013063259789733989.png"), true);
        new ImageIcon("big_frown", new ResourceLocation("textures/emoji/1013063256908234782.png"), true);
        new ImageIcon("worried", new ResourceLocation("textures/emoji/1013063255406673970.png"), true);
        new ImageIcon("scared", new ResourceLocation("textures/emoji/1013063253338894347.png"), true);
        new ImageIcon("scream", new ResourceLocation("textures/emoji/1013063252101570570.png"), true);
        new ImageIcon("cold", new ResourceLocation("textures/emoji/1013063250734235708.png"), true);
        new ImageIcon("party", new ResourceLocation("textures/emoji/1005491471455096935.png"), true);
        new ImageIcon("star_eyes", new ResourceLocation("textures/emoji/1005491470486208632.png"), true);
        new ImageIcon("nerd", new ResourceLocation("textures/emoji/1005491467990614107.png"), true);
        new ImageIcon("hmm", new ResourceLocation("textures/emoji/1005491466736500817.png"), true);
        new ImageIcon("raised_eyebrow", new ResourceLocation("textures/emoji/1005491463179747429.png"), true);
        new ImageIcon("crazy", new ResourceLocation("textures/emoji/1005491460352774255.png"), true);
        new ImageIcon("wink_silly", new ResourceLocation("textures/emoji/1005491459165782097.png"), true);
        new ImageIcon("extremely_silly", new ResourceLocation("textures/emoji/1005491457982996635.png"), true);
        new ImageIcon("silly", new ResourceLocation("textures/emoji/1005491456611467285.png"), true);
        new ImageIcon("yum", new ResourceLocation("textures/emoji/1005491455344783370.png"), true);
        new ImageIcon("blush_kiss", new ResourceLocation("textures/emoji/1005491454115852378.png"), true);
        new ImageIcon("kiss", new ResourceLocation("textures/emoji/1005491451846721718.png"), true);
        new ImageIcon("kiss_wink", new ResourceLocation("textures/emoji/1005491450668134620.png"), true);
        new ImageIcon("love", new ResourceLocation("textures/emoji/1005491448982012096.png"), true);
        new ImageIcon("heart_eyes", new ResourceLocation("textures/emoji/1005491448138973264.png"), true);
        new ImageIcon("uhoh", new ResourceLocation("textures/emoji/1005491446842929152.png"), true);
        new ImageIcon("relief", new ResourceLocation("textures/emoji/1005491445337161829.png"), true);
        new ImageIcon("wink", new ResourceLocation("textures/emoji/1005491444208906340.png"), true);
        new ImageIcon("downwards_smile", new ResourceLocation("textures/emoji/1005491443101618227.png"), true);
        new ImageIcon("slight_smile", new ResourceLocation("textures/emoji/1005491441881075752.png"), true);
        new ImageIcon("angel", new ResourceLocation("textures/emoji/1005491440253685821.png"), true);
        new ImageIcon("blush", new ResourceLocation("textures/emoji/1005491439574196224.png"), true);
        new ImageIcon("rofl", new ResourceLocation("textures/emoji/1005491437124714536.png"), true);
        new ImageIcon("roy", new ResourceLocation("textures/emoji/1005491435899998338.png"), true);
        new ImageIcon("guilty", new ResourceLocation("textures/emoji/1005491434566189176.png"), true);
        new ImageIcon("lol", new ResourceLocation("textures/emoji/1005491428278935664.png"), true);
        new ImageIcon("cheer", new ResourceLocation("textures/emoji/1005491425275822152.png"), true);
        new ImageIcon("smile", new ResourceLocation("textures/emoji/1005491424495685673.png"), true);
        new ImageIcon("happy", new ResourceLocation("textures/emoji/1005491423170285638.png"), true);
        new ImageIcon("downvote", new ResourceLocation("textures/emoji/downvote.png"), true);
        new ImageIcon("upvote", new ResourceLocation("textures/emoji/upvote.png"), true);
        new ImageIcon("left", new ResourceLocation("textures/emoji/left.png"), true);
        new ImageIcon("right", new ResourceLocation("textures/emoji/right.png"), true);

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
        if (FormattingConfig.fancyRendering()) {
            Minecraft.getMinecraft().fontRendererObj = imageIconRenderer;
        }
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
        eventBus.register(guiDisplayHandler);
        eventBus.register(new ResolutionUtil());
        eventBus.register(new BwRanks());
        eventBus.register(new GameMenuOpen());
        eventBus.register(new SbbRenderer());
        eventBus.register(new cc.woverflow.hysentials.handlers.SbbRenderer());


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
        eventBus.register(new MUtils());
        eventBus.register(new Limit256());
        eventBus.register(new QuestHandler());
        eventBus.register(new CapeHandler());
        eventBus.register(cubitCompanion = new CubitCompanion());
        eventBus.register(pepperCompanion = new PepperCompanion());
        eventBus.register(miyaCompanion = new MiyaCompanion());
        eventBus.register(hamsterCompanion = new HamsterCompanion());
        eventBus.register(technoCrown = new TechnoCrown());
        eventBus.register(kzeroBundle = new KzeroBundle());
        CatHat.loadCatHats();
        BackpackCosmetic.loadBackpacks();
        eventBus.register(new ContainerHandler());
        eventBus.register(new FormatPlayerName());

        new Renderer();

        EventManager.INSTANCE.register(new BwRanks());

        HysentialsUtilsKt.init();

//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            JSONArray array = new JSONArray();
//            for (SBBoxes box : SBBoxes.boxes) {
//                array.put(box.save());
//            }
//            JSONObject object = new JSONObject();
//            object.put("lines", array);
//            sbBoxes.jsonObject = object;
//            sbBoxes.save();
//
//            macroJson.save();
//        }));
    }

    public void sendMessage(String message) {
        UChat.chat(HysentialsConfig.chatPrefix + " " + ChatColor.Companion.translateAlternateColorCodes('&', message));
    }

    public HysentialsConfig getConfig() {
        return config;
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
