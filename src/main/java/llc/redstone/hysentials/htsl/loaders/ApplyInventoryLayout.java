package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.*;

public class ApplyInventoryLayout extends Loader {

    public ApplyInventoryLayout(String layout) {
        super("Apply Inventory Layout", "applyLayout", layout);

        if (layout != null) {
            add(LoaderObject.click(10));
            add(LoaderObject.option(layout));
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> errors) {
        return new ApplyInventoryLayout(args.get(0));
    }

    @Override
    public String export(List<String> args) {
        return "applyLayout \"" + args.get(0) + "\"";
    }
}