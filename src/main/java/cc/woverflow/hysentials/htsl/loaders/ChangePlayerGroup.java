package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class ChangePlayerGroup extends Loader {
    public ChangePlayerGroup(JSONObject actionData) {
        super(actionData, "Change Player's Group");

        if (actionData.has("group")) {
            add(click(10));
            add(option(actionData.getString("group")));
        }

        if (actionData.getBoolean("demotionProtection")) {
            add(click(11));
        }
    }
}
