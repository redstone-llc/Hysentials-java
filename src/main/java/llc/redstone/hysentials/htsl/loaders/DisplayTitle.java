package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;
import org.json.JSONObject;

import java.util.List;

public class DisplayTitle extends Loader {
    public DisplayTitle(String title, String subtitle, String fadeIn, String stay, String fadeOut) {
        super("Display Title", "title", title, subtitle, fadeIn, stay, fadeOut);

        if (title != null) {
            add(LoaderObject.click(10));
            add(LoaderObject.chat(title));
        }

        if (subtitle != null) {
            add(LoaderObject.click(11));
            add(LoaderObject.chat(subtitle));
        }

        if (!isNAN(fadeIn) && !fadeIn.equals("1")) {
            add(LoaderObject.click(12));
            add(LoaderObject.anvil(fadeIn));
        }

        if (!isNAN(stay) && !stay.equals("5")) {
            add(LoaderObject.click(13));
            add(LoaderObject.anvil(stay));
        }

        if (!isNAN(fadeOut) && !fadeOut.equals("1")) {
            add(LoaderObject.click(14));
            add(LoaderObject.anvil(fadeOut));
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        if (args.size() != 5) {
            compileErorrs.add("&cIncomplete arguments on line &e" + (index + 1) + "&c!");
            return null;
        }
        return new DisplayTitle(args.get(0), args.get(1), args.get(2), args.get(3), args.get(4));
    }

    @Override
    public String export(List<String> args) {
        return "title \"" + args.get(0) + "\" \"" + args.get(1) + "\" " + args.get(2) + " " + args.get(3) + " " + args.get(4);
    }
}
