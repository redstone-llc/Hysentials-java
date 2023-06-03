package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class GiveExperienceLevels extends Loader {
    public GiveExperienceLevels(String levels) {
        super("Give Experience Levels", "xpLevel", levels);

        if (levels != null) {
            add(click(10));
            add(anvil(levels));
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
