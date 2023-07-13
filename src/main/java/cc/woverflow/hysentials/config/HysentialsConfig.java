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
import cc.polyfrost.oneconfig.libs.checker.units.qual.N;
import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.cosmetic.CosmeticGui;
import cc.woverflow.hytils.HytilsReborn;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.File;

public class HysentialsConfig extends Config {
    // GENERAL
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

    @KeyBind(
        name = "Open Cosmetics",
        category = "General",
        subcategory = "Keybinds",
        description = "The keybind to open the cosmetics menu."
    )
    public static OneKeyBind keyBind = new OneKeyBind(UKeyboard.KEY_K);



    @Switch(
        name = "Global Chat Enabled",
        category = "General",
        subcategory = "Chat",
        description = "Enable global chat. This will allow you to chat with other players who are using Hysentials."
    )
    public static boolean globalChatEnabled = true;

    @Switch(
        name = "Futuristic Ranks",
        category = "General",
        subcategory = "Ranks",
        description = "Enable futuristic ranks. This will allow you to see an image as a users rank."
    )
    public static boolean futuristicRanks = true;

    @Switch(
        name = "Futuristic Channels",
        category = "General",
        subcategory = "Chat",
        description = "Enable futuristic channels. This will allow you to see an image as a chat channel"
    )
    public static boolean futuristicChannels = true;

    @Switch(
        name = "Chat Limit 256",
        category = "General",
        subcategory = "Chat",
        description = "Enable chat limit 256. This will allow you to send messages up to 256 characters long. (This will only work on Hypixel)"
    )
    public static boolean chatLimit256 = true;

    @Button(
        name = "Rank Image Config",
        category = "General",
        subcategory = "Ranks",
        description = "Opens the rank image config directory.",
        text = "Open Folder")
    public void openRankImageConfig() {
        Desktop desktop = Desktop.getDesktop();
        File directory = new File("./config/hysentials/imageicons");
        try {
            desktop.open(directory);
        } catch (Exception e) {
            e.printStackTrace();
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


//    // PETS
//    @Button(
//        name = "Hamster Pet",
//        category = "Pets",
//        subcategory = "Hamster",
//        description = "Will enable the hamster pet, if you have it unlocked.",
//        text = "DISABLED"
//    )
//    public void hamsterEnabled() {
//
//    }
//
//    @Button(
//        name = "Cubit Pet",
//        category = "Pets",
//        subcategory = "Cubit",
//        description = "Will enable the cubit pet, if you have it unlocked. Only available for \"Special\" Plus Players.",
//        text = "DISABLED"
//    )
//    public void cubitEnabled() {
//    }
    @Switch(
        name = "Show Pets in game",
        category = "Pets",
        subcategory = "General",
        description = "Will allow pets to be shown in game."
    )
    public static boolean showPets = false;

    // LOBBY

    @Switch(
        name = "Housing Lag Reducer",
        category= "Lobby",
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
        category = "HTSL",
        subcategory = "General",
        description = "Enable HTSL. This will allow you to use the HTSL language."
    )
    public static boolean htslEnabled = true;

    @Checkbox(
        name = "Use Safemode",
        category = "HTSL",
        subcategory = "General",
        description = "Will show you where to click while loading in an action, this requires manual input and is no longer considered a \"macro\".\n\n&aSafeMode is recommended if you want to be extra careful not to break the rules."
    )
    public static boolean htslSafeMode = false;

    @Number(
        name = "Gui Cooldown",
        category = "HTSL",
        subcategory = "Miscellaneous",
        description = "Amount of cooldown between clicking an item in a GUI.\n\nvalues under 20 will result in more errors.",
        min = 0,
        max = 100
    )
    public static int guiCooldown = 20;

    @Number(
        name = "Gui Timeout",
        category = "HTSL",
        subcategory = "Miscellaneous",
        description = "Amount of ticks after not clicking anything in the GUI before declaring an error and timing out.\n\n&eIf you have lots of lagspikes / slow internet and HTSL keeps timing out you should increase this",
        min = 60,
        max = 200
    )
    public static int guiTimeout = 60;


    public HysentialsConfig() {
        super(new Mod("Hysentials", ModType.HYPIXEL), "hysentials.json");
        this.initialize();
        this.hideIf("openHytilsConfig", () -> !Hysentials.INSTANCE.isHytils);
        this.registerKeyBind(keyBind, () -> {
            Minecraft.getMinecraft().thePlayer.closeScreen();
            Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(new CosmeticGui());
        });
    }
}
