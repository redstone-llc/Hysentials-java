package llc.redstone.hysentials.handlers.redworks;

import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.config.hysentialMods.FormattingConfig;
import llc.redstone.hysentials.schema.HysentialsSchema;
import llc.redstone.hysentials.util.BlockWAPIUtils;
import llc.redstone.hysentials.util.C;
import llc.redstone.hysentials.util.HypixelRanks;
import llc.redstone.hysentials.util.ScoreboardWrapper;
import llc.redstone.hysentials.websocket.Socket;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.schema.HysentialsSchema;
import llc.redstone.hysentials.util.BlockWAPIUtils;
import llc.redstone.hysentials.websocket.Socket;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BwRanksUtils {

    static HashMap<UUID, String> previousNames = new HashMap<>();

    public static String getMessage(String message, String name, UUID uuid, boolean plus, boolean checksColor) {
        String s = checkRegexes(message, name, uuid);
        if (s != null) {
            return s;
        }

        try {
            BlockWAPIUtils.Rank rank = null;
            if (Socket.cachedUsers.stream().anyMatch(u -> u.getString("uuid").equals(uuid.toString()))) {
                String r = Socket.cachedUsers.stream().filter(u -> u.getString("uuid").equals(uuid.toString())).findFirst().get().getString("rank");
                if (r != null) {
                    rank = BlockWAPIUtils.Rank.valueOf(r.toUpperCase());
                }
            } else {
                rank = BlockWAPIUtils.getRank(uuid);
            }
            String regex1 = "\\[[A-Za-z§0-9+]+] " + name;
            String regex2 = "(§r§7|§7)" + name;

            if (rank != null && rank != BlockWAPIUtils.Rank.DEFAULT) {
                String replacement = (rank.getPrefix(name) + name + (plus ? getPlus(uuid) : ""));
                if (futuristicRanks(true)) {
                    if (!checksColor) {
                        return rank.getHex() + name;
                    }
                    replacement = (rank.getPlaceholder() + name + (plus ? getPlus(uuid) : ""));
                }
                if (!checksColor) {
                    return rank.getColor() + name;
                }
                Matcher m1 = Pattern.compile(regex1, Pattern.UNICODE_CASE).matcher(message);
                if (m1.find(0)) {
                    message = message.replaceAll("\\[[A-Za-z§0-9+]+] " + name, replacement).replace("§7:", "§f:");
                } else if (Pattern.compile(regex2, Pattern.UNICODE_CASE).matcher(message.split("§7:")[0]).find(0)) {
                    message = message.replaceAll("(§r§7|§7)" + name, replacement).replace("§7:", "§f:");
                }
            } else {
                Matcher m1 = Pattern.compile(regex1, Pattern.UNICODE_CASE).matcher(message);
                Matcher m2 = Pattern.compile(regex2, Pattern.UNICODE_CASE).matcher(message);

                if (m1.find(0) && checksColor) {
                    Object[] replacement = getReplacement(m1.group(0).split(" ")[0], name, uuid, false, true);
                    HypixelRanks r = (HypixelRanks) replacement[1];
                    message = message.replaceAll("\\[[A-Za-z§0-9+]+] " + name, replacement[0].toString()).replaceAll("§[7f]: ", r.getChat() + ": ");
                }
                if (m2.find(0) && checksColor) {
                    Object[] replacement = getReplacement("§7", name, uuid, false, true);
                    HypixelRanks r = (HypixelRanks) replacement[1];
                    message = message.replaceAll("(§r§7|§7)" + name, replacement[0].toString()).replaceAll("§[7f]: ", r.getChat() + ": ");
                }
            }
        } catch (Exception e) {
        }
        return message;
    }

    public static String checkRegexes(String message, String name, UUID uuid) {
        Pattern teamsP = Pattern.compile("(§[8efacd9b]§l[SYWGRPBA]) §[8efacd9b](.+)");
        Matcher teamsM = teamsP.matcher(message.replaceAll("§r", ""));

        if (teamsM.find()) {
            switch (teamsM.group(1)) {
                case "§8§lS":
                    return ":gray: <#d9d9d9>" + teamsM.group(2);
                case "§e§lY":
                    return ":yellow: <#ebd028>" + teamsM.group(2);
                case "§a§lW":
                    return ":white: <#d9d9d9>" + teamsM.group(2);
                case "§9§lG":
                    return ":green: <#56e656>" + teamsM.group(2);
                case "§c§lR":
                    return ":red: &c" + teamsM.group(2);
                case "§d§lP":
                    return ":pink: <#e070e0>" + teamsM.group(2);
                case "§b§lB":
                    return ":blue: &9" + teamsM.group(2);
                case "§7§lA":
                    return ":aqua: <#67e9e9>" + teamsM.group(2);
            }
        }
        return null;
    }

    public static String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
        if (networkPlayerInfoIn != null) {
            String displayName = networkPlayerInfoIn.getDisplayName() != null ? networkPlayerInfoIn.getDisplayName().getFormattedText() : null;
            ScorePlayerTeam playerTeam = networkPlayerInfoIn.getPlayerTeam();
            String gameProfileName = networkPlayerInfoIn.getGameProfile() != null ? networkPlayerInfoIn.getGameProfile().getName() : null;

            if (displayName != null) {
                return displayName;
            } else {
                return ScorePlayerTeam.formatPlayerName(playerTeam, gameProfileName);
            }
        } else {
            return "";
        }
    }

    public static String removePrefix(String msg) {
        Pattern p = Pattern.compile("\\[[A-Za-z§0-9+]+] ");
        Pattern p2 = Pattern.compile("(§r§7|§7)");
        Matcher m = p.matcher(msg);
        Matcher m2 = p2.matcher(msg);
        if (m.find()) {
            msg = msg.replace(m.group(0), "");
        } else if (m2.find()) {
            msg = msg.replace(m2.group(0), "");
        }
        return C.removeColor(msg);
    }

    public static Object[] getReplacement(String match, String name, UUID uuid, boolean onlyColor, boolean tab) {
        String replacement = "";
        HypixelRanks r = null;
        for (HypixelRanks rank : HypixelRanks.values()) {
            if (onlyColor && rank.getColor().matches(match.replace("§r", ""))) {
                if (futuristicRanks(tab)) {
                    return new Object[]{rank.getNametag() + name, rank};
                } else {
                    return new Object[]{rank.getColor() + name, rank};
                }
            }
            if (rank.getPrefix().replace(" ", "").equals(match.replace("§r", ""))) {
                replacement = (rank.getPrefixReplace() + name + getPlus(uuid));
                r = rank;
                if (futuristicRanks(tab)) {
                    replacement = (rank.getAsPlaceholder() + name + getPlus(uuid));
                }
            }
        }
        return new Object[]{replacement, r};
    }

    public static String getReplace(String prefix, String name, UUID uuid) {
        BlockWAPIUtils.Rank rank = null;
        if (uuid != null) {
            if (Socket.cachedUsers.stream().anyMatch(u -> u.getString("uuid").equals(uuid.toString()))) {
                String r = Socket.cachedUsers.stream().filter(u -> u.getString("uuid").equals(uuid.toString())).findFirst().get().getString("rank");
                if (r != null) {
                    rank = BlockWAPIUtils.Rank.valueOf(r.toUpperCase());
                }
            } else {
                rank = BlockWAPIUtils.getRank(uuid);
            }
        }

        if (rank != null && rank != BlockWAPIUtils.Rank.DEFAULT) {
            String replacement = (rank.getPrefix(name));
            if (futuristicRanks(null)) {
                replacement = (rank.getPlaceholder());
            }
            return replacement;
        }

        if (prefix.equals("§7")) {
            if (futuristicRanks(null)) {
                return HypixelRanks.DEFAULT.getAsPlaceholder();
            }
            return HypixelRanks.DEFAULT.getPrefixReplace();
        } else {
            for (HypixelRanks r : HypixelRanks.values()) {
                if ((r.getColor() + r.getPrefix()).replace(" ", "").equals(prefix.replace("§r", ""))) {
                    if (futuristicRanks(null)) {
                        return r.getAsPlaceholder();
                    }
                    return r.getPrefixReplace();
                }
            }
        }
        return prefix;
    }

    public static boolean futuristicRanks(Boolean tab) {
        if (tab == null) {
            return Hysentials.INSTANCE.getConfig().formattingConfig.enabled && FormattingConfig.futuristicRanks;
        }
        return Hysentials.INSTANCE.getConfig().formattingConfig.enabled && FormattingConfig.futuristicRanks && (tab ? FormattingConfig.fancyRankInTab : FormattingConfig.fancyRankInChat);
    }

    public static String getPlus(UUID id) {
        HysentialsSchema.User user = Socket.cachedUsersNew.get(id.toString());
        if (user == null) return "";
        return user.getHasPlus() ? " §6[+]§r" : "";
    }
}
