package cc.woverflow.hysentials.handlers.chat.modules.bwranks;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.handlers.chat.ChatReceiveModule;
import cc.woverflow.hysentials.handlers.redworks.BwRanks;
import cc.woverflow.hysentials.util.BlockWAPIUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cc.woverflow.hysentials.handlers.redworks.BwRanksUtils.getPlus;
import static cc.woverflow.hysentials.handlers.redworks.BwRanksUtils.getReplacement;

public class BWSReplace implements ChatReceiveModule {
    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        if (!HypixelUtils.INSTANCE.isHypixel()) return;
        if (event.type != 0 && event.type != 1) return;
        String message = event.message.getFormattedText();
        if (BwRanks.hidingGuildList) {
            if (message.equals("§b§m-----------------------------------------------------§r") && BwRanks.lines.size() > 1) {
                for (String username : getUsernames(BwRanks.lines)) {
                    Hysentials.INSTANCE.getOnlineCache().guildCache.put(username, null);
                }
                BwRanks.lines.clear();
                BwRanks.hidingLastMessage = true;
            } else {
                BwRanks.lines.add(message);
            }
        }
        HashMap<String, UUID> users = new HashMap<>(Hysentials.INSTANCE.getOnlineCache().guildCache);
        Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().forEach(playerInfo -> {
            users.put(playerInfo.getGameProfile().getName(), playerInfo.getGameProfile().getId());
        });
        for (Map.Entry<String, UUID> user : users.entrySet()) {
            String name = user.getKey();
            UUID uuid = user.getValue();
            if (uuid.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) continue;
            try {
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
                    String replacement = (rank.getPrefix(name) + name + (getPlus(uuid)));
                    if (HysentialsConfig.futuristicRanks) {
                        replacement = (rank.getPlaceholder() + name + (getPlus(uuid)));
                        if (!BwRanks.hasRank) {
                            replacement = (rank.getPlaceholder() + name);
                        }
                    }
                    if (!BwRanks.hasRank) {
                        replacement = (rank.getColor() + name);
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
                    Matcher m2 = Pattern.compile(regex2).matcher(message);
                    Matcher m3 = Pattern.compile(regex3).matcher(message);
                    if (m1.find(0) && BwRanks.hasRank) {
                        message = message.replaceAll("\\[[A-Za-z§0-9+]+] " + name, getReplacement(m1.group(0).split(" ")[0], name, uuid, false));
                    }
                    if (m2.find(0) && BwRanks.hasRank) {
                        message = message.replaceAll("(§r§7|§7)" + name, getReplacement("§7", name, uuid, LocrawUtil.INSTANCE.getLocrawInfo().getGameType().equals(LocrawInfo.GameType.SKYBLOCK)));
                    }
                    if (m3.find(0) && (!BwRanks.hasRank)) {
                        message = message.replaceAll("[a-f0-9§]{2}" + name, getReplacement(m3.group(0).substring(0, 2), name, uuid, true));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        event.setCanceled(true);
        event.message = colorMessage(message);
        event.setCanceled(true);
        UChat.chat(message);
    }

    private static List<String> getUsernames(List<String> lines) {
        List<String> usernames = new ArrayList<>();
        for (String line : lines) {
            String[] split = line.split("● ");
            if (split.length > 1) {
                for (String s : split) {
                    if (s.contains("]")) {
                        String username = s.split("]")[1];
                        if (username.startsWith(" ")) {
                            username = username.substring(1);
                        }
                        usernames.add(ChatColor.Companion.stripControlCodes(username));
                    } else {
                        String[] split2 = s.split(" §r§7");
                        if (split2.length == 1) {
                            continue;
                        }
                        String username = split2[1];
                        if (username.startsWith(" ")) {
                            username = username.substring(1);
                        }
                        usernames.add(ChatColor.Companion.stripControlCodes(username));
                    }
                }
            }
        }
        return usernames;
    }
}
