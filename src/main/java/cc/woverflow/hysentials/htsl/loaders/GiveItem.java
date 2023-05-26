package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class GiveItem extends Loader {
    public GiveItem(String item, boolean allowMultiple) {
        super("Give Item", "giveItem", item, allowMultiple);

        if (item != null) {
            add(click(10));
            add(item(item));
        }

        if (allowMultiple) {
            add(click(11));
        }
    }
}
