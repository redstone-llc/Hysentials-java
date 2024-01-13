package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

public class ClearAllPotionEffects extends Loader {
    public ClearAllPotionEffects() {
        super("Clear All Potion Effects", "clearEffects");
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new ClearAllPotionEffects();
    }
}
