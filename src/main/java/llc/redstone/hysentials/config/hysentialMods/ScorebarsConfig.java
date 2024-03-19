package llc.redstone.hysentials.config.hysentialMods;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Button;
import cc.polyfrost.oneconfig.config.annotations.Checkbox;
import cc.polyfrost.oneconfig.config.annotations.Color;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.core.ConfigUtils;
import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import llc.redstone.hysentials.guis.sbBoxes.SBBoxesEditor;
import net.minecraft.client.Minecraft;

import java.io.File;

public class ScorebarsConfig extends Config {
    @Button(
        name = "Open Editor",
        text = "Open Editor",
        description = "Open the editor.",
        category = "SBBoxes",
        subcategory = "General"
    )
    public void openEditor() {
        Minecraft.getMinecraft().thePlayer.closeScreen();
        new SBBoxesEditor().show();
    }

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

    public ScorebarsConfig() {
        super(new Mod("Scorebars", ModType.UTIL_QOL, "/assets/hysentials/mods/scorebars.png", 244, 80), "hysentials-scorebars.json");
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
