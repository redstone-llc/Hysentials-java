package llc.redstone.hysentials.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.ConfigUtils;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.gui.OneConfigGui;
import cc.polyfrost.oneconfig.gui.pages.ModConfigPage;
import cc.polyfrost.oneconfig.hud.Hud;
import cc.polyfrost.oneconfig.internal.hud.HudCore;
import cc.polyfrost.oneconfig.utils.gui.GuiUtils;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.guis.utils.SBBoxes;
import llc.redstone.hysentials.guis.utils.SBBoxesHud;
import net.minecraft.client.Minecraft;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class EditorConfig extends Config {
    public SBBoxes line;
    @Checkbox(
        name = "enabled",
        description = "Enable the line.",
        category = "editor",
        subcategory = "general"
    )
    public boolean enabled = true;

    @Text(
        name = "RegEx",
        description = "The RegEx to match the line. Don't edit this unless you know RegEx.",
        category = "editor",
        subcategory = "general",
        size = 1,
        multiline = true
    )
    public String regex = "";

    @Text(
        name = "display",
        description = "The text that will be displayed.",
        category = "editor",
        subcategory = "general",
        size = 1,
        multiline = true
    )
    public String display = "";

    @Text(
        name = "title",
        description = "Title of the scoreboard to match. Leave blank to match all.",
        category = "editor",
        subcategory = "general"
    )
    public String title = "";

    @Number(
        name = "x",
        description = "X position of the line.",
        category = "editor",
        subcategory = "position",
        min = 0,
        max = 5000
    )
    public int x = 0;

    @Number(
        name = "y",
        description = "Y position of the line.",
        category = "editor",
        subcategory = "position",
        min = 0,
        max = 5000
    )
    public int y = 0;

    @Slider(
        name = "scale",
        description = "Scale of the line.",
        category = "editor",
        subcategory = "position",
        min = 0,
        max = 5
    )
    public float scale = 1;

    @Button(
        name = "Delete",
        description = "Delete this line.",
        category = "editor",
        subcategory = "delete",
        text = "Delete"
    )
    public void delete() {
        HudCore.huds.remove(line.entry, line.hud);
        SBBoxes.boxes.remove(line);
        Minecraft.getMinecraft().thePlayer.closeScreen();
    }

    public EditorConfig(SBBoxes line) {
        super(new Mod("Hysentials", ModType.HYPIXEL), "editor");
        initialize();
        this.line = line;
        this.enabled = line.isEnabled();
        this.regex = line.getRegex();
        this.display = line.getRegexDisplay();
        this.title = line.getTitle();
        this.x = (int) line.position.getX();
        this.y = (int) line.position.getY();
        this.scale = (float) line.getScale();

        this.addListener("enabled", () -> {
            line.setEnabled(enabled);
        });
        this.addListener("regex", () -> {
            line.setRegex(regex);
        });
        this.addListener("display", () -> {
            line.setRegexDisplay(display);
        });
        this.addListener("title", () -> {
            line.setTitle(title);
        });
        this.addListener("x", () -> {
            line.setX(x);
        });
        this.addListener("y", () -> {
            line.setY(y);
        });
        this.addListener("scale", () -> {
            line.setScale(scale);
        });
    }

    public OneConfigGui openGuI() {
        if (mod == null) return null;
        OneConfigGui gui = new OneConfigGui(new ModConfigPage(mod.defaultPage));
        GuiUtils.displayScreen(gui);
        return gui;
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
    public void save() {

    }
}
