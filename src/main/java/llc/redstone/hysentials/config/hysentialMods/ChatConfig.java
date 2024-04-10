package llc.redstone.hysentials.config.hysentialMods;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.annotations.Text;
import cc.polyfrost.oneconfig.config.core.ConfigUtils;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.gui.OneConfigGui;
import cc.polyfrost.oneconfig.gui.pages.ModConfigPage;
import cc.polyfrost.oneconfig.utils.gui.GuiUtils;

import java.io.File;

public class ChatConfig extends Config {
    @Switch(
        name = "Chat Limit 256",
        category = "General",
        subcategory = "General",
        description = "Enable chat limit 256. This will allow you to send messages up to 256 characters long. (This will only work on supported servers)",
        size = 0
    )
    public static boolean chatLimit256 = true;

    @Switch(
        name = "Level Prefix Colors",
        description = "Enable/Disable level prefix colors.",
        category = "General",
        subcategory = "General"
    )
    public static boolean levelPrefixColors = true;

    @Color(
        name = "Default Level Color",
        description = "The default color for level prefixes.",
        category = "General",
        subcategory = "General",
        allowAlpha = false
    )
    public static OneColor defaultLevelColor = new OneColor("#FFFFFF");

    @Switch(
            name = "Party Formatting",
            description = "The prefix for all chat messages.",
            category = "Party",
            subcategory = "General"
    )
    public static boolean partyFormatting = true;

    @Text(
        name = "Party Prefix",
        description = "The prefix for all party related chat messages.",
        category = "Party",
        subcategory = "General",
        placeholder = ":partyprefix: "
    )
    public static String partyPrefix = ":partyprefix: ";

    @Switch(
        name = "Hide Lines",
        description = "Hide the lines between chat messages.",
        category = "Party",
        subcategory = "General"
    )
    public static boolean hideLines = true;

    @Switch(
        name = "Player Party Chat Prefix",
        description = "Enable/Disable player prefixes in party chat.",
        category = "Party",
        subcategory = "General"
    )
    public static boolean partyChatPrefix = true;

    @Switch(
        name = "Guild Formatting",
        description = "Enable/Disable guild formatting.",
        category = "Guild",
        subcategory = "General"
    )
    public static boolean guildFormatting = true;

    @Text(
        name = "Guild Prefix",
        description = "The prefix for all guild related chat messages.",
        category = "Guild",
        subcategory = "General",
        placeholder = ":guildprefix: "
    )
    public static String guildPrefix = ":guildprefix: ";

    @Switch(
        name = "Player Guild Chat Prefix",
        description = "Enable/Disable player prefixes in guild chat.",
        category = "Guild",
        subcategory = "General"
    )
    public static boolean guildChatPrefix = false;

    @Switch(
        name = "Guild Chat Suffix",
        description = "Enable/Disable guild suffixes in guild chat.",
        category = "Guild",
        subcategory = "General"
    )
    public static boolean guildChatSuffix = true;

    @Switch(
        name = "Global Chat",
        description = "Enable/Disable global chat.",
        category = "Global Chat",
        subcategory = "General"
    )
    public static boolean globalChat = true;


    @Switch(
        name = "Player Global Chat Suffix",
        description = "Enable/Disable player suffixes in global chat.",
        category = "Global Chat",
        subcategory = "General"
    )
    public static boolean globalChatSuffix = true;

    @Text(
        name = "Global Chat Prefix",
        description = "The prefix for all global chat messages.",
        category = "Global Chat",
        subcategory = "General",
        placeholder = ":globalchat: "
    )
    public static String globalPrefix = ":globalchat: ";

    public ChatConfig() {
        super(new Mod("Chat", ModType.UTIL_QOL, "/assets/hysentials/mods/chat.png", 244, 80), "hysentials-chat.json");
        initialize();
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
}
