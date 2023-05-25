package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

public class DisplayActionBar extends Loader {
    public DisplayActionBar(String message) {
        super("Display Action Bar", message);

        if (message != null) {
            add(LoaderObject.click(10));
            add(LoaderObject.option(message));
        }
    }
}
