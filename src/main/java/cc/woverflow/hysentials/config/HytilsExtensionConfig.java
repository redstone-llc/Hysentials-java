//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package cc.woverflow.hysentials.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.ConfigUtils;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.PageLocation;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.BlockHighlightConfig;
import cc.woverflow.hytils.config.HytilsConfig;
import com.google.common.collect.Lists;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class HytilsExtensionConfig extends Config {
    @Text(
            name = "API Key",
            category = "API",
            secure = true
    )
    public static String apiKey = HytilsConfig.apiKey;
    @Switch(
            name = "Auto Start",
            category = "Automatic",
            subcategory = "General"
    )
    public static boolean autoStart = HytilsConfig.autoStart;
    @Switch(
            name = "Auto Queue",
            category = "Automatic",
            subcategory = "Game"
    )
    public static boolean autoQueue = HytilsConfig.autoQueue;
    @Slider(
            name = "Auto Queue Delay",
            category = "Automatic",
            subcategory = "Game",
            min = 0.0F,
            max = 10.0F
    )
    public static int autoQueueDelay = HytilsConfig.autoQueueDelay;
    @Switch(
            name = "Auto-Complete Play Commands",
            category = "Automatic",
            subcategory = "Game"
    )
    public static boolean autocompletePlayCommands = HytilsConfig.autocompletePlayCommands;
    @Switch(
            name = "Limbo Play Helper",
            category = "Automatic",
            subcategory = "Game"
    )
    public static boolean limboPlayCommandHelper = HytilsConfig.limboPlayCommandHelper;
    @Switch(
            name = "Auto GL",
            category = "Automatic",
            subcategory = "AutoGL"
    )
    public static boolean autoGL = HytilsConfig.autoGL;
    @Dropdown(
            name = "Auto GL Phrase",
            category = "Automatic",
            subcategory = "AutoGL",
            options = {"glhf", "Good Luck", "GL", "Have a good game!", "gl", "Good luck!"}
    )
    public static int glPhrase = HytilsConfig.glPhrase;
    @Switch(
            name = "Anti GL",
            category = "Automatic",
            subcategory = "AutoGL"
    )
    public static boolean antiGL = HytilsConfig.antiGL;
    @Switch(
            name = "Auto Friend",
            category = "Automatic",
            subcategory = "Social"
    )
    public static boolean autoFriend = HytilsConfig.autoFriend;
    @Switch(
            name = "Automatically Check GEXP",
            category = "Automatic",
            subcategory = "Stats"
    )
    public static boolean autoGetGEXP = HytilsConfig.autoGetGEXP;
    @DualOption(
            name = "GEXP Mode",
            category = "Automatic",
            subcategory = "Stats",
            left = "Daily",
            right = "Weekly"
    )
    public static boolean gexpMode = HytilsConfig.gexpMode;
    @Switch(
            name = "Automatically Check Winstreak",
            category = "Automatic",
            subcategory = "Stats"
    )
    public static boolean autoGetWinstreak = HytilsConfig.autoGetWinstreak;
    @Switch(
            name = "Hide Locraw Messages",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean hideLocraw = HytilsConfig.hideLocraw;
    @Switch(
            name = "Remove Lobby Statuses",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean lobbyStatus = HytilsConfig.lobbyStatus;
    @Switch(
            name = "Remove Mystery Box Rewards",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean mysteryBoxAnnouncer = HytilsConfig.mysteryBoxAnnouncer;
    @Switch(
            name = "Remove Soul Well Announcements",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean soulWellAnnouncer = HytilsConfig.soulWellAnnouncer;
    @Switch(
            name = "Remove Game Announcements",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean gameAnnouncements = HytilsConfig.gameAnnouncements;
    @Switch(
            name = "Remove Hype Limit Reminder",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean hypeLimitReminder = HytilsConfig.hypeLimitReminder;
    @Switch(
            name = "Player AdBlocker",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean playerAdBlock = HytilsConfig.playerAdBlock;
    @Switch(
            name = "Remove BedWars Advertisements",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean bedwarsAdvertisements = HytilsConfig.bedwarsAdvertisements;
    @Switch(
            name = "Remove Friend/Guild Statuses",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean connectionStatus = HytilsConfig.connectionStatus;
    @Switch(
            name = "Remove Guild MOTD",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean guildMotd = HytilsConfig.guildMotd;
    @Switch(
            name = "Remove Chat Emojis",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean mvpEmotes = HytilsConfig.mvpEmotes;
    @Switch(
            name = "Remove Server Connected Messages",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean serverConnectedMessages = HytilsConfig.serverConnectedMessages;
    @Switch(
            name = "Remove Game Tips Messages",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean gameTipMessages = HytilsConfig.gameTipMessages;
    @Switch(
            name = "Remove Auto Activated Quest Messages",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean questsMessages = HytilsConfig.questsMessages;
    @Switch(
            name = "Remove Stats Messages",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean statsMessages = HytilsConfig.statsMessages;
    @Switch(
            name = "Remove Curse of Spam Messages",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean curseOfSpam = HytilsConfig.curseOfSpam;
    @Switch(
            name = "Remove Bridge Self Goal Death Messages",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean bridgeOwnGoalDeath = HytilsConfig.bridgeOwnGoalDeath;
    @Switch(
            name = "Remove Duels No Stats Change Messages",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean duelsNoStatsChange = HytilsConfig.duelsNoStatsChange;
    @Switch(
            name = "Remove Block Trail Disabled Messages",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean duelsBlockTrail = HytilsConfig.duelsBlockTrail;
    @Switch(
            name = "Remove SkyBlock Welcome Messages",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean skyblockWelcome = HytilsConfig.skyblockWelcome;
    @Switch(
            name = "Remove Gift Messages",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean giftBlocker = HytilsConfig.giftBlocker;
    @Switch(
            name = "Remove Seasonal Simulator Collection Messages",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean grinchPresents = HytilsConfig.grinchPresents;
    @Switch(
            name = "Remove Earned Coins and Experience Messages",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean earnedCoinsAndExp = HytilsConfig.earnedCoinsAndExp;
    @Switch(
            name = "Remove Replay Messages",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean replayMessage = HytilsConfig.replayMessage;
    @Switch(
            name = "Remove Tip Messages",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean tipMessage = HytilsConfig.tipMessage;
    @Switch(
            name = "Remove Online Status Messages",
            category = "Chat",
            subcategory = "Toggles"
    )
    public static boolean onlineStatus = HytilsConfig.onlineStatus;
    @Switch(
            name = "Trim Line Separators",
            category = "Chat",
            subcategory = "Visual"
    )
    public static boolean lineBreakerTrim = HytilsConfig.lineBreakerTrim;
    @Switch(
            name = "Clean Line Separators",
            category = "Chat",
            subcategory = "Visual"
    )
    public static boolean cleanLineSeparator = HytilsConfig.cleanLineSeparator;
    @Switch(
            name = "White Chat",
            category = "Chat",
            subcategory = "Visual"
    )
    public static boolean whiteChat = HytilsConfig.whiteChat;
    @Switch(
            name = "White Private Messages",
            category = "Chat",
            subcategory = "Visual"
    )
    public static boolean whitePrivateMessages = HytilsConfig.whitePrivateMessages;
    @Switch(
            name = "Colored Friend/Guild Statuses",
            category = "Chat",
            subcategory = "Visual"
    )
    public static boolean coloredStatuses = HytilsConfig.coloredStatuses;
    @Switch(
            name = "Cleaner Game Start Counter",
            category = "Chat",
            subcategory = "Visual"
    )
    public static boolean cleanerGameStartAnnouncements = HytilsConfig.cleanerGameStartAnnouncements;
    @Switch(
            name = "Short Channel Names",
            category = "Chat",
            subcategory = "Visual"
    )
    public static boolean shortChannelNames = HytilsConfig.shortChannelNames;
    @Switch(
            name = "Game Status Restyle",
            category = "Chat",
            subcategory = "Restyler"
    )
    public static boolean gameStatusRestyle = HytilsConfig.gameStatusRestyle;
    @Switch(
            name = "Player Count Before Player Name",
            category = "Chat",
            subcategory = "Restyler"
    )
    public static boolean playerCountBeforePlayerName = HytilsConfig.playerCountBeforePlayerName;
    @Switch(
            name = "Player Count on Player Leave",
            category = "Chat",
            subcategory = "Restyler"
    )
    public static boolean playerCountOnPlayerLeave = HytilsConfig.playerCountOnPlayerLeave;
    @Switch(
            name = "Player Count Padding",
            category = "Chat",
            subcategory = "Restyler"
    )
    public static boolean padPlayerCount = HytilsConfig.padPlayerCount;
    @Switch(
            name = "Party Chat Swapper",
            category = "Chat",
            subcategory = "Parties"
    )
    public static boolean chatSwapper = HytilsConfig.chatSwapper;
    @Dropdown(
            name = "Chat Swapper Channel",
            category = "Chat",
            subcategory = "Parties",
            options = {"ALL", "GUILD", "OFFICER"}
    )
    public static int chatSwapperReturnChannel = HytilsConfig.chatSwapperReturnChannel;
    @Switch(
            name = "Swap Chatting Tab With Chat Swapper",
            category = "Chat",
            subcategory = "Parties"
    )
    public static boolean chattingIntegration = HytilsConfig.chattingIntegration;
    @Switch(
            name = "Remove All Chat Message",
            category = "Chat",
            subcategory = "Parties"
    )
    public static boolean hideAllChatMessage = HytilsConfig.hideAllChatMessage;
    @Switch(
            name = "Thank Watchdog",
            category = "Chat",
            subcategory = "Watchdog"
    )
    public static boolean thankWatchdog = HytilsConfig.thankWatchdog;
    @Switch(
            name = "Auto Chat Report Confirm",
            category = "Automatic",
            subcategory = "Chat"
    )
    public static boolean autoChatReportConfirm = HytilsConfig.autoChatReportConfirm;
    @Switch(
            name = "Auto Party Warp Confirm",
            category = "Automatic",
            subcategory = "Chat"
    )
    public static boolean autoPartyWarpConfirm = HytilsConfig.autoPartyWarpConfirm;
    @Switch(
            name = "Guild Welcome Message",
            category = "Chat",
            subcategory = "Guild"
    )
    public static boolean guildWelcomeMessage = HytilsConfig.guildWelcomeMessage;
    @Switch(
            name = "Shout Cooldown",
            category = "Chat",
            subcategory = "Cooldown"
    )
    public static boolean preventShoutingOnCooldown = HytilsConfig.preventShoutingOnCooldown;
    @Switch(
            name = "Non Speech Cooldown",
            category = "Chat",
            subcategory = "Cooldown"
    )
    public static boolean preventNonCooldown = HytilsConfig.preventNonCooldown;
    @Switch(
            name = "AutoWB",
            category = "Chat",
            subcategory = "AutoWB"
    )
    public static boolean autoWB = HytilsConfig.autoWB;
    @Switch(
            name = "Guild AutoWB",
            category = "Chat",
            subcategory = "AutoWB"
    )
    public static boolean guildAutoWB = HytilsConfig.guildAutoWB;
    @Switch(
            name = "Friend AutoWB",
            category = "Chat",
            subcategory = "AutoWB"
    )
    public static boolean friendsAutoWB = HytilsConfig.friendsAutoWB;
    @Slider(
            name = "AutoWB Delay",
            category = "Chat",
            subcategory = "AutoWB",
            min = 2.0F,
            max = 10.0F
    )
    public static int autoWBCooldown = HytilsConfig.autoWBCooldown;
    @Text(
            name = "AutoWB Message",
            category = "Chat",
            subcategory = "AutoWB",
            size = 2
    )
    public static String autoWBMessage1 = HytilsConfig.autoWBMessage1;
    @Switch(
            name = "Random AutoWB Messages",
            category = "Chat",
            subcategory = "AutoWB"
    )
    public static boolean randomAutoWB = HytilsConfig.randomAutoWB;
    @Text(
            name = "First Random Message",
            category = "Chat",
            subcategory = "AutoWB",
            size = 2
    )
    public static String autoWBMessage2 = HytilsConfig.autoWBMessage2;
    @Text(
            name = "Second Random Message",
            category = "Chat",
            subcategory = "AutoWB",
            size = 2
    )
    public static String autoWBMessage3 = HytilsConfig.autoWBMessage3;
    @Text(
            name = "Third Random Message",
            category = "Chat",
            subcategory = "AutoWB",
            size = 2
    )
    public static String autoWBMessage4 = HytilsConfig.autoWBMessage4;
    @Text(
            name = "Fourth Random Message",
            category = "Chat",
            subcategory = "AutoWB",
            size = 2
    )
    public static String autoWBMessage5 = HytilsConfig.autoWBMessage5;
    @Text(
            name = "Fifth Random Message",
            category = "Chat",
            subcategory = "AutoWB",
            size = 2
    )
    public static String autoWBMessage6 = HytilsConfig.autoWBMessage6;
    @Text(
            name = "Sixth Random Message",
            category = "Chat",
            subcategory = "AutoWB",
            size = 2
    )
    public static String autoWBMessage7 = HytilsConfig.autoWBMessage7;
    @Text(
            name = "Seventh Random Message",
            category = "Chat",
            subcategory = "AutoWB",
            size = 2
    )
    public static String autoWBMessage8 = HytilsConfig.autoWBMessage8;
    @Text(
            name = "Eighth Random Message",
            category = "Chat",
            subcategory = "AutoWB",
            size = 2
    )
    public static String autoWBMessage9 = HytilsConfig.autoWBMessage9;
    @Text(
            name = "Ninth Random Message",
            category = "Chat",
            subcategory = "AutoWB",
            size = 2
    )
    public static String autoWBMessage10 = HytilsConfig.autoWBMessage10;
    @Switch(
            name = "Notify Mining Fatigue",
            category = "General",
            subcategory = "Potion Effects"
    )
    public static boolean notifyMiningFatigue = HytilsConfig.notifyMiningFatigue;
    @Switch(
            name = "Disable Mining Fatigue Notification in SkyBlock",
            category = "General",
            subcategory = "Potion Effects"
    )
    public static boolean disableNotifyMiningFatigueSkyblock = HytilsConfig.disableNotifyMiningFatigueSkyblock;
    @Switch(
            name = "Hide NPCs in Tab",
            category = "General",
            subcategory = "Tab"
    )
    public static boolean hideNpcsInTab = HytilsConfig.hideNpcsInTab;
    @Switch(
            name = "Don't Hide Important NPCs",
            category = "General",
            subcategory = "Tab"
    )
    public static boolean keepImportantNpcsInTab = HytilsConfig.keepImportantNpcsInTab;
    @Switch(
            name = "Hide Guild Tags in Tab",
            category = "General",
            subcategory = "Tab"
    )
    public static boolean hideGuildTagsInTab = HytilsConfig.hideGuildTagsInTab;
    @Switch(
            name = "Hide Player Ranks in Tab",
            category = "General",
            subcategory = "Tab"
    )
    public static boolean hidePlayerRanksInTab = HytilsConfig.hidePlayerRanksInTab;
    @Dropdown(
            name = "Highlight Friends In Tab",
            category = "General",
            subcategory = "Tab",
            options = {"Off", "Left of Name", "Right of Name"}
    )
    public static int highlightFriendsInTab = HytilsConfig.highlightFriendsInTab;
    @Dropdown(
            name = "Highlight Self In Tab",
            category = "General",
            subcategory = "Tab",
            options = {"Off", "Left of Name", "Right of Name"}
    )
    public static int highlightSelfInTab = HytilsConfig.highlightSelfInTab;
    @Switch(
            name = "Cleaner Tab in SkyBlock",
            category = "General",
            subcategory = "Tab"
    )
    public static boolean cleanerSkyblockTabInfo = HytilsConfig.cleanerSkyblockTabInfo;
    @Switch(
            name = "Hide Ping in Tab",
            category = "General",
            subcategory = "Tab"
    )
    public static boolean hidePingInTab = HytilsConfig.hidePingInTab;
    @Switch(
            name = "Broadcast Achievements",
            category = "General",
            subcategory = "Guilds"
    )
    public static boolean broadcastAchievements = HytilsConfig.broadcastAchievements;
    @Switch(
            name = "Broadcast Levelup",
            category = "General",
            subcategory = "Guilds"
    )
    public static boolean broadcastLevelup = HytilsConfig.broadcastLevelup;
    @Switch(
            name = "Notify When Kicked From Game",
            category = "Game",
            subcategory = "Chat"
    )
    public static boolean notifyWhenKick = HytilsConfig.notifyWhenKick;
    @Switch(
            name = "Put Notify Message In Capital Letters",
            category = "Game",
            subcategory = "Chat"
    )
    public static boolean putInCaps = HytilsConfig.putInCaps;
    @Switch(
            name = "Highlight Opened Chests",
            category = "Game",
            subcategory = "Visual"
    )
    public static boolean highlightChests = HytilsConfig.highlightChests;
    @Color(
            name = "Highlight Color",
            category = "Game",
            subcategory = "Visual"
    )
    public static OneColor highlightChestsColor = HytilsConfig.highlightChestsColor;
    @Switch(
            name = "UHC Overlay",
            category = "Game",
            subcategory = "Visual"
    )
    public static boolean uhcOverlay = HytilsConfig.uhcOverlay;
    @Slider(
            name = "UHC Overlay Multiplier",
            category = "Game",
            subcategory = "Visual",
            min = 1.0F,
            max = 5.0F
    )
    public static float uhcOverlayMultiplier = HytilsConfig.uhcOverlayMultiplier;
    @Switch(
            name = "UHC Middle Waypoint",
            category = "Game",
            subcategory = "Visual"
    )
    public static boolean uhcMiddleWaypoint = HytilsConfig.uhcMiddleWaypoint;
    @Text(
            name = "UHC Middle Waypoint Text",
            category = "Game",
            subcategory = "Visual"
    )
    public static String uhcMiddleWaypointText = HytilsConfig.uhcMiddleWaypointText;
    @Switch(
            name = "Lower Render Distance in Sumo",
            category = "Game",
            subcategory = "Visual"
    )
    public static boolean sumoRenderDistance = HytilsConfig.sumoRenderDistance;
    @Slider(
            name = "Sumo Render Distance",
            category = "Game",
            subcategory = "Visual",
            min = 1.0F,
            max = 5.0F
    )
    public static int sumoRenderDistanceAmount = HytilsConfig.sumoRenderDistanceAmount;
    @Switch(
            name = "Hide Armor",
            category = "Game",
            subcategory = "Visual"
    )
    public static boolean hideArmor = HytilsConfig.hideArmor;
    @Switch(
            name = "Hide Useless Game Nametags",
            category = "Game",
            subcategory = "Visual"
    )
    public static boolean hideUselessArmorStandsGame = HytilsConfig.hideUselessArmorStandsGame;
    @Switch(
            name = "Hardcore Hearts",
            category = "Game",
            subcategory = "Visual"
    )
    public static boolean hardcoreHearts = HytilsConfig.hardcoreHearts;
    @Switch(
            name = "Pit Lag Reducer",
            category = "Game",
            subcategory = "Visual"
    )
    public static boolean pitLagReducer = HytilsConfig.pitLagReducer;
    @Switch(
            name = "Hide Game Starting Titles",
            category = "Game",
            subcategory = "Visual"
    )
    public static boolean hideGameStartingTitles = HytilsConfig.hideGameStartingTitles;
    @Switch(
            name = "Hide Game Ending Titles",
            category = "Game",
            subcategory = "Visual"
    )
    public static boolean hideGameEndingTitles = HytilsConfig.hideGameEndingTitles;
    @Switch(
            name = "Hide Game Ending Countdown Titles",
            category = "Game",
            subcategory = "Visual"
    )
    public static boolean hideGameEndingCountdownTitles = HytilsConfig.hideGameEndingCountdownTitles;
    @Switch(
            name = "Height Overlay",
            category = "Game",
            subcategory = "Visual"
    )
    public static boolean heightOverlay = HytilsConfig.heightOverlay;
    @Slider(
            name = "Height Overlay Tint Multiplier",
            category = "Game",
            subcategory = "Visual",
            min = 1.0F,
            max = 1000.0F
    )
    public static int overlayAmount = HytilsConfig.overlayAmount;
    @Switch(
            name = "Edit Height Overlay Manually",
            category = "Game",
            subcategory = "Visual"
    )
    public static boolean manuallyEditHeightOverlay = HytilsConfig.manuallyEditHeightOverlay;
    @Page(
            name = "Manual Height Overlay Editor",
            location = PageLocation.BOTTOM,
            category = "Game",
            subcategory = "Visual"
    )
    public static BlockHighlightConfig blockHighlightConfig = HytilsConfig.blockHighlightConfig;
    @Switch(
            name = "Hide Duels Cosmetics",
            category = "Game",
            subcategory = "Visual"
    )
    public static boolean hideDuelsCosmetics = HytilsConfig.hideDuelsCosmetics;
    @Switch(
            name = "Hide Actionbar in Housing",
            category = "Game",
            subcategory = "Visual"
    )
    public static boolean hideHousingActionBar = HytilsConfig.hideHousingActionBar;
    @Switch(
            name = "Hide Actionbar in Dropper",
            category = "Game",
            subcategory = "Visual"
    )
    public static boolean hideDropperActionBar = HytilsConfig.hideDropperActionBar;
    @Switch(
            name = "Remove Non-NPCs in SkyBlock",
            category = "Game",
            subcategory = "Visual"
    )
    public static boolean hideNonNPCs = HytilsConfig.hideNonNPCs;
    @Switch(
            name = "Middle Waypoint Beacon in MiniWalls",
            category = "Game",
            subcategory = "Visual"
    )
    public static boolean miniWallsMiddleBeacon = HytilsConfig.miniWallsMiddleBeacon;
    @Color(
            name = "MiniWalls Beacon Color",
            category = "Game",
            subcategory = "Visual"
    )
    public static OneColor miniWallsMiddleBeaconColor = HytilsConfig.miniWallsMiddleBeaconColor;
    @Switch(
            name = "Mute Housing Music",
            category = "Game",
            subcategory = "Sound"
    )
    public static boolean muteHousingMusic = HytilsConfig.muteHousingMusic;
    @Switch(
            name = "Notify When Blocks Run Out",
            category = "Game",
            subcategory = "Sound"
    )
    public static boolean blockNotify = HytilsConfig.blockNotify;
    @Slider(
            name = "Block Number",
            category = "Game",
            subcategory = "Sound",
            min = 4.0F,
            max = 20.0F
    )
    public static int blockNumber = HytilsConfig.blockNumber;
    @Dropdown(
            name = "Block Notify Sound",
            category = "Game",
            subcategory = "Sound",
            options = {"Hypixel Ding", "Golem Hit", "Blaze Hit", "Anvil Land", "Horse Death", "Ghast Scream", "Guardian Floop", "Cat Meow", "Dog Bark"}
    )
    public static int blockNotifySound = HytilsConfig.blockNotifySound;
    @Switch(
            name = "Hide Lobby NPCs",
            category = "Lobby",
            subcategory = "Entities"
    )
    public static boolean npcHider = HytilsConfig.npcHider;
    @Switch(
            name = "Hide Useless Lobby Nametags",
            category = "Lobby",
            subcategory = "Entities"
    )
    public static boolean hideUselessArmorStands = HytilsConfig.hideUselessArmorStands;
    @Switch(
            name = "Remove Limbo AFK Title",
            category = "Lobby",
            subcategory = "General"
    )
    public static boolean hideLimboTitle = HytilsConfig.hideLimboTitle;
    @Switch(
            name = "Limbo Limiter",
            category = "Lobby",
            subcategory = "General"
    )
    public static boolean limboLimiter = HytilsConfig.limboLimiter;
    @Slider(
            name = "Limbo Limiter FPS",
            category = "Lobby",
            subcategory = "General",
            min = 1.0F,
            max = 60.0F
    )
    public static int limboFPS = HytilsConfig.limboFPS;
    @Switch(
            name = "Limbo PM Ding",
            category = "Lobby",
            subcategory = "General"
    )
    public static boolean limboDing = HytilsConfig.limboDing;
    @Switch(
            name = "Hide Lobby Bossbars",
            category = "Lobby",
            subcategory = "GUI"
    )
    public static boolean lobbyBossbar = HytilsConfig.lobbyBossbar;
    @Switch(
            name = "Mystery Box Star",
            category = "Lobby",
            subcategory = "GUI"
    )
    public static boolean mysteryBoxStar = HytilsConfig.mysteryBoxStar;
    public static int configNumber = HytilsConfig.configNumber;
    @Exclude
    public static final ArrayList<String> wbMessages;

    public HytilsExtensionConfig(Mod mod) {
        super(mod, "hytilsreborn.json");
        this.initialize();

        if (configNumber != 2) {
            if (configNumber == 1) {
                overlayAmount = 300;
            }

            configNumber = 2;
            this.save();
        }

        this.addDependency("autoQueueDelay", "autoQueue");
        this.addDependency("gexpMode", "autoGetGEXP");
        this.addDependency("glPhrase", "autoGL");
        this.addDependency("guildAutoWB", "autoWB");
        this.addDependency("friendsAutoWB", "autoWB");
        this.addDependency("autoWBCooldown", "autoWB");
        this.addDependency("randomAutoWB", "autoWB");
        Iterator var5 = Lists.newArrayList(new String[]{"autoWBMessage1", "autoWBMessage2", "autoWBMessage3", "autoWBMessage4", "autoWBMessage5", "autoWBMessage6", "autoWBMessage7", "autoWBMessage8", "autoWBMessage9", "autoWBMessage10"}).iterator();

        while(var5.hasNext()) {
            String property = (String)var5.next();
            this.addDependency(property, "autoWB");
        }

        Iterator var = Arrays.stream(this.getClass().getDeclaredFields()).iterator();

        while (var.hasNext()) {
            Field field = (Field)var.next();
            String property = field.getName();

            this.addListener(property, () -> updateHytils(property));
        }
        this.addDependency("disableNotifyMiningFatigueSkyblock", "notifyMiningFatigue");
        this.addDependency("chatSwapperReturnChannel", "chatSwapper");
        this.addDependency("chattingIntegration", "chatSwapper");
        this.addDependency("hideAllChatMessage", "chatSwapper");
        this.addDependency("playerCountBeforePlayerName", "gameStatusRestyle");
        this.addDependency("playerCountOnPlayerLeave", "gameStatusRestyle");
        this.addDependency("padPlayerCount", "gameStatusRestyle");
        this.addDependency("blockNumber", "blockNotify");
        this.addDependency("blockNotifySound", "blockNotify");
        this.addDependency("blockNotifySound", "blockNotify");
        this.addDependency("keepImportantNpcsInTab", "hideNpcsInTab");
        this.addDependency("highlightChestsColor", "highlightChests");
        this.addDependency("uhcOverlayMultiplier", "uhcOverlay");
        this.addDependency("uhcMiddleWaypointText", "uhcMiddleWaypoint");
        this.addDependency("miniWallsMiddleBeaconColor", "miniWallsMiddleBeacon");
        this.addDependency("sumoRenderDistanceAmount", "sumoRenderDistance");
        this.addDependency("overlayAmount", "heightOverlay");
        this.addDependency("putInCaps", "notifyWhenKick");
        this.addDependency("manuallyEditHeightOverlay", "heightOverlay");
    }

    public void initialize() {
        boolean migrate = false;
        File profileFile = ConfigUtils.getProfileFile(configFile);
        if (profileFile.exists()) load();
        if (!profileFile.exists()) {
            if (mod.migrator != null) migrate = true;
            else save();
        }
        mod.config = this;
        generateOptionList(this, mod.defaultPage, mod, migrate);
        if (migrate) save();
    }

    public void hideTabulous() {
        hideNpcsInTab = false;
        keepImportantNpcsInTab = false;
        hideGuildTagsInTab = false;
        hidePlayerRanksInTab = false;
        hidePingInTab = false;
        cleanerSkyblockTabInfo = false;
        this.save();
        this.addDependency("hideNpcsInTab", false);
        this.addDependency("keepImportantNpcsInTab", false);
        this.addDependency("hideGuildTagsInTab", false);
        this.addDependency("hidePlayerRanksInTab", false);
        this.addDependency("hidePingInTab", false);
        this.addDependency("cleanerSkyblockTabInfo", false);
    }

    private void updateHytils(String property) {
        try {
            HytilsConfig.class.getField(property).set(HytilsConfig.class, this.getClass().getField(property).get(this.getClass()));
            HytilsReborn.INSTANCE.getConfig().save();
            HytilsReborn.INSTANCE.getConfig().load();
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void openGui() {
        this.load();
        super.openGui();
    }

    static {
        highlightChestsColor = new OneColor(java.awt.Color.RED);
        uhcOverlayMultiplier = 1.0F;
        uhcMiddleWaypointText = "0,0";
        sumoRenderDistanceAmount = 2;
        overlayAmount = 300;
        blockHighlightConfig = new BlockHighlightConfig();
        miniWallsMiddleBeaconColor = new OneColor(java.awt.Color.BLUE);
        blockNumber = 10;
        blockNotifySound = 0;
        limboFPS = 15;
        configNumber = 0;
        wbMessages = new ArrayList();
    }
}
