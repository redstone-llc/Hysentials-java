package cc.woverflow.hysentials.config.hysentialMods;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.core.ConfigUtils;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;

import java.io.File;

public class HousingConfig extends Config {
    public HousingConfig() {
        //, "/assets/hysentials/housing.png", 244, 80
        super(new Mod("Housing", ModType.UTIL_QOL), "hysentials-housing.json");
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
