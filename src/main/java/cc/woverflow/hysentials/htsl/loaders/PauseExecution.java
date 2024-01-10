package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class PauseExecution extends Loader {
    public PauseExecution(String ticks) {
        super("Pause Execution", "pause", ticks);
        if (isNAN(ticks)) {
            return;
        }
        int tick = Integer.parseInt(ticks);
        if (tick > 6000 || tick < 1) {
            return;
        }
        add(click(10));
        add(anvil(ticks));
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        if (args.size() != 1) {
            compileErorrs.add(String.format("&cInvalid arguments on line &e%d&c!", index + 1));
            return null;
        }
        if (isNAN(args.get(0))) {
            compileErorrs.add(String.format("&cInvalid argument on line &e%d&c!", index + 1));
            return null;
        }
        return new PauseExecution(args.get(0));
    }

    @Override
    public String export(List<String> args) {
        return "pause " + args.get(0);
    }
}
