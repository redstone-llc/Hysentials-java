package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class GiveExperienceLevels extends Loader {
    public GiveExperienceLevels(String levels) {
        super( "Give Experience Levels", levels);

        if (levels != null) {
            add(click(10));
            add(anvil(levels));
        }
    }
}
