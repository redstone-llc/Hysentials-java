package llc.redstone.hysentials.config.hysentialMods;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.CustomOption;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
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
import llc.redstone.hysentials.config.hysentialMods.rank.RankAnnotation;
import llc.redstone.hysentials.config.hysentialMods.rank.RankOption;
import llc.redstone.hysentials.handlers.imageicons.ImageIcon;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class IconsConfig extends Config {
    @Info(
        text = "The directory of the icons must be in <config>/hysentials/imageicons/ and must be a .png file.",
        type = InfoType.WARNING,
        category = "General",
        subcategory = "Hysentials",
        size = 0
    )
    public String info = null;
    @IconsAnnotation(
        name = "Icons",
        category = "General",
        subcategory = "Hysentials"
    )
    public List<IconStuff> icons = getIcons();

    //, "/assets/hysentials/mods/cosmetics.png", 244, 80
    public IconsConfig() {
        super(new Mod("Icons", ModType.UTIL_QOL), "hysentials-icons.json", true, false);
        initialize();

        for (IconStuff icon : getIcons()) {
            if (icons.stream().anyMatch(iconStuff -> iconStuff.name.equals(icon.name))) continue;
            icons.add(icon);
        }
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

    public List<IconStuff> getIcons() {
        return ImageIcon.imageIcons.values().stream().filter(imageIcon -> !imageIcon.emoji).map(
            icon -> new IconStuff(icon.getName(), "./config/hysentials/imageicons/" + icon.getName() + ".png")
        ).collect(Collectors.toList());
    }

    @Override
    protected BasicOption getCustomOption(Field field, CustomOption annotation, OptionPage page, Mod mod, boolean migrate) {
        BasicOption option = null;
        switch (annotation.id()) {
            case "icons":
                IconsAnnotation myOption =  ConfigUtils.findAnnotation(field, IconsAnnotation.class);
                option = IconsOption.create(field, this);
                ConfigUtils.getSubCategory(page, myOption.category(), myOption.subcategory()).options.add(option);
                break;
        }
        return option;
    }
}
