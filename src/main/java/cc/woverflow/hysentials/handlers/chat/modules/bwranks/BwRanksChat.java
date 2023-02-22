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
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.util.BlockWAPIUtils;
import cc.woverflow.hysentials.util.HypixelRanks;
import net.minecraft.client.Minecraft;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BwRanksChat {

    static HashMap<UUID, String> previousNames = new HashMap<>();
    public static String onMessageReceivedS(String message) {
        HashMap<UUID, String> users = new HashMap<>(previousNames);
        Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().forEach(playerInfo -> {
            if (!previousNames.containsKey(playerInfo.getGameProfile().getId())) {
                users.put(playerInfo.getGameProfile().getId(), playerInfo.getGameProfile().getName());
                previousNames.put(playerInfo.getGameProfile().getId(), playerInfo.getGameProfile().getName());
            }
        });

        for (Map.Entry<UUID, String> user : users.entrySet()) {
            UUID uuid = user.getKey();
            String name = user.getValue();

            String regex1 = "\\[[A-Za-z§0-9+]+] " + name;
            String regex2 = "(§r§7|§7)" + name;
            String regex3 = "[a-f0-9§]{2}" + name;

            String replacement = Hysentials.INSTANCE.getOnlineCache().getPlayerDisplayNames().get(uuid);
            if (replacement == null) {
                continue;
            }
            if (Pattern.compile(regex1).matcher(message).find(0)) {
                message = message.replaceAll("\\[[A-Za-z§0-9+]+] " + name, replacement).replace("§7:", "§f:");
            } else if (Pattern.compile(regex2).matcher(message.split("§7:")[0]).find(0)) {
                message = message.replaceAll("(§r§7|§7)" + name, replacement).replace("§7:", "§f:");
            } else if (Pattern.compile(regex3).matcher(message).find(0)) {
                message = message.replaceAll("[a-f0-9§]{2}" + name, replacement).replace("§7:", "§f:");
            }
        }
        return message;
    }

    public static String getMessage(String message) {
        try {
            HashMap<UUID, String> users = new HashMap<>(previousNames);
            Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().forEach(playerInfo -> {
                if (!previousNames.containsKey(playerInfo.getGameProfile().getId())) {
                    users.put(playerInfo.getGameProfile().getId(), playerInfo.getGameProfile().getName());
                    previousNames.put(playerInfo.getGameProfile().getId(), playerInfo.getGameProfile().getName());
                }
            });
            for (Map.Entry<UUID, String> user : users.entrySet()) {
                UUID uuid = user.getKey();
                String name = user.getValue();
                BlockWAPIUtils.Rank rank = null;
                if (Hysentials.INSTANCE.getOnlineCache().getOnlinePlayers().containsKey(uuid)) {
                    try {
                        rank = BlockWAPIUtils.Rank.valueOf(Hysentials.INSTANCE.getOnlineCache().getRankCache().get(uuid).toUpperCase());
                    } catch (Exception ignored) {
                        rank = BlockWAPIUtils.Rank.DEFAULT;
                    }
                }

                String regex1 = "\\[[A-Za-z§0-9+]+] " + name;
                String regex2 = "(§r§7|§7)" + name;
                String regex3 = "[a-f0-9§]{2}" + name;
                if (rank != null && rank != BlockWAPIUtils.Rank.DEFAULT) {
                    String replacement = (rank.getPrefix(name) + name + getPlus(uuid));
                    if (HysentialsConfig.futuristicRanks) {
                        replacement = (rank.getPlaceholder() + name + getPlus(uuid));
                    }
                    Matcher m1 = Pattern.compile(regex1).matcher(message);
                    if (m1.find(0)) {
                        message = message.replaceAll("\\[[A-Za-z§0-9+]+] " + name, replacement).replace("§7:", "§f:");
                    } else if (Pattern.compile(regex2).matcher(message.split("§7:")[0]).find(0)) {
                        message = message.replaceAll("(§r§7|§7)" + name, replacement).replace("§7:", "§f:");
                    } else if (Pattern.compile(regex3).matcher(message).find(0)) {
                        message = message.replaceAll("[a-f0-9§]{2}" + name, replacement).replace("§7:", "§f:");
                    }
                } else {
                    Matcher m1 = Pattern.compile(regex1).matcher(message);
                    if (m1.find(0)) {
                        message = message.replaceAll("\\[[A-Za-z§0-9+]+] " + name, getReplacement(m1.group(0).split(" ")[0], name, uuid));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public static String getReplacement(String match, String name, UUID uuid) {
        String replacement = "";
        for (HypixelRanks rank : HypixelRanks.values()) {
            if (rank.getPrefix().replace(" ", "").equals(match)) {
                replacement = (rank.getPrefix() + name + getPlus(uuid));
                if (HysentialsConfig.futuristicRanks) {
                    replacement = (rank.getAsPlaceholder() + name + getPlus(uuid));
                }
            }
        }
        return replacement;
    }


    public static String getPlus(UUID id) {
        return Hysentials.INSTANCE.getOnlineCache().getPlusPlayers().contains(id) ? " §6[+]§r" : "";
    }
}
