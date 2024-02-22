//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
package llc.redstone.hysentials.util;

public class C {
    public static final String COLOR_CODE_SYMBOL = "§";
    public static final String BLACK = "§0";
    public static final String DARK_BLUE = "§1";
    public static final String DARK_GREEN = "§2";
    public static final String DARK_AQUA = "§3";
    public static final String DARK_RED = "§4";
    public static final String DARK_PURPLE = "§5";
    public static final String GOLD = "§6";
    public static final String GRAY = "§7";
    public static final String DARK_GRAY = "§8";
    public static final String BLUE = "§9";
    public static final String GREEN = "§a";
    public static final String AQUA = "§b";
    public static final String RED = "§c";
    public static final String LIGHT_PURPLE = "§d";
    public static final String YELLOW = "§e";
    public static final String WHITE = "§f";
    public static final String OBFUSCATED = "§k";
    public static final String BOLD = "§l";
    public static final String STRIKETHROUGH = "§m";
    public static final String UNDERLINE = "§n";
    public static final String ITALIC = "§o";
    public static final String RESET = "§r";

    private C() {
    }

    public static String toHex(String color) {
        switch (color) {
            case "§0":
                return "#000000";
            case "§1":
                return "#0000AA";
            case "§2":
                return "#00AA00";
            case "§3":
                return "#00AAAA";
            case "§4":
                return "#AA0000";
            case "§5":
                return "#AA00AA";
            case "§6":
                return "#FFAA00";
            case "§7":
                return "#AAAAAA";
            case "§8":
                return "#555555";
            case "§9":
                return "#5555FF";
            case "§a":
                return "#55FF55";
            case "§b":
                return "#55FFFF";
            case "§c":
                return "#FF5555";
            case "§d":
                return "#FF55FF";
            case "§e":
                return "#FFFF55";
            default:
                return "#FFFFFF";
        }
    }

    public static String translate(String text) {
        return text.replace("&", "§");
    }

    public static String removeColor(String text) {
        return text.replaceAll("§[0-9a-fA-Fk-oK-ORr]", "");
    }

    public static String removeControlCodes(String text) {
        return text.replaceAll("§[k-oK-ORr]", "");
    }
}
