package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class ChangeGlobalStat extends Loader {

    public ChangeGlobalStat(String stat, String mode, String value) {
        super("Change Global Stat", "globalstat", stat, mode, value);

        if (stat != null) {
            add(click(10));
            add(chat(stat));
        }

        if (mode != null && !mode.equals("increment")) {
            add(click(11));
            if (mode.equals("decrement")) {
                add(click(11));
            }
            if (mode.equals("set")) {
                add(click(12));
            }
            if (mode.equals("multiply")) {
                add(click(13));
            }
            if (mode.equals("divide")) {
                add(click(14));
            }
        }

        if (value != null && !value.equals("1")) {
            add(click(12));
            add(anvil(value));
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        String mode = validOperator(args.get(1));
        if (mode == null) {
            compileErorrs.add("&cUnknown operator on line &e" + (index + 1) + "&c!");
            return null;
        }
        return new ChangeGlobalStat(args.get(0), mode, args.get(2));
    }

    @Override
    public String export(List<String> args) {
        return "globalstat \"" + args.get(0) + "\" " + args.get(1) + " " + args.get(2);
    }
}
