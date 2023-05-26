package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

public class ChangeMaxHealth extends Loader {
    public ChangeMaxHealth(String mode, String health, boolean healOnChange) {
        super("Change Max Health", "maxHealth", mode, health, healOnChange);

        if (health != null && !health.equals("20")) {
            add(LoaderObject.click(10));
            add(LoaderObject.anvil(health));
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

        if (healOnChange) {
            add(LoaderObject.click(12));
        }
    }

    @Override
    public void load(int index, List<String> args, List<String> compileErorrs) {
        boolean heal = true;
        if (args.get(2).equalsIgnoreCase("false")) {
            heal = false;
        }
        String mode = validOperator(args.get(0));
        if (mode == null) {
            compileErorrs.add("&cUnknown operator on line &e" + (index + 1) + "&c!");
            return;
        }
        new ChangeMaxHealth(mode, args.get(1), heal);
    }
}
