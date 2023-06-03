package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;

import java.util.List;

public class RandomAction extends Loader {
    public RandomAction(List<Object[]> actions) {
        super("Random Action", "random", actions);
        if (actions != null) {
            add(LoaderObject.click(10));
            for (Object[] action : actions) {
                sequence.addAll(loadAction(action));
            }
            add(LoaderObject.back());
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return null;
    }

    @Override
    public String export(List<String> args) {
        args.get(0);
        return "random";
    }
}
