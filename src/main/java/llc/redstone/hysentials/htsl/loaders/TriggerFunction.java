package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

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

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new TriggerFunction(args.get(0), Boolean.parseBoolean(args.get(1)));
    }

    @Override
    public String export(List<String> args) {
        return "function \"" + args.get(0) + "\" " + args.get(1);
    }
}
