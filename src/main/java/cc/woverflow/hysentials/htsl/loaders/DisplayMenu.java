package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.click;
import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.option;

public class DisplayMenu extends Loader {
    public DisplayMenu(String menuName) {
        super("Display Menu", "displayMenu", menuName);
        if (isNAN(menuName)) {
            return;
        }
        add(click(10));
        add(option(menuName));
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
