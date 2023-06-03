package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
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
