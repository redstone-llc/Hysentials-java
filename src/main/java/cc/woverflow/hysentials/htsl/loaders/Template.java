package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;
public class Template extends Loader {
    public Template(JSONObject actionData) {
        super(actionData, "Template");
    }
}
