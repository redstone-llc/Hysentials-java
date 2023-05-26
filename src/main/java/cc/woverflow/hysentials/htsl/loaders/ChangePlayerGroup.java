package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class ChangePlayerGroup extends Loader {
    public ChangePlayerGroup(String group, boolean demotionProtection) {
        super("Change Player's Group", "changePlayerGroup", group, demotionProtection);

        if (group != null) {
            add(click(10));
            add(option(group));
        }

        if (demotionProtection) {
            add(click(11));
        }
    }

    @Override
    public void load(int index, List<String> args, List<String> compileErorrs) {
        new ChangePlayerGroup(args.get(0), Boolean.parseBoolean(args.get(1)));
    }
}
