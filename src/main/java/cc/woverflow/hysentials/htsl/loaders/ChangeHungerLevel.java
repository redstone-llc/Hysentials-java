package main.java.cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

public class ChangeHungerLevel extends Loader {
    public ChangeHungerLevel(JSONObject actionData) {
        super(actionData, "Change Hunger Level");

        boolean hasMode = actionData.has("mode");
        boolean hasLevel = actionData.has("level");

        if (hasLevel && !actionData.getString("level").equals("20")) {
            add(LoaderObject.click(10));
            add(LoaderObject.anvil(actionData.getString("level")));
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
    }
}
