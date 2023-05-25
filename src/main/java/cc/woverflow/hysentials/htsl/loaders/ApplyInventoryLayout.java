package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class ApplyInventoryLayout extends Loader {

    public ApplyInventoryLayout(JSONObject actionData) {
        super(actionData, "Apply Inventory Layout");

        add(click(10));
        add(option(actionData.getString("layout")));
    }
}
