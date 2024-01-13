package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.click;
import static llc.redstone.hysentials.htsl.Loader.LoaderObject.option;

public class DisplayMenu extends Loader {
    public DisplayMenu(String menuName) {
        super("Display Menu", "displayMenu", menuName);
        if (isNAN(menuName)) {
            return;
        }
        add(LoaderObject.click(10));
        add(LoaderObject.option(menuName));
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new DisplayMenu(args.get(index));
    }

    @Override
    public String export(List<String> args) {
        return "displayMenu \"" + args.get(0) + "\"";
    }
}
