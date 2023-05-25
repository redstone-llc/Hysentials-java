/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
import cc.woverflow.hytils.config.HytilsConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.EnumChatFormatting;
import org.json.JSONObject;

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
     * Gets the player's GEXP and stores it in a variable.
     *
     * @return Whether the "getting" was successful.
     */
    public static boolean getGEXP() {
        String gexp = null;
        String uuid = Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString().replace("-", "");
        JsonArray guildMembers = NetworkUtils.getJsonElement("https://api.hypixel.net/guild?key=" + HytilsConfig.apiKey + ";player=" + uuid).getAsJsonObject().getAsJsonObject("guild").getAsJsonArray("members");
        for (JsonElement e : guildMembers) {
            if (e.getAsJsonObject().get("uuid").getAsString().equals(uuid)) {
                gexp = Integer.toString(e.getAsJsonObject().getAsJsonObject("expHistory").get(getCurrentESTTime()).getAsInt());
                break;
            }
        }
        if (gexp == null) return false;
        HypixelAPIUtils.gexp = gexp;
        return true;
    }

    /**
     * Gets the specified player's GEXP and stores it in a variable.
     *
     * @param username The username of the player.
     * @return Whether the "getting" was successful.
     */
    public static boolean getGEXP(String username) {
        String gexp = null;
        String uuid = getUUID(username);
        JsonArray guildMembers = NetworkUtils.getJsonElement("https://api.hypixel.net/guild?key=" + HytilsConfig.apiKey + ";player=" + uuid).getAsJsonObject().getAsJsonObject("guild").getAsJsonArray("members");
        for (JsonElement e : guildMembers) {
            if (e.getAsJsonObject().get("uuid").getAsString().equals(uuid)) {
                gexp = Integer.toString(e.getAsJsonObject().getAsJsonObject("expHistory").get(getCurrentESTTime()).getAsInt());
                break;
            }
        }
        if (gexp == null) return false;
        HypixelAPIUtils.gexp = gexp;
        return true;
    }

    /**
     * Gets the player's weekly GEXP and stores it in a variable.
     *
     * @return Whether the "getting" was successful.
     */
    public static boolean getWeeklyGEXP() {
        String gexp = null;
        String uuid = Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString().replace("-", "");
        JsonArray guildMembers = NetworkUtils.getJsonElement("https://api.hypixel.net/guild?key=" + HytilsConfig.apiKey + ";player=" + uuid).getAsJsonObject().getAsJsonObject("guild").getAsJsonArray("members");
        for (JsonElement e : guildMembers) {
            if (e.getAsJsonObject().get("uuid").getAsString().equals(uuid)) {
                int addGEXP = 0;
                for (Map.Entry<String, JsonElement> set : e.getAsJsonObject().get("expHistory").getAsJsonObject().entrySet()) {
                    addGEXP += set.getValue().getAsInt();
                }
                gexp = Integer.toString(addGEXP);
                break;
            }
        }
        if (gexp == null) return false;
        HypixelAPIUtils.gexp = gexp;
        return true;
    }

    /**
     * Gets the player's weekly GEXP and stores it in a variable.
     *
     * @param username The username of the player.
     * @return Whether the "getting" was successful.
     */
    public static boolean getWeeklyGEXP(String username) {
        String gexp = null;
        String uuid = getUUID(username);
        JsonArray guildMembers = NetworkUtils.getJsonElement("https://api.hypixel.net/guild?key=" + HytilsConfig.apiKey + ";player=" + uuid).getAsJsonObject().getAsJsonObject("guild").getAsJsonArray("members");
        for (JsonElement e : guildMembers) {
            if (e.getAsJsonObject().get("uuid").getAsString().equals(uuid)) {
                int addGEXP = 0;
                for (Map.Entry<String, JsonElement> set : e.getAsJsonObject().get("expHistory").getAsJsonObject().entrySet()) {
                    addGEXP += set.getValue().getAsInt();
                }
                gexp = Integer.toString(addGEXP);
                break;
            }
        }
        if (gexp == null) return false;
        HypixelAPIUtils.gexp = gexp;
        return true;
    }

    /**
     * Gets the player's current winstreak and stores it in a variable.
     *
     * @return Whether the "getting" was successful.
     */
    public static boolean getWinstreak() {
        String uuid = Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString().replace("-", "");
        JsonObject playerStats =
            NetworkUtils.getJsonElement("https://api.hypixel.net/player?key=" + HytilsConfig.apiKey + ";uuid=" + uuid).getAsJsonObject().getAsJsonObject("player").getAsJsonObject("stats");
        if (locraw != null) {
            switch (locraw.getGameType()) {
                case BEDWARS:
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("Bedwars").get("winstreak").getAsInt());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SKYWARS:
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("SkyWars").get("win_streak").getAsInt());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case DUELS:
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("Duels").get("current_winstreak").getAsInt());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        return false;
    }

    /**
     * Gets the specified player's current winstreak and stores it in a variable.
     *
     * @param username The username of the player.
     * @return Whether the "getting" was successful.
     */
    public static boolean getWinstreak(String username) {
        String uuid = getUUID(username);
        JsonObject playerStats =
            NetworkUtils.getJsonElement("https://api.hypixel.net/player?key=" + HytilsConfig.apiKey + ";uuid=" + uuid).getAsJsonObject().getAsJsonObject("player").getAsJsonObject("stats");
        if (locraw != null) {
            switch (locraw.getGameType()) {
                case BEDWARS:
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("Bedwars").get("winstreak").getAsInt());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SKYWARS:
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("SkyWars").get("win_streak").getAsInt());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case DUELS:
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("Duels").get("current_winstreak").getAsInt());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        return false;
    }

    /**
     * Gets the player's current winstreak and stores it in a variable.
     *
     * @param username The username of the player.
     * @param game     The game to get the stats.
     * @return Whether the "getting" was successful.
     */
    public static boolean getWinstreak(String username, String game) {
        String uuid = getUUID(username);
        JsonObject playerStats =
            NetworkUtils.getJsonElement("https://api.hypixel.net/player?key=" + HytilsConfig.apiKey + ";uuid=" + uuid).getAsJsonObject().getAsJsonObject("player").getAsJsonObject("stats");
        if (game != null) {
            switch (game.toLowerCase(Locale.ENGLISH)) {
                case "bedwars":
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("Bedwars").get("winstreak").getAsInt());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "skywars":
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("SkyWars").get("win_streak").getAsInt());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "duels":
                    try {
                        winstreak = Integer.toString(playerStats.getAsJsonObject("Duels").get("current_winstreak").getAsInt());
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        return false;
    }

    /**
     * Gets the Hypixel rank of the specified player.
     *
     * @param username The username of the player.
     * @return Player rank
     */
    public static String getRank(String username) {
        if (!HytilsConfig.apiKey.isEmpty() && HypixelAPIUtils.isValidKey(HytilsConfig.apiKey)) {
            String uuid = getUUID(username);
            try {
                JsonObject playerRank =
                    NetworkUtils.getJsonElement("https://api.hypixel.net/player?key=" + HytilsConfig.apiKey + ";uuid=" + uuid).getAsJsonObject().getAsJsonObject("player");
                rank = playerRank.get("newPackageRank").toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rank;
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

    public static UUID getUUIDpdb(String username) {
        JsonObject uuidResponse =
            NetworkUtils.getJsonElement("https://api.mojang.com/users/profiles/minecraft/" + username).getAsJsonObject();
        if (uuidResponse.has("error")) {
            Hysentials.INSTANCE.sendMessage(
                EnumChatFormatting.RED + "Failed with error: " + uuidResponse.get("reason").getAsString()
            );
            return null;
        }
        return UUID.fromString(uuidResponse.get("id").getAsString());
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

    public static String getDisplayName(String username, String uuid) {
        String displayName = username;
        if (uuid == null) return displayName;
        try {
            UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            return displayName;
        }
        JsonElement request = NetworkUtils.getJsonElement("https://api.hypixel.net/player?key=" + HytilsConfig.apiKey + "&uuid=" + uuid.replace("-", ""));
        if (request.isJsonNull()) return displayName;
        if (request.getAsJsonObject().get("player").isJsonNull()) return displayName;
        JsonObject player = request.getAsJsonObject().get("player").getAsJsonObject();
        if (player.get("newPackageRank").isJsonNull()) return "&7" + player.get("displayname").getAsString();
        String rank = player.get("newPackageRank").getAsString();
        boolean isSuperStar = player.get("monthlyPackageRank").getAsString().equals("SUPERSTAR");
        if (rank.equals("MVP_PLUS") && isSuperStar) {
            displayName = String.format("&6[MVP%s++&6] ", HypixelAPIUtils.Colors.valueOf(player.get("rankPlusColor").getAsString()).getColor()) + player.get("displayname").getAsString();
        } else {
            if (rank.equals("MVP_PLUS")) {
                displayName = String.format("&b[MVP%s+&6] ", HypixelAPIUtils.Colors.valueOf(player.get("rankPlusColor").getAsString()).getColor()) + player.get("displayname").getAsString();
            } else {
                displayName = String.format("%s %s", HypixelAPIUtils.Ranks.valueOf(rank).getPrefix(), player.get("displayname").getAsString());
            }
        }

        for (Map.Entry<UUID, String> entry : Hysentials.INSTANCE.getOnlineCache().rankCache.entrySet()) {
            UUID p = entry.getKey();
            String r = entry.getValue();
            if (p.toString().equals(uuid)) {
                switch (r.toLowerCase()) {
                    case "admin": {
                        displayName = "&c[ADMIN] " + player.get("displayname").getAsString();
                        break;
                    }
                    case "mod": {
                        displayName = "&9[MOD] " + player.get("displayname").getAsString();
                        break;
                    }
                    case "creator": {
                        displayName = "&3[&fCREATOR&3] " + player.get("displayname").getAsString();
                        break;
                    }
                    case "plus": {
                        displayName += " &6[+]";
                        break;
                    }
                }
            }
        }
        return displayName;
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
