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

package cc.woverflow.hysentials.util.blockw;

import cc.woverflow.hysentials.user.Player;
import cc.woverflow.hysentials.util.BlockWAPIUtils;
import cc.woverflow.hysentials.util.DuoVariable;

import java.util.*;

public class OnlineCache {
    public HashMap<UUID, String> onlinePlayers = new HashMap<>();
    public List<Player> playersCache = new ArrayList<>();
    public HashMap<UUID, String> rankCache = new HashMap<>();
    public ArrayList<BlockWAPIUtils.Group> groups = new ArrayList<>(); //This may be a bit of a memory leak...... :P
    public ArrayList<UUID> plusPlayers = new ArrayList<>();

    public HashMap<UUID, String> playerDisplayNames = new HashMap<>();

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
