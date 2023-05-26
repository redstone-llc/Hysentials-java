package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;

import java.util.List;

public class RandomAction extends Loader {
    public RandomAction(List<Object[]> actions) {
        super("Random Action", "random", actions);
        if (actions != null) {
            add(LoaderObject.click(10));
            actions.forEach(action -> sequence.addAll(loadAction(action)));
            add(LoaderObject.back());
        }
    }
}
