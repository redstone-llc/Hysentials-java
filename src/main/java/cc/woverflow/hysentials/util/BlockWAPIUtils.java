package cc.woverflow.hysentials.util;

import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.handlers.imageicons.ImageIcon;
import cc.woverflow.hysentials.websocket.Socket;
import com.google.gson.*;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.Async;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONPointer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static cc.woverflow.hysentials.guis.actionLibrary.ActionViewer.toList;
import static cc.woverflow.hysentials.handlers.chat.modules.bwranks.BWSReplace.diagnostics;

public class BlockWAPIUtils {
    public static JSONArray actions;
    public static JSONArray cosmetics;

    @NotNull
    public static HashMap<UUID, String> getOnline() {
        diagnostics.add("Starting cache refresh");
        long start = System.currentTimeMillis();
        try {
            JsonElement online = null;
            JsonElement ranks = null;
            JsonElement cosmetics = null;
            try {
                online = NetworkUtils.getJsonElement("http://127.0.0.1:8080/api/online");
                ranks = NetworkUtils.getJsonElement("http://127.0.0.1:8080/api/ranks");
                cosmetics = NetworkUtils.getJsonElement("http://127.0.0.1:8080/api/cosmetic");
                String a = NetworkUtils.getString("https://hysentials.redstone.llc/api/actions");
                JSONObject json = new JSONObject(a);
                actions = json.getJSONArray("actions");
                BlockWAPIUtils.cosmetics = new JSONObject(cosmetics.toString()).getJSONArray("cosmetics");
            } catch (Exception ignored) {
            }
            diagnostics.add("API calls took " + (System.currentTimeMillis() - start) + "ms");
            if (online == null || online.isJsonNull()) return new HashMap<>();
            if (ranks == null || ranks.isJsonNull()) return new HashMap<>();
            diagnostics.add("API calls were not null");
            diagnostics.add("Starting to parse data");
            long start2 = System.currentTimeMillis();
            JsonArray users = online.getAsJsonObject().get("uuids").getAsJsonArray();
            HashMap<UUID, String> onlinePlayers = new HashMap<>();
            for (JsonElement element : users) {
                onlinePlayers.put(UUID.fromString(element.getAsJsonObject().get("uuid").getAsString()), element.getAsJsonObject().get("username").getAsString());
            }
            Hysentials.INSTANCE.getOnlineCache().setOnlinePlayers(onlinePlayers);
            HashMap<UUID, String> rankCache = new HashMap<>();
            ArrayList<UUID> plusPlayers = new ArrayList<>();

            for (JsonElement element : ranks.getAsJsonObject().get("ranks").getAsJsonArray()) {
                JsonArray uuids = element.getAsJsonObject().get("users").getAsJsonArray();
                for (JsonElement uuid : uuids) {
                    if (onlinePlayers.containsKey(UUID.fromString(uuid.getAsString()))) {
                        if (element.getAsJsonObject().get("name").getAsString().equals("Plus")) {
                            plusPlayers.add(UUID.fromString(uuid.getAsString()));
                        } else {
                            Rank rank = Rank.valueOF(element.getAsJsonObject().get("name").getAsString().toUpperCase());
                            Rank oldRank = Rank.valueOF(rankCache.get(UUID.fromString(uuid.getAsString())));
                            if (oldRank != null && oldRank.index > rank.index) continue;
                            rankCache.put(UUID.fromString(uuid.getAsString()), element.getAsJsonObject().get("name").getAsString());
                        }
                    }
                }
            }
            Hysentials.INSTANCE.getOnlineCache().rankCache = rankCache;
            Hysentials.INSTANCE.getOnlineCache().plusPlayers = plusPlayers;
            diagnostics.add("Parsing took " + (System.currentTimeMillis() - start2) + "ms");

            return onlinePlayers;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public static boolean hasCosmetic(UUID uuid, String cosmetic) {
        if (cosmetics == null) return false;
        List<Object> list = toList(cosmetics);
        return list.stream().anyMatch(o -> ((JSONObject) o).getString("name").equals(cosmetic) && ((JSONObject) o).getJSONArray("users").toList().contains(uuid.toString()));
    }

    public static boolean equippedCosmetic(UUID uuid, String cosmetic) {
        if (cosmetics == null) return false;
        List<Object> list = toList(cosmetics);
        return list.stream().anyMatch(o -> ((JSONObject) o).getString("name").equals(cosmetic) && ((JSONObject) o).getJSONArray("users").toList().contains(uuid.toString()));
    }

    public static List<JSONObject> getCosmetics() {
        if (cosmetics == null) return new ArrayList<>();
        List<JSONObject> list = new ArrayList<>();
        for (Object o : toList(cosmetics)) {
            JSONObject object = (JSONObject) o;
            list.add(object);
        }
        return list;
    }

    public static String getRequest(String endpoint) {
        return "http://127.0.0.1:8080/api/" + endpoint + "?key=" + Socket.serverId + "&uuid=" + Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString();
    }

    public static JSONObject getAction(String id, String creator) {
        if (actions == null) return null;
        return (JSONObject) toList(actions).stream().filter(o -> {
            JSONObject object = ((JSONObject) o);
            return object.getJSONObject("action").getString("creator").equals(creator) && object.getString("id").equals(id);
        }).findFirst().orElse(null);
    }

    public static Rank getRank(String uuid) {
        BlockWAPIUtils.Rank rank;
        try {
            rank = BlockWAPIUtils.Rank.valueOf(Hysentials.INSTANCE.getOnlineCache().rankCache.get(UUID.fromString(uuid)).toUpperCase());
        } catch (Exception e) {
            rank = BlockWAPIUtils.Rank.DEFAULT;
        }
        return rank;
    }

    public static Rank getRank(UUID uuid) {
        BlockWAPIUtils.Rank rank;
        try {
            rank = BlockWAPIUtils.Rank.valueOf(Hysentials.INSTANCE.getOnlineCache().rankCache.get(uuid).toUpperCase());
        } catch (Exception e) {
            rank = BlockWAPIUtils.Rank.DEFAULT;
        }
        return rank;
    }

    public static String getUsername(UUID uuid) {
        return Socket.cachedUsers.stream().filter(user -> user.getString("uuid").equals(uuid.toString())).findFirst().orElse(new JSONObject().put("username", "unknown")).getString("username");
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final JsonParser PARSER = new JsonParser();

    public static List<Group> getGroups() {
        try {
            JsonElement groups = NetworkUtils.getJsonElement("http://127.0.0.1:8080/api/groups");
            if (groups.isJsonNull()) return new ArrayList<>();
            JsonArray groupsArray = groups.getAsJsonObject().get("groups").getAsJsonArray();
            List<Group> groupList = new ArrayList<>();
            for (JsonElement element : groupsArray) {
                Group group = GSON.fromJson(element, Group.class);
                groupList.add(group);
            }
            Hysentials.INSTANCE.getOnlineCache().setCachedGroups(groupList);
            return groupList;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public enum Rank {
        ADMIN(6, "1", "§c[ADMIN] ", "§c", "admin"),
        MOD(4, "2", "§2[MOD] ", "§2", "mod"),
        HELPER(3, "3", "§9[HELPER] ", "§9", "helper"),
        CREATOR(2, "4", "§3[§fCREATOR§3] ", "§3", "creator"),
        TEAM(5, "5", "§6[TEAM] ", "§6", "team"),
        DEFAULT(1, "replace", "", "", "", "");

        public final int index;
        private final String id;
        private final String prefix;
        private final String color;
        private final String hex;

        private String placeholder;

        Rank(int index, String id, String prefix, String color, String hex) {
            this.index = index;
            this.id = id;
            this.prefix = prefix;
            this.color = color;
            this.hex = hex;
        }

        Rank(int index, String id, String prefix, String color, String placeholder, String hex) {
            this.index = index;
            this.id = id;
            this.prefix = prefix;
            this.color = color;
            this.placeholder = placeholder;
            this.hex = hex;
        }

        public String getId() {
            return id;
        }

        public String getPrefix(String name) {
            return prefix;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getColor() {
            if (HysentialsConfig.futuristicRanks) {
                return getNametag();
            }
            return color;
        }

        public String getNametag() {
            if (HysentialsConfig.futuristicRanks) {
                JSONObject colorGroup = Hysentials.INSTANCE.rankColors.jsonObject.getJSONObject(hex);
                return "<" + colorGroup.getString("nametag_color") + ">";
            } else {
                return color;
            }
        }

        public String getChat() {
            if (HysentialsConfig.futuristicRanks) {
                JSONObject colorGroup = Hysentials.INSTANCE.rankColors.jsonObject.getJSONObject(hex);
                return "<" + colorGroup.getString("chat_message_color") + ">";
            } else {
                return "§f";
            }
        }

        public String getHex() {
            return getNametag();
        }

        public String getPlaceholder() {
            if (placeholder == null && ImageIcon.getIcon(name().toLowerCase()) != null) {
                return "§f:" + ImageIcon.getIcon(name().toLowerCase()).getName() + ": " + (hex.isEmpty() ? color : getNametag());
            }
            return placeholder;
        }


        public static Rank valueOF(String s) {
            if (s == null) return null;
            for (Rank rank : values()) {
                if (rank.name().equalsIgnoreCase(s)) {
                    return rank;
                }
            }
            return null;
        }
    }

    public static class Group {
        private final String name;
        private final String groupId;
        private final String owner;
        private final List<String> members;
        private final List<String> officers;
        private final List<String> invites;
        private final String color;
        private final JsonObject settings;
        private final List<String> messages;
        private final List<String> hidden;


        public Group(String name, String groupId, String owner, List<String> members, List<String> officers, List<String> invites, String color, JsonObject settings, List<String> messages, List<String> hidden) {
            this.name = name;
            this.groupId = groupId;
            this.owner = owner;
            this.members = members;
            this.officers = officers;
            this.invites = invites;
            this.color = color;
            this.settings = settings;
            this.messages = messages;
            this.hidden = hidden;
        }

        public String getName() {
            return name;
        }

        public String getGroupId() {
            return groupId;
        }

        public String getOwner() {
            return owner;
        }

        public List<String> getMembers() {
            return members;
        }

        public List<String> getOfficers() {
            return officers;
        }

        public List<String> getInvites() {
            return invites;
        }

        public String getColor() {
            return color;
        }

        public JsonObject getSettings() {
            return settings;
        }

        public List<String> getMessages() {
            return messages;
        }

        @Override
        public String toString() {
            return "Group{" +
                "name='" + name + '\'' +
                ", groupId='" + groupId + '\'' +
                ", owner='" + owner + '\'' +
                ", members=" + members +
                ", officers=" + officers +
                ", invites=" + invites +
                ", color='" + color + '\'' +
                ", settings=" + settings +
                ", messages=" + messages +
                '}';
        }

        public List<String> getHidden() {
            return hidden;
        }

        public boolean isHidden(String uuid) {
            return hidden.contains(uuid);
        }
    }
}
