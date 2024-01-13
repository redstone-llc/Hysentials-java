package llc.redstone.hysentials.util;

/**
 * Taken from Wynntils under GNU Affero General Public License v3.0
 * https://github.com/Wynntils/Wynntils/blob/development/LICENSE
 * Author: Wynntils
 */
public class MathUtil {
    /**
     * Returns `value` cast as an int, and no greater than Integer.MAX_VALUE-1024.
     */
    public static int fastFloor(double value) {
        return (int) (value + 1024.0) - 1024;
    }

    public static int ceil(float value) {
        int i = (int) value;
        return value > i ? i + 1 : i;
    }

    public static int ceil(double value) {
        int i = (int) value;
        return value > i ? i + 1 : i;
    }

    /**
     * Returns the greatest integer less than or equal to the float argument.
     */
    public static int floor(float value) {
        int i = (int) value;
        return value < i ? i - 1 : i;
    }
}
