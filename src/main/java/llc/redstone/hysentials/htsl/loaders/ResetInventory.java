package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.*;

public class ResetInventory extends Loader {
    public ResetInventory() {
        super( "Reset Inventory", "resetInventory");
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new ResetInventory();
    }
}
