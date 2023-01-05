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

package cc.woverflow.hysentials.handlers.chat.modules.bwranks;

import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.command.HysentialsCommand;
import cc.woverflow.hysentials.handlers.chat.ChatReceiveModule;
import cc.woverflow.hysentials.util.BlockWAPIUtils;
import cc.woverflow.hysentials.util.DuoVariable;
import cc.woverflow.hysentials.util.blockw.OnlineCache;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BwRanksChat implements ChatReceiveModule {
    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        try {
            final String message = event.message.getFormattedText();
            if (HysentialsCommand.collecting) {
                if (StringUtils.contains(message, Minecraft.getMinecraft().thePlayer.getName())) {
                    HysentialsCommand.messages.add(message);
                }
            }
            for (Map.Entry<UUID, String> entry : Hysentials.INSTANCE.getOnlineCache().getOnlinePlayers().entrySet()) {
                UUID uuid = entry.getKey();
                String name = entry.getValue();
                BlockWAPIUtils.Rank rank = null;
                try {
                    rank = BlockWAPIUtils.Rank.valueOf(Hysentials.INSTANCE.getOnlineCache().getRankCache().get(uuid).toUpperCase());
                } catch (Exception ignored) {
                    rank = BlockWAPIUtils.Rank.DEFAULT;
                }
                if (!rank.equals(BlockWAPIUtils.Rank.DEFAULT)) {
                    String regex1 = "\\[[A-Za-z§0-9+]+] " + name;
                    String regex2 = "(§r§7|§7)" + name;
                    String regex3 = "[a-f0-9§]{2}" + name;
                    if (Pattern.compile(regex1).matcher(message).find(0)) {
                        event.message = colorMessage(message.replaceAll("\\[[A-Za-z§0-9+]+] " + name, rank.getPrefix() + name + getPlus(uuid)).replace("§7:", "§f:"));
                    } else if (Pattern.compile(regex2).matcher(message).find(0)) {
                        event.message = colorMessage(message.replaceAll("(§r§7|§7)" + name, rank.getPrefix() + name + getPlus(uuid)).replace("§7:", "§f:"));
                    } else if (Pattern.compile(regex3).matcher(message).find(0)) {
                        event.message = colorMessage(message.replaceAll("[a-f0-9§]{2}" + name, rank.getColor() + name + getPlus(uuid)).replace("§7:", "§f:"));
                    }
                } else {
                    event.message = colorMessage(message.replaceAll(name, name + getPlus(uuid)));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getPlus(UUID id) {
        return Hysentials.INSTANCE.getOnlineCache().getPlusPlayers().contains(id) ? " §6[+]§r" : "";
    }
}
