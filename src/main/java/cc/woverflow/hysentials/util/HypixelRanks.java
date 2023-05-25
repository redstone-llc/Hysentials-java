package cc.woverflow.hysentials.util;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.woverflow.hysentials.handlers.imageicons.ImageIcon;

public enum HypixelRanks {
    //normal ranks
    DEFAULT("§7", "default", "§7", "#7a8182"),
    VIP("[VIP] ", "vip", "§a", "#85cd17"),
    VIP_PLUS("[VIP§6+§a] ", "vipplus", "§a", "#85cd17"),
    MVP("[MVP] ", "mvp", "§b", "#0fa7e9"),
    MOD("[MOD] ", "mod", "§2"),
    ADMIN("[ADMIN] ", "admin", "§c", "#ff2f2e"),
    YOUTUBER("[§fYOUTUBE§c] ", "youtube", "§c", "#ff2f2e"),
    NPC("[NPC] ", "npc", "§8", "#555555"),

    //mvp
    MVP_PLUS_BLACK("[MVP§0+§b] ", "mvpplus_black", "§b", "#0fa7e9"),
    MVP_PLUS_DARK_BLUE("[MVP§1+§b] ", "mvpplus_darkblue", "§b", "#0fa7e9"),
    MVP_PLUS_DARK_GREEN("[MVP§2+§b] ", "mvpplus_darkgreen", "§b", "#0fa7e9"),
    MVP_PLUS_DARK_AQUA("[MVP§3+§b] ", "mvpplus_darkaqua", "§b", "#0fa7e9"),
    MVP_PLUS_DARK_RED("[MVP§4+§b] ", "mvpplus_darkred", "§b", "#0fa7e9"),
    MVP_PLUS_DARK_PURPLE("[MVP§5+§b] ", "mvpplus_darkpurple", "§b", "#0fa7e9"),
    MVP_PLUS_GOLD("[MVP§6+§b] ", "mvpplus_gold", "§b", "#0fa7e9"),
    MVP_PLUS_GRAY("[MVP§8+§b] ", "mvpplus_gray", "§b", "#0fa7e9"),
    MVP_PLUS_BLUE("[MVP§9+§b] ", "mvpplus_blue", "§b", "#0fa7e9"),
    MVP_PLUS_GREEN("[MVP§a+§b] ", "mvpplus_green", "§b", "#0fa7e9"),
    MVP_PLUS_AQUA("[MVP§b+§b] ", "mvpplus_aqua", "§b", "#0fa7e9"),
    MVP_PLUS_RED("[MVP§c+§b] ", "mvpplus_red", "§b", "#0fa7e9"),
    MVP_PLUS_LIGHT_PURPLE("[MVP§d+§b] ", "mvpplus_pink", "§b", "#0fa7e9"),
    MVP_PLUS_YELLOW("[MVP§e+§b] ", "mvpplus_yellow", "§b", "#0fa7e9"),
    MVP_PLUS_WHITE("[MVP§f+§b] ", "mvpplus_white", "§b", "#0fa7e9"),

    //mvp++
    MVP_PLUS_PLUS_BLACK("[MVP§0++§6] ", "mvpplusplus_black", "§6", "#e8b208"),
    MVP_PLUS_PLUS_DARK_BLUE("[MVP§1++§6] ", "mvpplusplus_darkblue", "§6", "#e8b208"),
    MVP_PLUS_PLUS_DARK_GREEN("[MVP§2++§6] ", "mvpplusplus_darkgreen", "§6", "#e8b208"),
    MVP_PLUS_PLUS_DARK_AQUA("[MVP§3++§6] ", "mvpplusplus_darkaqua", "§6", "#e8b208"),
    MVP_PLUS_PLUS_DARK_RED("[MVP§4++§6] ", "mvpplusplus_darkred", "§6", "#e8b208"),
    MVP_PLUS_PLUS_DARK_PURPLE("[MVP§5++§6] ", "mvpplusplus_darkpurple", "§6", "#e8b208"),
    MVP_PLUS_PLUS_GOLD("[MVP§6++§6] ", "mvpplusplus_gold", "§6", "#e8b208"),
    MVP_PLUS_PLUS_GRAY("[MVP§8++§6] ", "mvpplusplus_gray", "§6", "#e8b208"),
    MVP_PLUS_PLUS_BLUE("[MVP§9++§6] ", "mvpplusplus_blue", "§6", "#e8b208"),
    MVP_PLUS_PLUS_GREEN("[MVP§a++§6] ", "mvpplusplus_green", "§6", "#e8b208"),
    MVP_PLUS_PLUS_RED("[MVP§c++§6] ", "mvpplusplus_red", "§6", "#e8b208"),
    MVP_PLUS_PLUS_LIGHT_PURPLE("[MVP§d++§6] ", "mvpplusplus_pink", "§6", "#e8b208"),
    MVP_PLUS_PLUS_YELLOW("[MVP§e++§6] ", "mvpplusplus_yellow", "§6", "#e8b208"),
    MVP_PLUS_PLUS_WHITE("[MVP§f++§6] ", "mvpplusplus_white", "§6", "#e8b208"),

    //aqua MVP++
    AMVP_PLUS_PLUS_BLACK("[MVP§0++§b] ", "amvpplusplus_black", "§b", "#0fa7e9"),
    AMVP_PLUS_PLUS_DARK_BLUE("[MVP§1++§b] ", "amvpplusplus_darkblue", "§b", "#0fa7e9"),
    AMVP_PLUS_PLUS_DARK_GREEN("[MVP§2++§b] ", "amvpplusplus_darkgreen", "§b", "#0fa7e9"),
    AMVP_PLUS_PLUS_DARK_AQUA("[MVP§3++§b] ", "amvpplusplus_darkaqua", "§b", "#0fa7e9"),
    AMVP_PLUS_PLUS_DARK_RED("[MVP§4++§b] ", "amvpplusplus_darkred", "§b", "#0fa7e9"),
    AMVP_PLUS_PLUS_DARK_PURPLE("[MVP§5++§b] ", "amvpplusplus_darkpurple", "§b", "#0fa7e9"),
    AMVP_PLUS_PLUS_GOLD("[MVP§6++§b] ", "amvpplusplus_gold", "§b", "#0fa7e9"),
    AMVP_PLUS_PLUS_GRAY("[MVP§8++§b] ", "amvpplusplus_gray", "§b", "#0fa7e9"),
    AMVP_PLUS_PLUS_BLUE("[MVP§9++§b] ", "amvpplusplus_blue", "§b", "#0fa7e9"),
    AMVP_PLUS_PLUS_GREEN("[MVP§a++§b] ", "amvpplusplus_green", "§b", "#0fa7e9"),
    AMVP_PLUS_PLUS_RED("[MVP§c++§b] ", "amvpplusplus_red", "§b", "#0fa7e9"),
    AMVP_PLUS_PLUS_LIGHT_PURPLE("[MVP§d++§b] ", "amvpplusplus_pink", "§b", "#0fa7e9"),
    AMVP_PLUS_PLUS_YELLOW("[MVP§e++§b] ", "amvpplusplus_yellow", "§b", "#0fa7e9"),
    AMVP_PLUS_PLUS_WHITE("[MVP§f++§b] ", "amvpplusplus_white", "§b", "#0fa7e9"),
    ;

    private final String prefix;
    private final String iconName;
    private final String color;
    private final String hex;

    HypixelRanks(String prefix, String iconName, String color) {
        this.prefix = prefix;
        this.iconName = iconName;
        this.color = color;
        this.hex = null;
    }

    HypixelRanks(String prefix, String iconName, String color, String hex) {
        this.prefix = prefix;
        this.iconName = iconName;
        this.color = color;
        this.hex = hex;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getIconName() {
        return iconName;
    }

    public String getColor() {
        return color;
    }

    public String getHex() {
        return "<" + hex + ">";
    }

    public String getAsPlaceholder() {
        if (ImageIcon.getIcon(iconName) == null) {
            return null;
        }
        return "§f:" + iconName + ": " + "<" + hex + ">";
    }

    public enum RankColors {
        BLACK("§0"),
        DARK_BLUE("§1"),
        DARK_GREEN("§2"),
        DARK_AQUA("§3"),
        DARK_RED("§4"),
        DARK_PURPLE("§5"),
        GOLD("§6"),
        GRAY("§7"),
        DARK_GRAY("§8"),
        BLUE("§9"),
        GREEN("§a"),
        AQUA("§b"),
        RED("§c"),
        LIGHT_PURPLE("§d"),
        YELLOW("§e"),
        WHITE("§f");

        private final String color;
        RankColors(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }

        public static RankColors getPlusColor(String match) {
            String noB = match.replace("[", "").replace("]", "").replace(" ", "");
            String noColor = ChatColor.Companion.stripColorCodes(noB);
            if (noColor == null) return null;

            if (noColor.equals("MVP++") || noColor.equals("MVP+")) {
                //get the color before the + sign
                String color = noB.substring(noB.indexOf("+") - 2, noB.indexOf("+"));
                for (RankColors rankColor : values()) {
                    if (rankColor.getColor().equals(color)) {
                        return rankColor;
                    }
                }
            }
            return null;
        }
    }
}
