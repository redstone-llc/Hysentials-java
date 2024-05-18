package llc.redstone.hysentials.guis.utils;

import cc.polyfrost.oneconfig.internal.hud.HudCore;
import cc.polyfrost.oneconfig.renderer.TextRenderer;
import llc.redstone.hysentials.config.hysentialmods.ScorebarsConfig;
import llc.redstone.hysentials.guis.sbBoxes.SBBoxesEditor;
import llc.redstone.hysentials.handlers.sbb.SbbRenderer;
import llc.redstone.hysentials.util.C;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import llc.redstone.hysentials.util.ScoreboardWrapper;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.beans.Transient;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

public class SBBoxes {
    public static List<SBBoxes> boxes = new ArrayList<>();
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
    transient public SBBoxesHud hud;
    transient public Map.Entry<Field, Object> entry;

    public SBBoxes(@NotNull JSONObject line) {
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

        hud = new SBBoxesHud(this);

        HudCore.huds.put(entry = new Map.Entry<Field, Object>() {
            @Override
            public Field getKey() {
                return null;
            }

            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public Object setValue(Object o) {
                return null;
            }
        }, hud);

    }

    public void draw() {
        if (display.startsWith(" ") && !display.endsWith(" ")) {
            display = display.trim();
        }
        display = C.translate(display);
        SbbRenderer.drawBox(position.getX(), position.getY(), getWidth(display), getHeight(display),
            ScorebarsConfig.boxColor, ScorebarsConfig.boxShadows,
            new Integer[]{0, 2, 4}[ScorebarsConfig.scoreboardBoxesBorderRadius]
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

    @Override
    public String toString() {
        return "SBBoxes{" +
            "position=" + position +
            ", display='" + display + '\'' +
            ", regexDisplay='" + regexDisplay + '\'' +
            ", text='" + text + '\'' +
            ", regex='" + regex + '\'' +
            ", x=" + x +
            ", y=" + y +
            ", scale=" + scale +
            ", title='" + title + '\'' +
            ", enabled=" + enabled +
            '}';
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

    public void setDisplay(String text) {
        Matcher matcher = Pattern.compile(regex).matcher(text);
        if (matcher.find()) {
            setDisplay(text, matcher);
        }
    }


    public JSONObject save() {
        String json = new GsonBuilder().create().toJson(this, SBBoxes.class);
        JSONObject obj = new JSONObject(json);
        obj.put("x", position.getX());
        obj.put("y", position.getY());
        return obj;
    }

    public boolean canRender() {
        ArrayList<ScoreboardWrapper.ScoreWrapper> ls = ScoreboardWrapper.getScoreboard().getSortedScores(ScoreboardWrapper.getSidebar()).stream().map(ScoreboardWrapper.ScoreWrapper::new).collect(Collectors.toCollection(ArrayList::new));

        for (ScoreboardWrapper.ScoreWrapper l : ls) {
            if (doesMatch(l.getName())) {
                setDisplay(l.getName());
                return true;
            }
        }
        return false;
    }

    public boolean doesMatch(String s) {
        s = C.removeRepeatColor(s);
        s = s.replace("§", "&");
        s = SBBoxesEditor.removeHiddenCharacters(s);
        Matcher matcher = Pattern.compile(regex).matcher(s);
        if (matcher.find()) {
            setDisplay(s, matcher);
            return true;
        }
        return false;
    }

    public static SBBoxes getFromMatch(String s) {
        s = C.removeRepeatColor(s);
        s = s.replace("§", "&");
        s = SBBoxesEditor.removeHiddenCharacters(s);
        for (SBBoxes box : boxes) {
            try {
                Matcher matcher = Pattern.compile(box.regex).matcher(s);
                if (matcher.find()) {
                    box.setDisplay(s, matcher);
                    return box;
                }
            } catch (PatternSyntaxException ignored) { // Bad regex pattern
            }
        }
        return null;
    }
}
