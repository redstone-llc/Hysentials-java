package llc.redstone.hysentials.htsl.cluster;

import llc.redstone.hysentials.htsl.Cluster;
import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Cluster;
import llc.redstone.hysentials.htsl.Loader;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.*;
import static llc.redstone.hysentials.htsl.Loader.LoaderObject.returnToEditActions;

public class Function extends Cluster {
    public Function(String name, @Nullable List<Object[]> loaders) {
        super("Function", "function", name);
        add(Loader.LoaderObject.close());
        add(Loader.LoaderObject.command("function"));
        add(Loader.LoaderObject.selectOrClick(name, 50));
//        add(Loader.LoaderObject.click(50));
        add(Loader.LoaderObject.anvil(name));
        if (loaders != null) {
            loadActions(loaders);
        }
        add(Loader.LoaderObject.close());
    }

    @Override
    public Cluster create(int index, List<String> compileErrors, Object... args) {
        return new Function((String) args[0], (List<Object[]>) args[1]);
    }
}
