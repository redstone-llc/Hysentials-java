package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

public class DisplayActionBar extends Loader {
    public DisplayActionBar(String message) {
        super("Display Action Bar", "actionBar", message);

        if (message != null) {
            add(LoaderObject.click(10));
            add(LoaderObject.option(message));
        }
    }

    @Override
    public void load(int index, List<String> args, List<String> compileErorrs) {
        new DisplayActionBar(args.get(0));
    }
}
