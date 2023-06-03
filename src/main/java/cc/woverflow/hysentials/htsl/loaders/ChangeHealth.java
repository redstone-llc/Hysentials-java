package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;
import scala.Int;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class ChangeHealth extends Loader {
    public ChangeHealth(String health, String mode) {
        super("Change Health", "changeHealth", health, mode);

        if (!isNAN(health) && !health.equalsIgnoreCase("20")) {
            add(click(10));
            add(anvil(health));
        }

        if (mode != null && !mode.equalsIgnoreCase("set")) {
            add(click(11));
            if (mode.equalsIgnoreCase("increment")) {
                add(click(10));
            }
            if (mode.equalsIgnoreCase("decrement")) {
                add(click(12));
            }
            if (mode.equalsIgnoreCase("multiply")) {
                add(click(13));
            }
            if (mode.equalsIgnoreCase("divide")) {
                add(click(14));
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
        return new ChangeHealth(args.get(1), mode);
    }

    @Override
    public String export(List<String> args) {
        return "changeHealth " + args.get(1) + " " + args.get(0);
    }
}
