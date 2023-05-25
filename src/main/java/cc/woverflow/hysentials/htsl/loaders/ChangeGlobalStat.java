package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class ChangeGlobalStat extends Loader {

    public ChangeGlobalStat(String stat, String mode, String value) {
        super("Change Global Stat", stat, mode, value);

        if (stat != null) {
            add(click(10));
            add(chat(stat));
        }

        if (mode != null && !mode.equals("increment")) {
            add(click(11));
            if (mode.equals("decrement")) {
                add(click(12));
            }
            if (mode.equals("set")) {
                add(click(13));
            }
            if (mode.equals("multiply")) {
                add(click(14));
            }
            if (mode.equals("divide")) {
                add(click(15));
            }
        }

        if (!value.equals("1")) {
            add(click(12));
            add(anvil(value));
        }
    }
}
