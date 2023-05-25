package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

public class Cancel extends Loader {
    public Cancel(JSONObject actionData) {
        super(actionData, "Cancel Event");
    }
}
