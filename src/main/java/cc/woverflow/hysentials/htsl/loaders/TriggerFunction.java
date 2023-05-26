package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

public class TriggerFunction extends Loader {
    public TriggerFunction(String function, boolean triggerForAllPlayers) {
        super("Trigger Function", "function", function, triggerForAllPlayers);

        if (function != null) {
            add(LoaderObject.click(10));
            add(LoaderObject.option(function));
        }

        if (triggerForAllPlayers) {
            add(LoaderObject.click(11));
        }
    }
}
