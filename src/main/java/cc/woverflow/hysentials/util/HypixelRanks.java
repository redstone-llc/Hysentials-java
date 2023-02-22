package cc.woverflow.hysentials.util;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.woverflow.hysentials.handlers.imageicons.ImageIcon;

public enum HypixelRanks {
    //normal ranks
    DEFAULT("", "default", "§7"),
    VIP("[VIP] ", "vip", "§a"),
    VIP_PLUS("[VIP§6+§a] ", "vipplus", "§a"),
    MVP("[MVP] ", "mvp", "§b"),
    MOD("[MOD] ", "mod", "§2"),
    ADMIN("[ADMIN] ", "admin", "§c"),
    YOUTUBER("[§fYOUTUBE§c] ", "youtube", "§c"),

    //mvp
    MVP_PLUS_BLACK("[MVP§0+§b] ", "mvpplus_black", "§b"),
    MVP_PLUS_DARK_BLUE("[MVP§1+§b] ", "mvpplus_darkblue", "§b"),
    MVP_PLUS_DARK_GREEN("[MVP§2+§b] ", "mvpplus_darkgreen", "§b"),
    MVP_PLUS_DARK_AQUA("[MVP§3+§b] ", "mvpplus_darkaqua", "§b"),
    MVP_PLUS_DARK_RED("[MVP§4+§b] ", "mvpplus_darkred", "§b"),
    MVP_PLUS_DARK_PURPLE("[MVP§5+§b] ", "mvpplus_darkpurple", "§b"),
    MVP_PLUS_GOLD("[MVP§6+§b] ", "mvpplus_gold", "§b"),
    MVP_PLUS_GRAY("[MVP§8+§b] ", "mvpplus_gray", "§b"),
    MVP_PLUS_BLUE("[MVP§9+§b] ", "mvpplus_blue", "§b"),
    MVP_PLUS_GREEN("[MVP§a+§b] ", "mvpplus_green", "§b"),
    MVP_PLUS_AQUA("[MVP§b+§b] ", "mvpplus_aqua", "§b"),
    MVP_PLUS_RED("[MVP§c+§b] ", "mvpplus_red", "§b"),
    MVP_PLUS_LIGHT_PURPLE("[MVP§d+§b] ", "mvpplus_pink", "§b"),
    MVP_PLUS_YELLOW("[MVP§e+§b] ", "mvpplus_yellow", "§b"),
    MVP_PLUS_WHITE("[MVP§f+§b] ", "mvpplus_white", "§b"),

    //mvp++
    MVP_PLUS_PLUS_BLACK("[MVP§0++§6] ", "mvpplusplus_black", "§6"),
    MVP_PLUS_PLUS_DARK_BLUE("[MVP§1++§6] ", "mvpplusplus_darkblue", "§6"),
    MVP_PLUS_PLUS_DARK_GREEN("[MVP§2++§6] ", "mvpplusplus_darkgreen", "§6"),
    MVP_PLUS_PLUS_DARK_AQUA("[MVP§3++§6] ", "mvpplusplus_darkaqua", "§6"),
    MVP_PLUS_PLUS_DARK_RED("[MVP§4++§6] ", "mvpplusplus_darkred", "§6"),
    MVP_PLUS_PLUS_DARK_PURPLE("[MVP§5++§6] ", "mvpplusplus_darkpurple", "§6"),
    MVP_PLUS_PLUS_GOLD("[MVP§6++§6] ", "mvpplusplus_gold", "§6"),
    MVP_PLUS_PLUS_GRAY("[MVP§8++§6] ", "mvpplusplus_gray", "§6"),
    MVP_PLUS_PLUS_BLUE("[MVP§9++§6] ", "mvpplusplus_blue", "§6"),
    MVP_PLUS_PLUS_GREEN("[MVP§a++§6] ", "mvpplusplus_green", "§6"),
    MVP_PLUS_PLUS_RED("[MVP§c++§6] ", "mvpplusplus_red", "§6"),
    MVP_PLUS_PLUS_LIGHT_PURPLE("[MVP§d++§6] ", "mvpplusplus_pink", "§6"),
    MVP_PLUS_PLUS_YELLOW("[MVP§e++§6] ", "mvpplusplus_yellow", "§6"),
    MVP_PLUS_PLUS_WHITE("[MVP§f++§6] ", "mvpplusplus_white", "§6"),

    //aqua MVP++
    AMVP_PLUS_PLUS_BLACK("[MVP§0++§b] ", "amvpplusplus_black", "§b"),
    AMVP_PLUS_PLUS_DARK_BLUE("[MVP§1++§b] ", "amvpplusplus_darkblue", "§b"),
    AMVP_PLUS_PLUS_DARK_GREEN("[MVP§2++§b] ", "amvpplusplus_darkgreen", "§b"),
    AMVP_PLUS_PLUS_DARK_AQUA("[MVP§3++§b] ", "amvpplusplus_darkaqua", "§b"),
    AMVP_PLUS_PLUS_DARK_RED("[MVP§4++§b] ", "amvpplusplus_darkred", "§b"),
    AMVP_PLUS_PLUS_DARK_PURPLE("[MVP§5++§b] ", "amvpplusplus_darkpurple", "§b"),
    AMVP_PLUS_PLUS_GOLD("[MVP§6++§b] ", "amvpplusplus_gold", "§b"),
    AMVP_PLUS_PLUS_GRAY("[MVP§8++§b] ", "amvpplusplus_gray", "§b"),
    AMVP_PLUS_PLUS_BLUE("[MVP§9++§b] ", "amvpplusplus_blue", "§b"),
    AMVP_PLUS_PLUS_GREEN("[MVP§a++§b] ", "amvpplusplus_green", "§b"),
    AMVP_PLUS_PLUS_RED("[MVP§c++§b] ", "amvpplusplus_red", "§b"),
    AMVP_PLUS_PLUS_LIGHT_PURPLE("[MVP§d++§b] ", "amvpplusplus_pink", "§b"),
    AMVP_PLUS_PLUS_YELLOW("[MVP§e++§b] ", "amvpplusplus_yellow", "§b"),
    AMVP_PLUS_PLUS_WHITE("[MVP§f++§b] ", "amvpplusplus_white", "§b")
    ;

    private final String prefix;
    private final String iconName;
    private final String color;

    HypixelRanks(String prefix, String iconName, String color) {
        this.prefix = prefix;
        this.iconName = iconName;
        this.color = color;
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

    public String getAsPlaceholder() {
        if (ImageIcon.getIcon(iconName) == null) {
            return null;
        }
        return color + ":" + iconName + ": ";
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
