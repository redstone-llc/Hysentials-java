/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2020, 2021, 2022, 2023  Polyfrost, Sk1er LLC and contributors
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

package llc.redstone.hysentials.handlers.lobby;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import llc.redstone.hysentials.forge.HysentialsMixinPlugin;
import llc.redstone.hysentials.handlers.redworks.BwRanksUtils;
import llc.redstone.hysentials.util.BlockWAPIUtils;
import llc.redstone.hysentials.util.LocrawInfo;
import llc.redstone.hysentials.util.LocrawUtil;
import llc.redstone.hysentials.utils.StringUtilsKt;
import net.hypixel.data.type.GameType;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

/**
 * Used in {@link HysentialsMixinPlugin
 * Borrowed from Hytils Reborn
 */
@SuppressWarnings("unused")
public class TabChanger {
    public static Ordering<NetworkPlayerInfo> ordering = Ordering.from(new PlayerComparator());

    public static String modifyName(String displayName, NetworkPlayerInfo playerInfo) {
        if (HypixelUtils.INSTANCE.isHypixel()) {
            final UUID uuid = playerInfo.getGameProfile().getId();
            final String username = playerInfo.getGameProfile().getName();
            final LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();

            if (locraw != null && LocrawUtil.INSTANCE.isInGame() && locraw.getGameType().equals(GameType.SKYBLOCK)) {
                return displayName;
            }

            if (!BwRanksUtils.futuristicRanks(true)) {
                //TODO add regular rank formatting
                //[admin], [creator], [mod], etc
            }

            String prefixName = StringUtilsKt.substringBefore(displayName, username) + username;
            String suffix = StringUtilsKt.substringAfter(displayName, username);

            String newPrefix = BwRanksUtils.getMessage(prefixName, username, playerInfo.getGameProfile().getId(), true, true);

            return newPrefix + suffix;
        }

        return displayName;
    }

    @SideOnly(Side.CLIENT)
    static class PlayerComparator implements Comparator<NetworkPlayerInfo> {
        private PlayerComparator() {
        }

        public int compare(NetworkPlayerInfo networkPlayerInfo, NetworkPlayerInfo networkPlayerInfo2) {
            LocrawInfo locrawInfo = LocrawUtil.INSTANCE.getLocrawInfo();

            ScorePlayerTeam scorePlayerTeam = networkPlayerInfo.getPlayerTeam();
            ScorePlayerTeam scorePlayerTeam2 = networkPlayerInfo2.getPlayerTeam();

            BlockWAPIUtils.Rank rank = BlockWAPIUtils.getRank(networkPlayerInfo.getGameProfile().getId());
            BlockWAPIUtils.Rank rank2 = BlockWAPIUtils.getRank(networkPlayerInfo2.getGameProfile().getId());

            if (locrawInfo != null && Objects.equals(locrawInfo.getGameType(), GameType.HOUSING) && locrawInfo.getLobbyName() == null) {
                //In a house, sort by housing team name before hysentials rank
                return ComparisonChain.start().compareTrueFirst(networkPlayerInfo.getGameType() != WorldSettings.GameType.SPECTATOR, networkPlayerInfo2.getGameType() != WorldSettings.GameType.SPECTATOR).compare(scorePlayerTeam != null ? scorePlayerTeam.getRegisteredName() : "", scorePlayerTeam2 != null ? scorePlayerTeam2.getRegisteredName() : "").compare(
                    rank2 != null ? rank2.index : -1, rank != null ? rank.index : -1
                ).compare(networkPlayerInfo.getGameProfile().getName(), networkPlayerInfo2.getGameProfile().getName()).result();
            }

            //Not in a house, sort by hysentials rank before team name
            return ComparisonChain.start().compareTrueFirst(networkPlayerInfo.getGameType() != WorldSettings.GameType.SPECTATOR, networkPlayerInfo2.getGameType() != WorldSettings.GameType.SPECTATOR).compare(
                rank2 != null ? rank2.index : -1, rank != null ? rank.index : -1
            ).compare(scorePlayerTeam != null ? scorePlayerTeam.getRegisteredName() : "", scorePlayerTeam2 != null ? scorePlayerTeam2.getRegisteredName() : "").compare(networkPlayerInfo.getGameProfile().getName(), networkPlayerInfo2.getGameProfile().getName()).result();
        }
    }
}
