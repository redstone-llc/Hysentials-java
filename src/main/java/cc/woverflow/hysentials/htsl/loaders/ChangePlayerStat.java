package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class ChangePlayerStat extends Loader {

    public ChangePlayerStat(JSONObject actionData) {
        super(actionData, "Change Player Stat");

        if (actionData.has("stat")) {
            add(click(10));
            add(chat(actionData.getString("stat")));
        }

        if (actionData.has("mode") && !actionData.getString("mode").equals("increment")) {
            add(click(11));
            if (actionData.has("mode") && actionData.getString("mode").equals("decrement")) {
                add(click(12));
            }
            if (actionData.has("mode") && actionData.getString("mode").equals("set")) {
                add(click(13));
            }
            if (actionData.has("mode") && actionData.getString("mode").equals("multiply")) {
                add(click(14));
            }
            if (actionData.has("mode") && actionData.getString("mode").equals("divide")) {
                add(click(15));
            }
        }

        if (!actionData.getString("value").equals("1")) {
            add(click(12));
            add(anvil(actionData.getString("value")));
        }
    }
}
