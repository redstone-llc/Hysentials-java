package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class ChangeHealth extends Loader {
    public ChangeHealth(JSONObject actionData) {
        super(actionData, "Change Health");

        if (!Double.isNaN(actionData.getDouble("health")) && actionData.getDouble("health") != 20) {
            add(click(10));
            add(anvil(String.valueOf(actionData.getDouble("health"))));
        }

        if (actionData.has("mode") && !actionData.getString("mode").equalsIgnoreCase("set")) {
            add(click(11));
            if (actionData.getString("mode").equalsIgnoreCase("increment")) {
                add(click(10));
            }
            if (actionData.getString("mode").equalsIgnoreCase("decrement")) {
                add(click(12));
            }
            if (actionData.getString("mode").equalsIgnoreCase("multiply")) {
                add(click(13));
            }
            if (actionData.getString("mode").equalsIgnoreCase("divide")) {
                add(click(14));
            }
        }
    }
}
