package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

public class UseRemoveHeldItem extends Loader {
    public UseRemoveHeldItem() {
        super( "Use/Remove Held Item", "consumeItem");
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new UseRemoveHeldItem();
    }
}
