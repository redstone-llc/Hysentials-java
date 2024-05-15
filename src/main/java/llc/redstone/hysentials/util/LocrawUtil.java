/*
 * This file is part of OneConfig.
 * OneConfig - Next Generation Config Library for Minecraft: Java Edition
 * Copyright (C) 2021~2023 Polyfrost.
 *   <https://polyfrost.cc> <https://github.com/Polyfrost/>
 *
 * Co-author: lyndseyy (Lyndsey Winter) <https://github.com/lyndseyy>
 * Co-author: asbyth <cyronize@gmail.com> (deleted GitHub account)
 * Co-author: Moire9 (Moiré) <https://github.com/Moire9> (non-copyrightable contribution)
 * Co-author: Sk1er (Mitchell Katz) <https://github.com/Sk1er>
 * Co-author: Cubxity <https://github.com/Cubxity> (non-copyrightable contribution)
 * Co-author: UserTeemu <https://github.com/UserTeemu> (non-copyrightable contribution)
 * Co-author: PyICoder (Befell) <https://github.com/PyICoder> (non-copyrightable contribution)
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 *   OneConfig is licensed under the terms of version 3 of the GNU Lesser
 * General Public License as published by the Free Software Foundation, AND
 * under the Additional Terms Applicable to OneConfig, as published by Polyfrost,
 * either version 1.0 of the Additional Terms, or (at your option) any later
 * version.
 *
 *   This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 * License.  If not, see <https://www.gnu.org/licenses/>. You should
 * have also received a copy of the Additional Terms Applicable
 * to OneConfig, as published by Polyfrost. If not, see
 * <https://polyfrost.cc/legal/oneconfig/additional-terms>
 */

package llc.redstone.hysentials.util;

import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import llc.redstone.hysentials.Hysentials;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundLocationPacket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.Nullable;

/**
 * <p>
 * An easy way to interact with the Hypixel Locraw API.
 * </p>
 * Modified from Hytilities by Sk1erLLC
 * <a href="https://github.com/Sk1erLLC/Hytilities/blob/master/LICENSE">https://github.com/Sk1erLLC/Hytilities/blob/master/LICENSE</a>
 */
public class LocrawUtil {
    public static final LocrawUtil INSTANCE = new LocrawUtil();

    private LocrawInfo locrawInfo;
    private LocrawInfo lastLocrawInfo;
    private int tick;
    private boolean inGame = false;

    public void init() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (locrawInfo != null) {
            lastLocrawInfo = locrawInfo;
        }
        locrawInfo = null;
        tick = 0;
    }

    public void sendLocraw() {
        Hysentials.INSTANCE.hypixelModAPI.sendPacket(HypixelPacketType.LOCATION,
            packet -> {
                ClientboundLocationPacket locationPacket = (ClientboundLocationPacket) packet;
                try {
                    locrawInfo = new LocrawInfo(locationPacket);
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
                if (locrawInfo != null) {
                    inGame = locrawInfo.getLobbyName() == null;
                }
            }
        );
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START || !HypixelUtils.INSTANCE.isHypixel()) {
            return;
        }

        this.tick++;
        if (this.tick == 40 || this.tick % 520 == 0) {
            sendLocraw();
        }
    }

    /**
     * Returns whether the player is in game.
     *
     * @return Whether the player is in game.
     */
    public boolean isInGame() {
        return inGame;
    }

    /**
     * Returns the current {@link LocrawInfo}.
     *
     * @return The current {@link LocrawInfo}.
     * @see LocrawInfo
     */
    @Nullable
    public LocrawInfo getLocrawInfo() {
        return locrawInfo;
    }

    /**
     * Returns the previous {@link LocrawInfo}.
     *
     * @return The previous {@link LocrawInfo}.
     * @see LocrawInfo
     */
    @Nullable
    public LocrawInfo getLastLocrawInfo() {
        return lastLocrawInfo;
    }
}
