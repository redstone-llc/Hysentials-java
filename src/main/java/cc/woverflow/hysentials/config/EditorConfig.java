package cc.woverflow.hysentials.config;

import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Number;
import cc.polyfrost.oneconfig.config.annotations.*;
import cc.polyfrost.oneconfig.config.core.ConfigUtils;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.gui.OneConfigGui;
import cc.polyfrost.oneconfig.gui.pages.ModConfigPage;
import cc.polyfrost.oneconfig.utils.gui.GuiUtils;
import cc.woverflow.hysentials.Hysentials;
import net.minecraft.client.Minecraft;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class EditorConfig extends Config {
    public JSONObject line;
    @Checkbox(
        name = "enabled",
        description = "Enable the line",
        category = "editor",
        subcategory = "general"
    )
    public boolean enabled = true;

    @Text(
        name = "regex",
        description = "The regex to match the line. Don't edit this unless you know regex",
        category = "editor",
        subcategory = "general",
        size = 1
    )
    public String regex = "";

    @Text(
        name = "display",
        description = "The text that will be displayed",
        category = "editor",
        subcategory = "general"
    )
    public String display = "";

    @Text(
        name = "title",
        description = "Title of the scoreboard to match. Leave blank to match all",
        category = "editor",
        subcategory = "general"
    )
    public String title = "";

    @Number(
        name = "x",
        description = "X position of the line",
        category = "editor",
        subcategory = "position",
        min = 0,
        max = 5000
    )
    public int x = 0;

    @Number(
        name = "y",
        description = "Y position of the line",
        category = "editor",
        subcategory = "position",
        min = 0,
        max = 5000
    )
    public int y = 0;

    @Slider(
        name = "scale",
        description = "Scale of the line",
        category = "editor",
        subcategory = "position",
        min = 0,
        max = 5
    )
    public float scale = 1;

    @Button(
        name = "Delete",
        description = "Delete this line",
        category = "editor",
        subcategory = "delete",
        text = "Delete"
    )
    public void delete() {
        JSONArray lines = Hysentials.INSTANCE.sbBoxes.jsonObject.getJSONArray("lines");
        int i = lines.toList().indexOf(line.toMap());
        lines.remove(i);
        Minecraft.getMinecraft().thePlayer.closeScreen();
    }

    public EditorConfig(JSONObject line) {
        super(new Mod("Hysentials", ModType.HYPIXEL), "editor");
        initialize();
        this.line = line;
        this.enabled = line.getBoolean("enabled");
        this.regex = line.getString("regex");
        this.display = line.getString("display");
        this.title = line.getString("title");
        this.x = line.getInt("x");
        this.y = line.getInt("y");
        this.scale = (float) line.getDouble("scale");

        this.addListener("enabled", () -> {
            line.put("enabled", enabled);
        });
        this.addListener("regex", () -> {
            line.put("regex", regex);
        });
        this.addListener("display", () -> {
            line.put("display", display);
        });
        this.addListener("title", () -> {
            line.put("title", title);
        });
        this.addListener("x", () -> {
            line.put("x", x);
        });
        this.addListener("y", () -> {
            line.put("y", y);
        });
        this.addListener("scale", () -> {
            line.put("scale", scale);
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
