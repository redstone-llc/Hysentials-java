package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class RemoveItem extends Loader {
    public RemoveItem(String item) {
        super("Remove Item", "removeItem", item);

        if (item != null) {
            add(click(10));
            add(item(item));
        }
    }
}
