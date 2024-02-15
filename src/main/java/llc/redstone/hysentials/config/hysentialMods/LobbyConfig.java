package llc.redstone.hysentials.config.hysentialMods;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.ConfigUtils;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;

import java.io.File;

public class LobbyConfig extends Config {
    @Switch(
        name = "Housing Lag Reducer",
        category = "Lobby",
        subcategory = "General",
        description = "Will reduce the lag in the housing lobby, by hiding armorstands further than 20 blocks away from the player."
    )
    public static boolean housingLagReducer = true;

    public LobbyConfig() {
        super(new Mod("Lobby", ModType.UTIL_QOL, "/assets/hysentials/mods/lobby.png", 244, 80), "hysentials-lobby.json");
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
