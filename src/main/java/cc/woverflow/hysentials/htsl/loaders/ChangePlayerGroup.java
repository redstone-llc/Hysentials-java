package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class ChangePlayerGroup extends Loader {
    public ChangePlayerGroup(String group, boolean demotionProtection) {
        super("Change Player's Group", group, demotionProtection);

        if (group != null) {
            add(click(10));
            add(option(group));
        }

        if (demotionProtection) {
            add(click(11));
        }
    }
}
