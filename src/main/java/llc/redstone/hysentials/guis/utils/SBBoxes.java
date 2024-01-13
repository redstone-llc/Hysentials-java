package llc.redstone.hysentials.guis.utils;

import cc.polyfrost.oneconfig.config.core.OneColor;
import cc.polyfrost.oneconfig.hud.Hud;
import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack;
import cc.polyfrost.oneconfig.renderer.TextRenderer;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.handlers.sbb.SbbRenderer;
import llc.redstone.hysentials.util.Renderer;
import llc.redstone.hysentials.util.ScoreboardWrapper;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import kotlin.jvm.JvmName;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.handlers.sbb.SbbRenderer;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SBBoxes {
    public static List<SBBoxes> boxes = new ArrayList<>();
    private final transient JSONObject line;
    public transient Position position;
    private transient String display = "";
    @SerializedName("display")
    private String regexDisplay = "";
    @NotNull
    private String text = "";
    private String regex = "";
    private int x = 0;
    private int y = 0;
    private float scale = 0;
    private String title = "";
    private boolean enabled = false;

    public SBBoxes(@NotNull JSONObject line) {
        this.line = line;
        this.display = line.getString("display");
        this.regexDisplay = line.getString("display");
        this.regex = line.getString("regex");
        this.x = line.getInt("x");
        this.y = line.getInt("y");
        this.scale = line.getFloat("scale");
        this.title = line.getString("title");
        this.enabled = line.getBoolean("enabled");
        this.text = line.getString("text");

        position = new Position(x, y, getWidth(text), getHeight(text));
    }

    public void draw() {
        SbbRenderer.drawBox(position.getX(), position.getY(), getWidth(display), getHeight(display),
            HysentialsConfig.boxColor, HysentialsConfig.boxShadows,
            new Integer[]{0, 2, 4}[HysentialsConfig.scoreboardBoxesBorderRadius]
        );
        TextRenderer.drawScaledString(display, position.getX() + ((getWidth(display) - getStringWidth(display)) / 2), position.getY() + ((getHeight(display) - (9 * scale)) / 2), 0xFFFFFF, TextRenderer.TextType.NONE, scale);
    }

    private float getStringWidth(String s) {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(s) * scale;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public @NotNull String getText() {
        return text;
    }

    public String getDisplay() {
        return display;
    }

    public String getRegexDisplay() {
        return regexDisplay;
    }

    public void setText(@NotNull String text) {
        this.text = text;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public void setRegexDisplay(String regexDisplay) {
        this.regexDisplay = regexDisplay;
    }

    public int getX() {
        return (int) position.getX();
    }

    public void setX(int x) {
        this.position.setPosition(x, position.getY());
    }

    public int getY() {
        return (int) position.getY();
    }

    public void setY(int y) {
        this.position.setPosition(position.getX(), y);
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getWidth(String s) {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(s) * scale + 10 * scale;
    }

    public float getHeight(String s) {
        return 15 * scale;
    }

    public void setDisplay(String score, Matcher matcher) {
        if (regexDisplay == null) {
            regexDisplay = display;
        }
        display = regexDisplay;
        for (int i = 1; i <= matcher.groupCount(); i++) {
            if (matcher.group(i) != null) {
                display = display.replace("{$" + i + "}", matcher.group(i));
            }
        }
    }

    @NotNull
    public JSONObject getData() {
        return line;
    }

    public JSONObject save() {
        String json = new GsonBuilder().create().toJson(this, SBBoxes.class);
        JSONObject obj = new JSONObject(json);
        obj.put("x", position.getX());
        obj.put("y", position.getY());
        return obj;
    }

    public static SBBoxes getFromMatch(String s) {
        for (SBBoxes box : boxes) {
            Matcher matcher = Pattern.compile(box.regex).matcher(s);
            if (matcher.find()) {
                box.setDisplay(s, matcher);
                return box;
            }
        }
        return null;
    }
}
