package llc.redstone.hysentials.util;

import llc.redstone.hysentials.config.hysentialmods.FormattingConfig;
import llc.redstone.hysentials.config.hysentialmods.rank.RankStuff;
import llc.redstone.hysentials.handlers.imageicons.ImageIcon;
import llc.redstone.hysentials.handlers.redworks.BwRanksUtils;

public enum HypixelRanks {
    //normal ranks
    DEFAULT("§7", "default", "§7", "default"),
    VIP("[VIP] ", "vip", "§a", "vip"),
    VIP_PLUS("[VIP§6+§a] ", "vipplus", "§a", "vip"),
    MVP("[MVP] ", "mvp", "§b", "bluemvp"),
    MOD("[MOD] ", "mod", "§2", "mod"),
    HELPER("[HELPER] ", "helper", "§9", "helper"),
    ADMIN("[ADMIN] ", "admin", "§c", "admin"),
    YOUTUBER("[§fYOUTUBE§c] ", "youtube", "§c", "youtube"),
    YT("[§fYT§c] ", "youtube", "§c", "youtube"),
    NPC("[NPC] ", "npc", "§8", "npc"),

    //mvp
    MVP_PLUS_BLACK("[MVP§0+§b] ", "mvpplus_black", "§b", "bluemvp"),
    MVP_PLUS_DARK_BLUE("[MVP§1+§b] ", "mvpplus_darkblue", "§b", "bluemvp"),
    MVP_PLUS_DARK_GREEN("[MVP§2+§b] ", "mvpplus_darkgreen", "§b", "bluemvp"),
    MVP_PLUS_DARK_AQUA("[MVP§3+§b] ", "mvpplus_darkaqua", "§b", "bluemvp"),
    MVP_PLUS_DARK_RED("[MVP§4+§b] ", "mvpplus_darkred", "§b", "bluemvp"),
    MVP_PLUS_DARK_PURPLE("[MVP§5+§b] ", "mvpplus_darkpurple", "§b", "bluemvp"),
    MVP_PLUS_GOLD("[MVP§6+§b] ", "mvpplus_gold", "§b", "bluemvp"),
    MVP_PLUS_GRAY("[MVP§8+§b] ", "mvpplus_gray", "§b", "bluemvp"),
    MVP_PLUS_BLUE("[MVP§9+§b] ", "mvpplus_blue", "§b", "bluemvp"),
    MVP_PLUS_GREEN("[MVP§a+§b] ", "mvpplus_green", "§b", "bluemvp"),
    MVP_PLUS_AQUA("[MVP§b+§b] ", "mvpplus_aqua", "§b", "bluemvp"),
    MVP_PLUS_RED("[MVP§c+§b] ", "mvpplus_red", "§b", "bluemvp"),
    MVP_PLUS_LIGHT_PURPLE("[MVP§d+§b] ", "mvpplus_pink", "§b", "bluemvp"),
    MVP_PLUS_YELLOW("[MVP§e+§b] ", "mvpplus_yellow", "§b", "bluemvp"),
    MVP_PLUS_WHITE("[MVP§f+§b] ", "mvpplus_white", "§b", "bluemvp"),

    //mvp++
    MVP_PLUS_PLUS_BLACK("[MVP§0++§6] ", "mvpplusplus_black", "§6", "goldmvp"),
    MVP_PLUS_PLUS_DARK_BLUE("[MVP§1++§6] ", "mvpplusplus_darkblue", "§6", "goldmvp"),
    MVP_PLUS_PLUS_DARK_GREEN("[MVP§2++§6] ", "mvpplusplus_darkgreen", "§6", "goldmvp"),
    MVP_PLUS_PLUS_DARK_AQUA("[MVP§3++§6] ", "mvpplusplus_darkaqua", "§6", "goldmvp"),
    MVP_PLUS_PLUS_DARK_RED("[MVP§4++§6] ", "mvpplusplus_darkred", "§6", "goldmvp"),
    MVP_PLUS_PLUS_DARK_PURPLE("[MVP§5++§6] ", "mvpplusplus_darkpurple", "§6", "goldmvp"),
    MVP_PLUS_PLUS_GOLD("[MVP§6++§6] ", "mvpplusplus_gold", "§6", "goldmvp"),
    MVP_PLUS_PLUS_GRAY("[MVP§8++§6] ", "mvpplusplus_gray", "§6", "goldmvp"),
    MVP_PLUS_PLUS_BLUE("[MVP§9++§6] ", "mvpplusplus_blue", "§6", "goldmvp"),
    MVP_PLUS_PLUS_GREEN("[MVP§a++§6] ", "mvpplusplus_green", "§6", "goldmvp"),
    MVP_PLUS_PLUS_RED("[MVP§c++§6] ", "mvpplusplus_red", "§6", "goldmvp"),
    MVP_PLUS_PLUS_LIGHT_PURPLE("[MVP§d++§6] ", "mvpplusplus_pink", "§6", "goldmvp"),
    MVP_PLUS_PLUS_YELLOW("[MVP§e++§6] ", "mvpplusplus_yellow", "§6", "goldmvp"),
    MVP_PLUS_PLUS_WHITE("[MVP§f++§6] ", "mvpplusplus_white", "§6", "goldmvp"),

    //aqua MVP++
    AMVP_PLUS_PLUS_BLACK("[MVP§0++§b] ", "amvpplusplus_black", "§b", "bluemvp"),
    AMVP_PLUS_PLUS_DARK_BLUE("[MVP§1++§b] ", "amvpplusplus_darkblue", "§b", "bluemvp"),
    AMVP_PLUS_PLUS_DARK_GREEN("[MVP§2++§b] ", "amvpplusplus_darkgreen", "§b", "bluemvp"),
    AMVP_PLUS_PLUS_DARK_AQUA("[MVP§3++§b] ", "amvpplusplus_darkaqua", "§b", "bluemvp"),
    AMVP_PLUS_PLUS_DARK_RED("[MVP§4++§b] ", "amvpplusplus_darkred", "§b", "bluemvp"),
    AMVP_PLUS_PLUS_DARK_PURPLE("[MVP§5++§b] ", "amvpplusplus_darkpurple", "§b", "bluemvp"),
    AMVP_PLUS_PLUS_GOLD("[MVP§6++§b] ", "amvpplusplus_gold", "§b", "bluemvp"),
    AMVP_PLUS_PLUS_GRAY("[MVP§8++§b] ", "amvpplusplus_gray", "§b", "bluemvp"),
    AMVP_PLUS_PLUS_BLUE("[MVP§9++§b] ", "amvpplusplus_blue", "§b", "bluemvp"),
    AMVP_PLUS_PLUS_GREEN("[MVP§a++§b] ", "amvpplusplus_green", "§b", "bluemvp"),
    AMVP_PLUS_PLUS_RED("[MVP§c++§b] ", "amvpplusplus_red", "§b", "bluemvp"),
    AMVP_PLUS_PLUS_LIGHT_PURPLE("[MVP§d++§b] ", "amvpplusplus_pink", "§b", "bluemvp"),
    AMVP_PLUS_PLUS_YELLOW("[MVP§e++§b] ", "amvpplusplus_yellow", "§b", "bluemvp"),
    AMVP_PLUS_PLUS_WHITE("[MVP§f++§b] ", "amvpplusplus_white", "§b", "bluemvp"),
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

    public String getPrefixReplace() {
        if (this.equals(DEFAULT)) {
            return prefix;
        }
        return color + prefix;
    }

    public String getIconName() {
        return iconName;
    }

    public String getColor() {
        return color;
    }

    public String getNametag() {
        if (BwRanksUtils.futuristicRanks(true)) {
            RankStuff rank = FormattingConfig.getRank(hex);
            return "<#" + rank.nametagColor.getHex() + ">";
        } else {
            return color;
        }
    }

    public String getChat() {
        if (BwRanksUtils.futuristicRanks(false)) {
            RankStuff rank = FormattingConfig.getRank(hex);
            return "<#" + rank.chatMessageColor.getHex() + ">";
        } else {
            return (this.equals(DEFAULT) ? "§7" : "§f");
        }
    }

    public String getAsPlaceholder() {
        if (ImageIcon.getIcon(iconName) == null) {
            return null;
        }
        return "§f:" + iconName + ": " + getNametag();
    }
}
