package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class GiveItem extends Loader {
    public GiveItem(String item, boolean allowMultiple) {
        super("Give Item", "giveItem", item, allowMultiple);

        if (item != null) {
            add(click(10));
            add(item(item));
        }

        if (allowMultiple) {
            add(click(11));
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new GiveItem(args.get(0), Boolean.parseBoolean(args.get(1)));

    }

    @Override
    public String export(List<String> args) {
        return "giveItem \"" + args.get(0) + "\" " + args.get(1);
    }
}
