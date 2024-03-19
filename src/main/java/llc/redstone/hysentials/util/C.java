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

    public static String toHex(String color, boolean includeSymbol) {
        String hex;
        switch (color) {
            case "§0":
                hex = "000000";
                break;
            case "§1":
                hex = "0000AA";
                break;
            case "§2":
                hex = "00AA00";
                break;
            case "§3":
                hex = "00AAAA";
                break;
            case "§4":
                hex = "AA0000";
                break;
            case "§5":
                hex = "AA00AA";
                break;
            case "§6":
                hex = "FFAA00";
                break;
            case "§7":
                hex = "AAAAAA";
                break;
            case "§8":
                hex = "555555";
                break;
            case "§9":
                hex = "5555FF";
                break;
            case "§a":
                hex = "55FF55";
                break;
            case "§b":
                hex = "55FFFF";
                break;
            case "§c":
                hex = "FF5555";
                break;
            case "§d":
                hex = "FF55FF";
                break;
            case "§e":
                hex = "FFFF55";
                break;
            default:
                hex = "FFFFFF";
        }
        return includeSymbol ? "#" + hex : hex;
    }

    public static String toHex(String color) {
        return toHex(color, true);
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
