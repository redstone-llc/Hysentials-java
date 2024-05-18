package llc.redstone.hysentials.config.hysentialmods;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.ConfigUtils;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;

import java.io.File;

public class CosmeticConfig extends Config {
    // Cosmetic stuff
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

    @Switch(
        name = "Wind Effect",
        category = "Comsetics",
        subcategory = "Capes",
        description = "Will give the capes a windy effect making it look like they are blowing in the wind."
    )
    public static boolean windEffect = true;

    @Dropdown(
        name = "Cape Animation",
        category = "Comsetics",
        subcategory = "Capes",
        description = "Which animation the capes should render with.",
        options = {"Blocky Animation", "Silky Animation"}
    )
    public static int blockyCapes = 0;

    public CosmeticConfig() {
        super(new Mod("Cosmetics", ModType.UTIL_QOL, "/assets/hysentials/mods/cosmetics.png", 244, 80), "hysentials-cosmetic.json");
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
