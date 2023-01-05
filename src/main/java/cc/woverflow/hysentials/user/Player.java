package cc.woverflow.hysentials.user;

import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.util.BlockWAPIUtils;
import cc.woverflow.hysentials.util.HypixelAPIUtils;
import cc.woverflow.hysentials.util.blockw.OnlineCache;
import cc.woverflow.hytils.config.HytilsConfig;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Player {
    public static Player CLIENT;
    private BlockWAPIUtils.Rank rank;
    private boolean isPlus;
    private boolean isOnline;
    private String username;
    private final String uuid;
    private final List<String> groups;

    public Player(String username, String uuid) {
        this.username = username;
        this.uuid = uuid;
        this.groups = new ArrayList<>();
    }

    public String getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BlockWAPIUtils.Rank getRank() {
        return rank;
    }

    public void setRank(BlockWAPIUtils.Rank rank) {
        this.rank = rank;
    }

    public List<String> getGroups() {
        return groups;
    }

    /*
    This method is only supposed to be used occasionally, as it is very slow. It is recommended to use the cache instead. (OnlineCache)

    Should be used in an async thread.
     */
    public String getDisplayName() {
        String displayName = username;
        JsonElement request = NetworkUtils.getJsonElement("https://api.hypixel.net/player?key" + HytilsConfig.apiKey + "&uuid=" + uuid.replace("-", ""));
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
        Hysentials.INSTANCE.getOnlineCache().playerDisplayNames.put(UUID.fromString(uuid), displayName);
        return displayName;
    }

    public boolean isPlus() {
        return isPlus;
    }

    public void setPlus(boolean plus) {
        isPlus = plus;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
