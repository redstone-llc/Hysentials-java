package main.java.cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

public class TriggerFunction extends Loader {
    public TriggerFunction(JSONObject actionData) {
        super(actionData, "Trigger Function");

        if (actionData.has("function")) {
            add(LoaderObject.click(10));
            add(LoaderObject.option(actionData.getString("function")));
        }

        if (actionData.optBoolean("triggerForAllPlayers")) {
            add(LoaderObject.click(11));
        }
    }
}
