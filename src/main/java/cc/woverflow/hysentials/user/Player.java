package cc.woverflow.hysentials.user;

import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.util.BlockWAPIUtils;
import cc.woverflow.hysentials.util.HypixelAPIUtils;
import cc.woverflow.hytils.config.HytilsConfig;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

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
    private String displayName;
    private final List<String> groups;

    public Player(String username, String uuid) {
        this.username = username;
        this.uuid = uuid;
        this.groups = new ArrayList<>();
    }

    public EntityPlayer getClientPlayer() {
        if (this == CLIENT) {
            return Minecraft.getMinecraft().thePlayer;
        } else {
            if (Minecraft.getMinecraft().theWorld == null) return null;
            return Minecraft.getMinecraft().theWorld.getPlayerEntityByUUID(UUID.fromString(uuid));
        }
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

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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


    public static Player getPlayer(UUID uuid) {
        return Hysentials.INSTANCE.getOnlineCache().playersCache.get(uuid);
    }

    public static Player getPlayer(EntityPlayer player) {
        return getPlayer(player.getUniqueID());
    }

    public static Player getPlayer() {
        return CLIENT;
    }

    public static Player getPlayer(String username) {
        for (Map.Entry<UUID, Player> entry : Hysentials.INSTANCE.getOnlineCache().playersCache.entrySet()) {
            if (entry.getValue().getUsername().equalsIgnoreCase(username)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
