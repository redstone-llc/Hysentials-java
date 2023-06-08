package cc.woverflow.hysentials.handlers.chat.modules.bwranks;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.handlers.chat.ChatReceiveModule;
import cc.woverflow.hysentials.handlers.redworks.BwRanks;
import cc.woverflow.hysentials.util.BlockWAPIUtils;
import cc.woverflow.hysentials.util.HypixelAPIUtils;
import cc.woverflow.hysentials.util.HypixelRanks;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.Sys;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cc.woverflow.hysentials.handlers.redworks.BwRanksUtils.*;

public class BWSReplace implements ChatReceiveModule {
    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        if (!HypixelUtils.INSTANCE.isHypixel()) return;
        if (event.type != 0 && event.type != 1) return;
        if (LocrawUtil.INSTANCE.isInGame()) return;
        boolean didSomething = false;
        String message = event.message.getFormattedText();
        HashMap<String, UUID> users = new HashMap<>();

        Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().forEach(playerInfo -> {
            users.put(playerInfo.getGameProfile().getName(), playerInfo.getGameProfile().getId());
        });
        for (Map.Entry<String, UUID> user : users.entrySet()) {
            String name = user.getKey();
            UUID uuid = user.getValue();

            if (message.contains(name)) {
                if (checkRegexes(event)) {
                    event.setCanceled(true);
                    return;
                }
            }
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
                        didSomething = true;
                        message = message.replaceAll("\\[[A-Za-z§0-9+]+] " + name, replacement).replaceAll("§[7f]:", rank.getChat() + ":");
                    } else if (Pattern.compile(regex2).matcher(message.split("§7:")[0]).find(0)) {
                        didSomething = true;
                        message = message.replaceAll("(§r§7|§7)" + name, replacement).replaceAll("§[7f]:", rank.getChat() + ":");
                    } else if (Pattern.compile(regex3).matcher(message).find(0)) {
                        didSomething = true;
                        message = message.replaceAll("[a-f0-9§]{2}" + name, replacement).replaceAll("§[7f]:", rank.getChat() + ":");
                    }
                } else {
                    Matcher m1 = Pattern.compile(regex1).matcher(message);
                    Matcher m2 = Pattern.compile(regex2).matcher(message);
                    Matcher m3 = Pattern.compile(regex3).matcher(message);
                    if (m1.find(0) && BwRanks.hasRank) {
                        didSomething = true;
                        Object[] replacement = getReplacement(m1.group(0).split(" ")[0], name, uuid, false);
                        HypixelRanks r = (HypixelRanks) replacement[1];
                        message = message.replaceAll("\\[[A-Za-z§0-9+]+] " + name, replacement[0].toString()).replaceAll("§[7f]:", r.getChat() + ":");
                    }
                    if (m2.find(0) && BwRanks.hasRank) {
                        didSomething = true;
                        Object[] replacement = getReplacement("§7", name, uuid, LocrawUtil.INSTANCE.getLocrawInfo().getGameType().equals(LocrawInfo.GameType.SKYBLOCK));
                        HypixelRanks r = (HypixelRanks) replacement[1];
                        message = message.replaceAll("(§r§7|§7)" + name, replacement[0].toString()).replaceAll("§[7f]:", r.getChat() + ":");
                    }
                    if (m3.find(0) && (!BwRanks.hasRank)) {
                        didSomething = true;
                        Object[] replacement = getReplacement(m3.group(0).substring(0, 2), name, uuid, true);
                        HypixelRanks r = (HypixelRanks) replacement[1];
                        message = message.replaceAll("[a-f0-9§]{2}" + name, replacement[0].toString()).replaceAll("§[7f]:", r.getChat() + ":");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (didSomething) {
            event.setCanceled(true);
            UChat.chat(message);
        }
    }


    public boolean checkRegexes(ClientChatReceivedEvent event) {
        Pattern partyPattern = Pattern.compile("§9Party §8> (§[0-9a-fk-or].+ |§[0-9a-fk-or])(.+)§[f7]: (.+)");

        Matcher partyMatcher = partyPattern.matcher(event.message.getFormattedText().replaceAll("§r", ""));
        if (partyMatcher.find()) {
            Multithreading.runAsync(() -> {
                try {
                    String name = partyMatcher.group(2);
                    String prefix = partyMatcher.group(1);
                    prefix = prefix;
                    String message = partyMatcher.group(3);
                    UUID uuid = HypixelAPIUtils.getUUIDpdb(name);
                    Object[] replacement = getStuff(prefix + name, name, uuid, true, true);
                    String chat = (replacement[1] instanceof HypixelRanks) ? ((HypixelRanks) replacement[1]).getChat() : ((BlockWAPIUtils.Rank) replacement[1]).getChat();
                    event.setCanceled(true);
//                    UChat.chat(":party: " + replacement[0].toString() + chat + ": " + message);
                    UChat.chat(":party: &9" + name + "<#c0def5>" + ": " + message);
                } catch (Exception e) {
                    System.out.println("Error in party chat\n" + e.getMessage());
                }
            });
            return true;
        }

        Pattern guildPattern = Pattern.compile("§2Guild > (§[0-9a-fk-or].+ |§[0-9a-fk-or])(.+)()§[f7]: (.+)");
        Pattern guildPattern2 = Pattern.compile("§2Guild > (§[0-9a-fk-or].+ |§[0-9a-fk-or])(.+)( §7.+)§[f7]: (.+)");
        Matcher guildMatcher = guildPattern.matcher(event.message.getFormattedText().replaceAll("§r", ""));
        Matcher guildMatcher2 = guildPattern2.matcher(event.message.getFormattedText().replaceAll("§r", ""));
        boolean guildMatch = guildMatcher.find();
        boolean guildMatch2 = guildMatcher2.find();
        if (guildMatch2 || guildMatch) {
            Matcher m = guildMatch2 ? guildMatcher2 : guildMatcher;
            Multithreading.runAsync(() -> {
                try {
                    String name = m.group(2);
                    String prefix = m.group(1);
                    prefix = prefix;
                    String message = m.group(4);
                    UUID uuid = HypixelAPIUtils.getUUIDpdb(name);
                    Object[] replacement = getStuff(prefix + name, name, uuid, true, true);
                    String chat = (replacement[1] instanceof HypixelRanks) ? ((HypixelRanks) replacement[1]).getChat() : ((BlockWAPIUtils.Rank) replacement[1]).getChat();
                    event.setCanceled(true);
//                    UChat.chat(":guild: " + replacement[0].toString() + chat + ": " + message);
                    UChat.chat(":guild: &2" + name + "<#c6f5c0>" + ": " + message);
                } catch (Exception e) {
                    System.out.println("Error in guild chat\n" + e.getMessage());
                    e.printStackTrace();
                }
            });
            return true;
        }
        return false;
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
