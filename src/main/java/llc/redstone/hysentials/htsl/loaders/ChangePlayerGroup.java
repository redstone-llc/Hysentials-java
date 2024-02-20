package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.*;

public class ChangePlayerGroup extends Loader {
    public ChangePlayerGroup(String group, boolean demotionProtection) {
        super("Change Player's Group", "changePlayerGroup", group, demotionProtection);

        if (group != null) {
            add(LoaderObject.click(10));
            add(LoaderObject.option(group));
        }

        if (demotionProtection) {
            add(LoaderObject.click(11));
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new ChangePlayerGroup(args.get(0), Boolean.parseBoolean(args.get(1)));
    }

    @Override
    public String export(List<String> args) {
        return "changePlayerGroup \"" + args.get(0) + "\" " + args.get(1);
    }
}