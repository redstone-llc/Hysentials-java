package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.*;

public class RemoveItem extends Loader {
    public RemoveItem(String item) {
        super("Remove Item", "removeItem", item);

        if (item != null) {
            add(LoaderObject.click(10));
            add(LoaderObject.item(item));
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new RemoveItem(args.get(0));
    }

    @Override
    public String export(List<String> args) {
        return "removeItem '" + args.get(0) + "'";
    }
}
