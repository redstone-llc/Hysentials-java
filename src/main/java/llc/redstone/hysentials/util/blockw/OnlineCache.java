package llc.redstone.hysentials.util.blockw;

import llc.redstone.hysentials.util.BlockWAPIUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class OnlineCache {
    public HashMap<UUID, String> onlinePlayers = new HashMap<>();
    public HashMap<UUID, String> playerDisplayNames = new HashMap<>();
    public HashMap<String, UUID> guildCache = new HashMap<>();

    public void setOnlinePlayers(HashMap<UUID, String> onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

}
