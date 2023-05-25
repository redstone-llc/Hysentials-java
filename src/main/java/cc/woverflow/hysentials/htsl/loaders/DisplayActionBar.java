package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

public class DisplayActionBar extends Loader {
    public DisplayActionBar(JSONObject actionData) {
        super(actionData, "Display Action Bar");

        if (actionData.has("message")) {
            add(LoaderObject.click(10));
            add(LoaderObject.option(actionData.getString("message")));
        }
    }
}
