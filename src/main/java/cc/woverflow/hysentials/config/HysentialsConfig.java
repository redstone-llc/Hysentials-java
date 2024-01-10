package cc.woverflow.hysentials.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.annotations.Button;
import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.core.OneKeyBind;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.gui.OneConfigGui;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.gui.GuiUtils;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.hysentialMods.ChatConfig;
import cc.woverflow.hysentials.config.hysentialMods.FormattingConfig;
import cc.woverflow.hysentials.config.hysentialMods.HousingConfig;
import cc.woverflow.hysentials.cosmetic.CosmeticGui;
import cc.woverflow.hysentials.updateGui.UpdateChecker;

import cc.woverflow.hysentials.util.ImageIconRenderer;
import cc.woverflow.hysentials.utils.RedstoneRepo;
import cc.woverflow.hysentials.utils.UpdateNotes;
import cc.woverflow.hytils.HytilsReborn;
import net.minecraft.client.Minecraft;
import org.json.JSONObject;

import java.awt.*;
import java.io.File;
import java.util.List;

import static cc.woverflow.hysentials.guis.actionLibrary.ClubActionViewer.toList;

public class HysentialsConfig extends Config {
    @Text(
        name = "Chat Prefix",
        category = "General",
        subcategory = "General",
        description = "The prefix of most Hysentials related messages, so you know the message is a result of Hysentials and not other mods."
    )
    public static String chatPrefix = "&b[HYSENTIALS]";

    @Dropdown(
        name = "Update Channel",
        category = "General",
        subcategory = "General",
        description = "The update channel you want to use.",
        options = {"Release", "Beta", "Dev"}
    )
    public static int updateChannel = 1;

    @Button(
        name = "Check for Updates",
        category = "General",
        subcategory = "General",
        description = "Check for updates for Hysentials.",
        text = "Check for Updates"
    )
    public void checkForUpdates() {
        UpdateChecker.Companion.checkUpdateAndOpenMenu();
    }

    @KeyBind(
        name = "Open Cosmetics",
        category = "General",
        subcategory = "Keybinds",
        description = "The keybind to open the cosmetics menu."
    )
    public static OneKeyBind keyBind = new OneKeyBind(UKeyboard.KEY_K);

    @KeyBind(
        name = "Open Online Players",
        category = "General",
        subcategory = "Keybinds",
        description = "The keybind to open the online players menu."
    )
    public static OneKeyBind onlinePlayersKeyBind = new OneKeyBind(UKeyboard.KEY_LCONTROL, UKeyboard.KEY_TAB);


    @Switch(
        name = "Global Chat Enabled",
        category = "General",
        subcategory = "Chat",
        description = "Enable global chat. This will allow you to chat with other players who are using Hysentials."
    )
    public static boolean globalChatEnabled = true;


    @Switch(
        name = "Fancy Formatting",
        category = "General",
        subcategory = "Fancy Formatting",
        description = "Enable fancy formatting. This will allow you to see rank images, hex colors, and other things."
    )
    public static boolean fancyFormatting = true;

    @Switch(
        name = "Fancy Ranks",
        category = "General",
        subcategory = "Fancy Formatting",
        description = "Enable futuristic ranks. This will allow you to see an image as a users rank, aswell as other things."
    )
    public static boolean futuristicRanks = true;

    @Switch(
        name = "Hex Colors",
        category = "General",
        subcategory = "Fancy Formatting",
        description = "Enable hex colors. This will allow you to see hex colors in chat."
    )
    public static boolean hexColors = true;

    @Switch(
        name = "Channel Formatting",
        category = "General",
        subcategory = "Fancy Formatting",
        description = "Enable channel formatting. This enables fancier formatting for channels."
    )
    public static boolean channelFormatting = true;

    @Button(
        name = "Rank Image Config",
        category = "General",
        subcategory = "Fancy Formatting",
        description = "Opens the rank image config folder.",
        text = "Open Folder")
    public void openRankImageConfig() {
        Desktop desktop = Desktop.getDesktop();
        File directory = new File("./config/hysentials/imageicons");
        try {
            desktop.open(directory);
        } catch (Exception e) {
            UChat.chat("&cError opening folder!");
            e.printStackTrace();
        }
    }

    @Button(
        name = "Hex Color Config",
        category = "General",
        subcategory = "Fancy Formatting",
        description = "Opens the rank hex color config file.",
        text = "Open File")
    public void openRankHexConfig() {
        Desktop desktop = Desktop.getDesktop();
        File directory = new File("./config/hysentials/colors.json");
        try {
            desktop.open(directory);
        } catch (Exception e) {
            UChat.chat("&cError opening file!");
            e.printStackTrace();
        }
    }

    @Button(
        name = "Install Hytils Reborn",
        category = "General",
        subcategory = "Hytils",
        description = "Installs Hytils Reborn for you.",
        text = "Install")
    public void installHytils() {
        if (!Hysentials.INSTANCE.isHytils) {
            Hysentials.INSTANCE.getLogger().info("Installing Hytils Reborn...");
            try {
                String request1 = NetworkUtils.getString("https://api.modrinth.com/v2/project/nF6YaBfO");
                JSONObject json1 = new JSONObject(request1);
                if (json1.isEmpty()) {
                    Hysentials.INSTANCE.getLogger().error("Error installing Hytils Reborn!");
                    return;
                }
                List<Object> versions = toList(json1.getJSONArray("versions"));
                String latestVersion = ((String) versions.get(versions.size() - 1));
                String request2 = NetworkUtils.getString("https://api.modrinth.com/v2/version/" + latestVersion);
                JSONObject json2 = new JSONObject(request2);
                if (json2.isEmpty()) {
                    Hysentials.INSTANCE.getLogger().error("Error installing Hytils Reborn!");
                    return;
                }
                JSONObject file = json2.getJSONArray("files").getJSONObject(0);
                RedstoneRepo repo = new RedstoneRepo(
                    file.getString("filename"),
                    "jar",
                    file.getInt("size"),
                    json2.getString("version_type"),
                    file.getString("url")
                );
                UpdateNotes notes = new UpdateNotes(
                    file.getString("filename"),
                    "https://cdn.modrinth.com/data/nF6YaBfO/5de4ce522bbc4af9229018cbaeefb117ec458648.png",
                    json2.getString("changelog")
                );

                UpdateChecker.Companion.installFromUrl(repo, notes);
            } catch (Exception e) {
                Hysentials.INSTANCE.getLogger().error("Error installing Hytils Reborn!");
                e.printStackTrace();
            }
        } else {
            Hysentials.INSTANCE.getLogger().error("Hytils Reborn is already installed!");
        }
    }

    @Button(
        name = "Additional Configs",
        category = "General",
        subcategory = "Hytils",
        description = "Opens the Hytils config page.",
        text = "OPEN")
    public void openHytilsConfig() {
        if (Hysentials.INSTANCE.isHytils) {
            HytilsReborn.INSTANCE.getConfig().openGui();
        } else {
            Hysentials.INSTANCE.getLogger().error("Hytils Reborn is not installed!");
        }
    }


    @Switch(
        name = "Remove Asterisk",
        category = "Housing",
        subcategory = "General",
        description = "Removes the asterisk from chat messages sent to the player."
    )
    public static boolean removeAsterisk = true;

    @Switch(
        name = "Housing Name Scoreboard",
        category = "Housing",
        subcategory = "General",
        description = "Adds the housing name to the scoreboards title."
    )
    public static boolean housingNameScoreboard = true;

    @Switch(
        name = "Shift+Right Click OpenInv",
        category = "Housing",
        subcategory = "General",
        description = "Toggles the ability to open a players inventory by shift right clicking them."
    )
    public static boolean shiftRightClickInv = true;

    @Switch(
        name = "Show Pets in game",
        category = "Comsetics",
        subcategory = "Pets",
        description = "Will allow pets to be shown in game."
    )
    public static boolean showPets = false;

    @Switch(
        name = "Disable Custom Capes",
        category = "Comsetics",
        subcategory = "Capes",
        description = "Will disable custom capes including their animations."
    )
    public static boolean disableCustomCapes = false;
    @Dropdown(
        name = "Cape Animation",
        category = "Comsetics",
        subcategory = "Capes",
        description = "Which animation the capes should render with.",
        options = {"Blocky Animation", "Silky Animation"}
    )
    public static int blockyCapes = 0;

    @Switch(
        name = "Wind Effect",
        category = "Comsetics",
        subcategory = "Capes",
        description = "Will give the capes a windy effect making it look like they are blowing in the wind."
    )
    public static boolean windEffect = true;


    // LOBBY

    @Switch(
        name = "Housing Lag Reducer",
        category = "Lobby",
        subcategory = "General",
        description = "Will reduce the lag in the housing lobby, by hiding armorstands further than 20 blocks away from the player."
    )
    public static boolean housingLagReducer = true;

    // Scoreboard Boxes
    @Color(
        name = "Color Picker",
        description = "Color for the boxes",
        category = "SBBoxes",
        subcategory = "General",
        allowAlpha = true
    )
    public static OneColor boxColor = new OneColor(0, 0, 0, 125);

    @Checkbox(
        name = "Box Shadows",
        description = "Enables box shadows",
        category = "SBBoxes",
        subcategory = "General"
    )
    public static boolean boxShadows = true;

    @Checkbox(
        name = "Scoreboard Boxes",
        description = "Enables scoreboard boxes",
        category = "SBBoxes",
        subcategory = "Boxes"
    )
    public static boolean scoreboardBoxes = true;

    @Checkbox(
        name = "Show Scoreboard",
        description = "Enables scoreboard",
        category = "SBBoxes",
        subcategory = "Boxes"
    )
    public static boolean showScoreboard = true;

    @Dropdown(
        name = "Border Radius",
        description = "Border radius of the boxes",
        category = "SBBoxes",
        subcategory = "Boxes",
        options = {"0", "2", "4"}
    )
    public static int scoreboardBoxesBorderRadius = 1;

    @Checkbox(
        name = "Action Bar",
        description = "Enables better action bar",
        category = "SBBoxes",
        subcategory = "Action Bar"
    )
    public static boolean actionBar = true;

    @Dropdown(
        name = "Border Radius",
        description = "Border radius of the action bar",
        category = "SBBoxes",
        subcategory = "Action Bar",
        options = {"0", "2", "4"}
    )
    public static int actionBarBorderRadius = 1;

    @Checkbox(
        name = "Scoreboard",
        description = "Enables better scoreboard",
        category = "SBBoxes",
        subcategory = "Scoreboard"
    )
    public static boolean scoreboard = true;

    @Checkbox(
        name = "Red Numbers",
        description = "Enables Scoreboard numbers",
        category = "SBBoxes",
        subcategory = "Scoreboard"
    )
    public static boolean redNumbers = true;

    @Dropdown(
        name = "Border Radius",
        description = "Border radius of the scoreboard",
        category = "SBBoxes",
        subcategory = "Scoreboard",
        options = {"0", "2", "4"}
    )
    public static int scoreboardBorderRadius = 1;

    // HTSL
    @Checkbox(
        name = "HTSL Enabled",
        category = "Housing",
        subcategory = "HTSL",
        description = "Enable HTSL. This will allow you to use the HTSL language."
    )
    public static boolean htslEnabled = true;

    @Checkbox(
        name = "Use Safemode",
        category = "Housing",
        subcategory = "HTSL",
        description = "Will show you where to click while loading in an action, this requires manual input and is no longer considered a \"macro\".\n\n&aSafeMode is recommended if you want to be extra careful not to break the rules."
    )
    public static boolean htslSafeMode = false;

    @Number(
        name = "Gui Cooldown",
        category = "Housing",
        subcategory = "HTSL",
        description = "Amount of cooldown between clicking an item in a GUI.\n\nvalues under 20 will result in more errors.",
        min = 0,
        max = 100
    )
    public static int guiCooldown = 20;

    @Number(
        name = "Gui Timeout",
        category = "Housing",
        subcategory = "HTSL",
        description = "Amount of ticks after not clicking anything in the GUI before declaring an error and timing out.\n\n&eIf you have lots of lagspikes / slow internet and HTSL keeps timing out you should increase this",
        min = 60,
        max = 200
    )
    public static int guiTimeout = 60;

    public static boolean wardrobeDarkMode = true;

    public transient ChatConfig chatConfig = new ChatConfig();
    public transient FormattingConfig formattingConfig = new FormattingConfig();
    public transient HousingConfig housingConfig = new HousingConfig();

    public HysentialsConfig() {
        super(new Mod("Hysentials", ModType.HYPIXEL), "hysentials.json");
        this.initialize();
        this.hideIf("openHytilsConfig", () -> !Hysentials.INSTANCE.isHytils);
        this.hideIf("installHytils", () -> Hysentials.INSTANCE.isHytils);
        this.registerKeyBind(keyBind, () -> {
            Minecraft.getMinecraft().thePlayer.closeScreen();
            Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(new CosmeticGui());
        });

//        addListener("fancyFormatting", () -> {
//            if (fancyFormatting) {
//                Minecraft.getMinecraft().fontRendererObj = new ImageIconRenderer();
//            } else {
//                Minecraft.getMinecraft().fontRendererObj = Hysentials.minecraftFont;
//            }
//        });
    }

    @Override
    public void openGui() {
        GuiUtils.displayScreen(new OneConfigGui(new HysentialsMods()));
    }
}
