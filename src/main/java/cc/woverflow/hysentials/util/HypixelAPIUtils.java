package cc.woverflow.hysentials.util;

import cc.polyfrost.oneconfig.events.event.LocrawEvent;
import cc.polyfrost.oneconfig.events.event.Stage;
import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.handlers.cache.HeightHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.EnumChatFormatting;
import org.json.JSONObject;
import sun.nio.ch.Net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class HypixelAPIUtils {
    public static boolean isBedwars = false;
    public static boolean isBridge = false;
    public static String gexp;
    public static String winstreak;
    public static String rank;
    public static LocrawInfo locraw;
    private int ticks = 0;

    private static String getCurrentESTTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        return simpleDateFormat.format(new Date(System.currentTimeMillis()));
    }

    public static void checkGameModes() {
        if (HypixelUtils.INSTANCE.isHypixel()) {
            ScoreObjective scoreboardObj = Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1);
            if (scoreboardObj != null) {
                String scObjName = cc.woverflow.hysentials.util.ScoreboardUtil.cleanSB(scoreboardObj.getDisplayName());
                if (scObjName.contains("BED WARS")) {
                    isBedwars = true;
                    isBridge = false;
                    return;
                } else if (scObjName.contains("DUELS")) {
                    for (String s : ScoreboardUtil.getSidebarLines()) {
                        if (s.toLowerCase(Locale.ENGLISH).contains("bridge ")) {
                            isBridge = true;
                            isBedwars = false;
                            return;
                        }
                    }
                }
            }
        }
        isBedwars = false;
        isBridge = false;
    }

    /**
     * Gets a UUID based on the username provided.
     *
     * @param username The username of the player to get.
     */
    public static String getUUID(String username) {
        JsonObject uuidResponse =
            NetworkUtils.getJsonElement("https://api.mojang.com/users/profiles/minecraft/" + username).getAsJsonObject();
        if (uuidResponse.has("error")) {
            Hysentials.INSTANCE.sendMessage(
                EnumChatFormatting.RED + "Failed with error: " + uuidResponse.get("reason").getAsString()
            );
            return null;
        }
        return uuidResponse.get("id").getAsString();
    }

    public static String getUsername(String uuid) {
        JsonObject uuidResponse =
            NetworkUtils.getJsonElement("https://api.mojang.com/user/profile/" + uuid).getAsJsonObject();
        if (uuidResponse.has("error")) {
            Hysentials.INSTANCE.sendMessage(
                EnumChatFormatting.RED + "Failed with error: " + uuidResponse.get("reason").getAsString()
            );
            return null;
        }
        return uuidResponse.get("name").getAsString();
    }

    public static UUID getUUIDpdb(String username) {
        JsonObject uuidResponse =
            NetworkUtils.getJsonElement("https://api.mojang.com/users/profiles/minecraft/" + username).getAsJsonObject();
        if (uuidResponse.has("error")) {
            Hysentials.INSTANCE.sendMessage(
                EnumChatFormatting.RED + "Failed with error: " + uuidResponse.get("reason").getAsString()
            );
            return null;
        }
        return UUID.fromString(uuidResponse.get("id").getAsString().replaceFirst(
            "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"
        ));
    }

    public static String getUUIDs(String username) throws IOException {
        URL url = new URL("https://api.namemc.com/profile/" + username + "/uuid");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject json = new JSONObject(response.toString());
        String uuid = json.get("id").toString();
        uuid = uuid.substring(0, 8) + "-" + uuid.substring(8, 12) + "-" + uuid.substring(12, 16) + "-" + uuid.substring(16, 20) + "-" + uuid.substring(20);
        return uuid;
    }

    public static boolean isValidKey(String key) {
        try {
            JsonObject gotten = NetworkUtils.getJsonElement("https://api.hypixel.net/key?key=" + key).getAsJsonObject();
            return gotten.has("success") && gotten.get("success").getAsBoolean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public enum Ranks {
        OWNER("&c[OWNER]"),
        ADMIN("&c[ADMIN]"),
        MOD("&2[MOD]"),
        YOUTUBER("&c[&fYOUTUBE&c]"),
        MVP_PLUS_PLUS("&6[MVP++]"),
        MVP_PLUS("&b[MVP+]"),
        MVP("&b[MVP]"),
        VIP_PLUS("&a[VIP+]"),
        VIP("&a[VIP]"),
        DEFAULT("&7");

        private final String prefix;

        Ranks(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    public enum Colors {
        BLACK("&0"),
        DARK_BLUE("&1"),
        DARK_GREEN("&2"),
        DARK_AQUA("&3"),
        DARK_RED("&4"),
        DARK_PURPLE("&5"),
        GOLD("&6"),
        GRAY("&7"),
        DARK_GRAY("&8"),
        BLUE("&9"),
        GREEN("&a"),
        AQUA("&b"),
        RED("&c"),
        LIGHT_PURPLE("&d"),
        YELLOW("&e"),
        WHITE("&f");

        private final String color;

        Colors(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }
    }

    @Subscribe
    public void onTick(TickEvent event) {
        if (event.stage == Stage.START) {
            if (ticks % 20 == 0) {
                if (Minecraft.getMinecraft().thePlayer != null) {
                    checkGameModes();
                    HeightHandler.INSTANCE.getHeight();
                }
                ticks = 0;
            }

            ticks++;
        }
    }

    @Subscribe
    private void onLocraw(LocrawEvent event) {
        locraw = event.info;
    }
}
