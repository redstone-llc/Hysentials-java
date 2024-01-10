package cc.woverflow.hysentials.util;

import cc.polyfrost.oneconfig.platform.Platform;

import java.util.Locale;

public class BUtils {
    public static int randomInt(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    public static boolean isInteger(String arg) {
        try {
            Integer.parseInt(arg);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isHypixelOrSBX() {
        if (!Platform.getServerPlatform().inMultiplayer()) return false;

        String serverBrand = Platform.getServerPlatform().getServerBrand();

        if (serverBrand == null) return false;

        return serverBrand.toLowerCase(Locale.ENGLISH).contains("hypixel") || serverBrand.toLowerCase(Locale.ENGLISH).contains("skyblocksandbox");
    }

    public static boolean isSBX() {
        if (!Platform.getServerPlatform().inMultiplayer()) return false;

        String serverBrand = Platform.getServerPlatform().getServerBrand();

        if (serverBrand == null) return false;

        return serverBrand.toLowerCase(Locale.ENGLISH).contains("skyblocksandbox");
    }


}
