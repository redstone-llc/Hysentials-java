package main.java.cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

public class ChangeMaxHealth extends Loader {
    public ChangeMaxHealth(JSONObject actionData) {
        super(actionData, "Change Max Health");

        boolean hasMode = actionData.has("mode");
        boolean hasHealth = actionData.has("health");
        boolean healOnChange = actionData.optBoolean("healOnChange");

        if (hasHealth && !actionData.getString("health").equals("20")) {
            add(LoaderObject.click(10));
            add(LoaderObject.anvil(actionData.getString("health")));
        }

        if (hasMode && !actionData.getString("mode").equals("set")) {
            if (actionData.getString("mode").equals("increment")) {
                add(LoaderObject.click(10));
            }
            add(LoaderObject.click(11));
            if (actionData.getString("mode").equals("decrement")) {
                add(LoaderObject.click(11));
            }
            if (actionData.getString("mode").equals("multiply")) {
                add(LoaderObject.click(13));
            }
            if (actionData.getString("mode").equals("divide")) {
                add(LoaderObject.click(14));
            }
        }

        if (healOnChange) {
            add(LoaderObject.click(12));
        }
    }
}
