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

package cc.woverflow.hysentials.handlers.bwranks;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.util.BlockWAPIUtils;
import cc.woverflow.hytils.config.HytilsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BwRanks {
    private int tick;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        final LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        if (event.phase != TickEvent.Phase.START || Minecraft.getMinecraft().thePlayer == null || !HypixelUtils.INSTANCE.isHypixel() || locraw == null) {
            return;
        }

        if (++this.tick == 80) {
            Multithreading.runAsync(() -> {
                List<String> users = new ArrayList<>();
                Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().forEach(playerInfo -> {
                    users.add(playerInfo.getGameProfile().getName());
                });

                BlockWAPIUtils.getOnline().forEach((id, user) -> {
                    BlockWAPIUtils.Rank rank;
                    try {
                         rank = BlockWAPIUtils.Rank.valueOf(Hysentials.INSTANCE.getOnlineCache().getRankCache().get(id).toUpperCase());
                    } catch (Exception ignored) {
                        rank = BlockWAPIUtils.Rank.DEFAULT;
                    }
                    if (!users.contains(user)) return;
                    String prefix = "§r§a■ ";
                    prefix += (HytilsConfig.hidePlayerRanksInTab ? rank.getColor() : rank.getPrefix()); //get the prefix of the rank
                    ScorePlayerTeam team = Minecraft.getMinecraft().theWorld.getScoreboard().getPlayersTeam(user); //get the team of the player
                    String teamName = team.getTeamName(); //get the name of the team
                    String suffix = team.getColorSuffix(); //get the suffix of the team
                    String oldPrefix = team.getColorPrefix(); //get the old prefix of the team
                    if (suffix == null) suffix = "§r"; //if the suffix is null, set it to §r
                    if (Hysentials.INSTANCE.getOnlineCache().getPlusPlayers().contains(id)) {
                        suffix = suffix.startsWith(" §6[+] ") ? suffix : " §6[+] " + suffix; //if the suffix doesn't start with &6[+], add it
                    }

                    Minecraft.getMinecraft().theWorld.getScoreboard().removePlayerFromTeams(user); //remove the player from the team
                    ScorePlayerTeam newTeam = Minecraft.getMinecraft().theWorld.getScoreboard().createTeam( //create a new team
                        rank.getId().equals("replace") ? (teamName.substring(0,4) + "." + UUID.randomUUID().toString().substring(0, 4)) : (rank.getId() + "." + UUID.randomUUID().toString().substring(0, 4))
                    );
                    newTeam.setNamePrefix(rank.equals(BlockWAPIUtils.Rank.DEFAULT) ? (oldPrefix.startsWith("§r§a■ ") ? "" : "§r§a■ ") + oldPrefix : prefix); //set the prefix of the team also add the §r§a■ if the rank is default
                    newTeam.setNameSuffix(HytilsConfig.hideGuildTagsInTab ? "" : suffix); //set the suffix of the team
                    Minecraft.getMinecraft().theWorld.getScoreboard().addPlayerToTeam(user, newTeam.getTeamName()); //add the player to the new team
                });

            });

            this.tick = 0;
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        this.tick = 79;
    }
}
