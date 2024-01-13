package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.*;

public class GiveExperienceLevels extends Loader {
    public GiveExperienceLevels(String levels) {
        super("Give Experience Levels", "xpLevel", levels);

        if (levels != null) {
            add(LoaderObject.click(10));
            add(LoaderObject.anvil(levels));
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new GiveExperienceLevels(args.get(0));
    }

    @Override
    public String export(List<String> args) {
        return "xpLevel " + args.get(0);
    }
}
