package main.java.cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

public class UseRemoveHeldItem extends Loader {
    public UseRemoveHeldItem(JSONObject actionData) {
        super(actionData, "Use/Remove Held Item");
    }
}
