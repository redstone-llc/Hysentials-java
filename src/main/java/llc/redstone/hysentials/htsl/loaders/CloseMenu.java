package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;

import java.util.List;

public class CloseMenu extends Loader {
    public CloseMenu() {
        super("Close Menu", "closeMenu");
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new CloseMenu();
    }

    @Override
    public String export(List<String> args) {
        return "closeMenu";
    }
}
