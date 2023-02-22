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

import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.handlers.imageicons.ImageIcon;
import cc.woverflow.hysentials.user.Player;
import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class BlockWAPIUtils {

    @NotNull
    public static HashMap<UUID, String> getOnline() {
        try {
            List<Player> players = new ArrayList<>();
            //http://localhost/
            //https://hysentials.blockworks.studio/
            //https://hysentials.redstone.llc/
            JsonElement online = null;
            JsonElement ranks = null;
            try {
                online = NetworkUtils.getJsonElement("https://hysentials.redstone.llc/api/online");
                ranks = NetworkUtils.getJsonElement("https://hysentials.redstone.llc/api/ranks");
            } catch (Exception ignored) {
            }
            if (online == null || online.isJsonNull()) return new HashMap<>();
            if (ranks == null || ranks.isJsonNull()) return new HashMap<>();

            JsonArray users = online.getAsJsonObject().get("uuids").getAsJsonArray();
            HashMap<UUID, String> onlinePlayers = new HashMap<>();
            for (JsonElement element : users) {
                players.add(new Player(element.getAsJsonObject().get("username").getAsString(), element.getAsJsonObject().get("uuid").getAsString()));
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
                            players.forEach(player -> {
                                if (player.getUuid().equals(uuid.getAsString())) {
                                    player.setPlus(true);
                                }
                            });
                            plusPlayers.add(UUID.fromString(uuid.getAsString()));
                        } else {
                            players.forEach(player -> {
                                if (player.getUuid().equals(uuid.getAsString())) {
                                    player.setRank(Rank.valueOf(element.getAsJsonObject().get("name").getAsString().toUpperCase()));
                                }
                            });
                            rankCache.put(UUID.fromString(uuid.getAsString()), element.getAsJsonObject().get("name").getAsString());
                        }
                    }
                }
            }
            players.forEach(player -> {
                Hysentials.INSTANCE.getOnlineCache().playersCache.put(UUID.fromString(player.getUuid()), player);
            });
            Hysentials.INSTANCE.getOnlineCache().rankCache = rankCache;
            Hysentials.INSTANCE.getOnlineCache().plusPlayers = plusPlayers;

            return onlinePlayers;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final JsonParser PARSER = new JsonParser();

    public static List<Group> getGroups() {
        try {
            JsonElement groups = NetworkUtils.getJsonElement("https://hysentials.redstone.llc/api/groups");
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
        ADMIN("1", "§c[ADMIN] ", "§c"),
        MOD("2", "§2[MOD] ", "§2"),
        CREATOR("3", "§3[§fCREATOR§3] ", "§3"),
        DEFAULT("replace", "", "", "");

        private final String id;
        private final String prefix;
        private final String color;

        private String placeholder;

        Rank(String id, String prefix, String color) {
            this.id = id;
            this.prefix = prefix;
            this.color = color;
        }
        Rank(String id, String prefix, String color, String placeholder) {
            this.id = id;
            this.prefix = prefix;
            this.color = color;
            this.placeholder = placeholder;
        }

        public String getId() {
            return id;
        }

        public String getPrefix(String name) {
            return prefix;
        }

        public String getColor() {
            return color;
        }

        public String getPlaceholder() {
            if (placeholder == null && ImageIcon.getIcon(name().toLowerCase()) != null) {
                return color + ":" + ImageIcon.getIcon(name().toLowerCase()).getName() + ": ";
            }
            return placeholder;
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
