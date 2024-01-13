package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

public class ChangeHungerLevel extends Loader {
    public ChangeHungerLevel(String mode, String level) {
        super("Change Hunger Level", "hungerLevel", mode, level);

        if (level != null && !level.equals("20")) {
            add(LoaderObject.click(10));
            add(LoaderObject.anvil(level));
        }

        if (mode != null && !mode.equals("set")) {
            if (mode.equals("increment")) {
                add(LoaderObject.click(10));
            }
            add(LoaderObject.click(11));
            if (mode.equals("decrement")) {
                add(LoaderObject.click(11));
            }
            if (mode.equals("multiply")) {
                add(LoaderObject.click(13));
            }
            if (mode.equals("divide")) {
                add(LoaderObject.click(14));
            }
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        String mode = validOperator(args.get(0));
        if (mode == null) {
            compileErorrs.add("&cUnknown operator on line &e" + (index + 1) + "&c!");
            return null;
        }
        return new ChangeHungerLevel(mode, args.get(1));
    }

    @Override
    public String export(List<String> args) {
        return "hungerLevel " + args.get(0) + " " + args.get(1);
    }
}
