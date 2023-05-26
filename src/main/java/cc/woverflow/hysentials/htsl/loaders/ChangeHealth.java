package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class ChangeHealth extends Loader {
    public ChangeHealth(Double health, String mode) {
        super("Change Health", "changeHealth", health, mode);

        if (!Double.isNaN(health) && health != 20) {
            add(click(10));
            add(anvil(String.valueOf(health)));
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
    public void load(int index, List<String> args, List<String> compileErorrs) {
        String mode = validOperator(args.get(1));
        if (mode == null) {
            compileErorrs.add("&cUnknown operator on line &e" + (index + 1) + "&c!");
            return;
        }
        new ChangeHealth(Double.parseDouble(args.get(0)), mode);
    }
}
