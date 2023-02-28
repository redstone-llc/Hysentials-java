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

package cc.woverflow.hysentials.handlers.redworks;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.handlers.chat.modules.bwranks.BwRanksChat;
import cc.woverflow.hysentials.util.BlockWAPIUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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
                BlockWAPIUtils.getOnline(); // Update online cache
            });
            this.tick = 0;
        }
        if (tick % 20 == 0) {
            if (!HysentialsConfig.futuristicRanks) {
                Hysentials.INSTANCE.getOnlineCache().playerDisplayNames.clear();
                return;
            }
            Multithreading.runAsync(() -> {
                Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().forEach(playerInfo -> {
                    String name = playerInfo.getGameProfile().getName();
                    ScorePlayerTeam team = Minecraft.getMinecraft().theWorld.getScoreboard().getPlayersTeam(name);

                    String displayName = team.getColorPrefix() + name;

                    displayName = BwRanksChat.getMessage(displayName, name, playerInfo.getGameProfile().getId());

                    Hysentials.INSTANCE.getOnlineCache().playerDisplayNames.put(playerInfo.getGameProfile().getId(), displayName);
                });

            });
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        this.tick = 79;
    }
}
