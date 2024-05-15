package llc.redstone.hysentials.config.hysentialMods;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.ConfigUtils;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;

import java.io.File;

public class HousingConfig extends Config {
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

    @Switch(
        name = "Export Color Codes",
        category = "Housing",
        subcategory = "HTSL",
        description = "Export color codes in the HTSL language."
    )
    public static boolean exportColorCodes = true;

    // Housing
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

    public HousingConfig() {
        super(new Mod("Housing", ModType.UTIL_QOL, "/assets/hysentials/mods/housing.png", 244, 80), "hysentials-housing.json");
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
