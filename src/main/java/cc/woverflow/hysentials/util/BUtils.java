package cc.woverflow.hysentials.util;

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


}
