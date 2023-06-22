package cc.woverflow.hysentials.htsl.cluster;

import cc.woverflow.hysentials.htsl.Cluster;
import cc.woverflow.hysentials.htsl.Loader;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;
import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.returnToEditActions;

public class Function extends Cluster {
    public Function(String name, @Nullable List<Object[]> loaders) {
        super("Function", "function", name);
        add(close());
        add(Loader.LoaderObject.command("function"));
        add(selectOrClick(name, 50));
//        add(Loader.LoaderObject.click(50));
        add(Loader.LoaderObject.anvil(name));
        if (loaders != null) {
            loadActions(loaders);
        }
        add(close());
    }

    @Override
    public Cluster create(int index, List<String> compileErrors, Object... args) {
        return new Function((String) args[0], (List<Object[]>) args[1]);
    }
}
