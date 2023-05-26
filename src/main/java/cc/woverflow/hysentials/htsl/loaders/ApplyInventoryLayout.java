package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class ApplyInventoryLayout extends Loader {

    public ApplyInventoryLayout(String layout) {
        super("Apply Inventory Layout", "applyLayout", layout);

        if (layout != null) {
            add(click(10));
            add(option(layout));
        }
    }

    @Override
    public void load(int index, List<String> args, List<String> errors) {
        new ApplyInventoryLayout(args.get(0));
    }
}
