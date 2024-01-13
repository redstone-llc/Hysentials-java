package llc.redstone.hysentials.htsl.cluster;

import llc.redstone.hysentials.htsl.Cluster;
import llc.redstone.hysentials.htsl.Cluster;
import llc.redstone.hysentials.htsl.Loader;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.close;
import static llc.redstone.hysentials.htsl.Loader.LoaderObject.manualOpen;

public class ActionPad extends Cluster {
    public ActionPad(@Nullable List<Object[]> loaders) {
        super("Button", "button", loaders);
        add(Loader.LoaderObject.close());
        add(Loader.LoaderObject.manualOpen("button", "&aPlease 'Right Click' on the Action Pad you want to edit."));
        if (loaders != null) {
            loadActions(loaders);
        }
        add(Loader.LoaderObject.close());
    }

    @Override
    public Cluster create(int index, List<String> compileErrors, Object... args) {
        return new ActionPad((List<Object[]>) args[1]);
    }
}
