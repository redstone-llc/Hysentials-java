package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class ChangeHealth extends Loader {
    public ChangeHealth(Double health, String mode) {
        super("Change Health");

        if (!Double.isNaN(health) && health != 20) {
            add(click(10));
            add(anvil(String.valueOf(health)));
        }

        if (mode != null && !mode.equalsIgnoreCase("set")) {
            add(click(11));
            if (mode.equalsIgnoreCase("increment")) {
                add(click(10));
            }
            if (mode.equalsIgnoreCase("decrement")) {
                add(click(12));
            }
            if (mode.equalsIgnoreCase("multiply")) {
                add(click(13));
            }
            if (mode.equalsIgnoreCase("divide")) {
                add(click(14));
            }
        }
    }
}
