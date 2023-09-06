package cc.woverflow.hysentials.util.blockw;

import cc.woverflow.hysentials.user.Player;
import cc.woverflow.hysentials.util.BlockWAPIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class OnlineCache {
    public HashMap<UUID, String> onlinePlayers = new HashMap<>();
    public HashMap<UUID, Player> playersCache = new HashMap<>();
    public HashMap<UUID, String> rankCache = new HashMap<>();
    public ArrayList<BlockWAPIUtils.Group> groups = new ArrayList<>(); //This may be a bit of a memory leak...... :P
    public ArrayList<UUID> plusPlayers = new ArrayList<>();

    public HashMap<UUID, String> playerDisplayNames = new HashMap<>();
    public HashMap<String, UUID> guildCache = new HashMap<>();

    public void setOnlinePlayers(HashMap<UUID, String> onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public HashMap<UUID, String> getOnlinePlayers() {
        return onlinePlayers;
    }

    public HashMap<UUID, String> getRankCache() {
        return rankCache;
    }

    public ArrayList<UUID> getPlusPlayers() {
        return plusPlayers;
    }

    public List<BlockWAPIUtils.Group> getCachedGroups() {
        return groups;
    }

    public HashMap<UUID, String> getPlayerDisplayNames() {
        return playerDisplayNames;
    }

    public void setCachedGroups(List<BlockWAPIUtils.Group> groups) {
        this.groups = new ArrayList<>(groups);
    }
}
