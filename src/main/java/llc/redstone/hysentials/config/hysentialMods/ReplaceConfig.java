package llc.redstone.hysentials.config.hysentialMods;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.CustomOption;
import cc.polyfrost.oneconfig.config.annotations.Info;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.core.ConfigUtils;
import cc.polyfrost.oneconfig.config.data.InfoType;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.elements.BasicOption;
import cc.polyfrost.oneconfig.config.elements.OptionPage;
import llc.redstone.hysentials.config.hysentialMods.icons.IconStuff;
import llc.redstone.hysentials.config.hysentialMods.icons.IconsAnnotation;
import llc.redstone.hysentials.config.hysentialMods.icons.IconsOption;
import llc.redstone.hysentials.config.hysentialMods.replace.ReplaceAnnotation;
import llc.redstone.hysentials.config.hysentialMods.replace.ReplaceOption;
import llc.redstone.hysentials.config.hysentialMods.replace.ReplaceStuff;
import llc.redstone.hysentials.handlers.imageicons.ImageIcon;
import llc.redstone.hysentials.util.BlockWAPIUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class ReplaceConfig extends Config {
    @Switch(
        name = "Enable Regex Replacement",
        description = "Enable regex replacement",
        category = "Global",
        subcategory = "Global",
        size = 0
    )
    public boolean enableRegex = false;

    @ReplaceAnnotation(
        name = "Global Replace",
        description = "Replace things globally",
        category = "Global",
        subcategory = "Global"
    )
    public List<ReplaceStuff> globalReplacements = new ArrayList<>(Collections.singletonList(
            new ReplaceStuff("Global")
    ));

    @ReplaceAnnotation(
        name = "Club Replace",
        description = "Replace things in clubs",
        category = "Clubs",
        subcategory = ""
    )
    public List<ReplaceStuff> clubReplacements = new ArrayList<>();

    //, "/assets/hysentials/mods/cosmetics.png", 244, 80
    public ReplaceConfig() {
        super(new Mod("Replace", ModType.UTIL_QOL), "hysentials-replace.json", true, false);
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

    @Override
    protected BasicOption getCustomOption(Field field, CustomOption annotation, OptionPage page, Mod mod, boolean migrate) {
        BasicOption option = null;
        switch (annotation.id()) {
            case "replace":
                ReplaceAnnotation myOption =  ConfigUtils.findAnnotation(field, ReplaceAnnotation.class);
                option = ReplaceOption.create(field, this);
                ConfigUtils.getSubCategory(page, myOption.category(), myOption.subcategory()).options.add(option);
                break;
        }
        return option;
    }

    public HashMap<String, String> getAllActiveReplacements() {
        HashMap<String, String> replacements = new HashMap<>();
        for (ReplaceStuff replaceStuff : globalReplacements) {
            replacements.putAll(replaceStuff.replacements);
        }
        if (BlockWAPIUtils.currentHousingsClub != null) {
            clubReplacements.stream().filter(replaceStuff -> replaceStuff.clubName.equals(BlockWAPIUtils.currentHousingsClub.getName())).findFirst().ifPresent(clubReplace -> {
                replacements.putAll(clubReplace.replacements);
            });
        }
        return replacements;
    }

    public boolean isRegexEnabled() {
        boolean regex = enableRegex;
        if (BlockWAPIUtils.currentHousingsClub != null) {
            regex = BlockWAPIUtils.currentHousingsClub.getRegex();
        }
        return regex;
    }
}
