package llc.redstone.hysentials.htsl.cluster;

import llc.redstone.hysentials.htsl.Cluster;
import llc.redstone.hysentials.htsl.Cluster;
import llc.redstone.hysentials.htsl.Loader;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.*;

public class Button extends Cluster {
    public Button(@Nullable List<Object[]> loaders) {
        super("Button", "button", loaders);
        add(Loader.LoaderObject.close());
        add(Loader.LoaderObject.manualOpen("button", "&aPlease 'Shift + Right Click' on the Button you want to edit."));
        if (loaders != null) {
            loadActions(loaders);
        }
        add(Loader.LoaderObject.close());
    }

    @Override
    public Cluster create(int index, List<String> compileErrors, Object... args) {
        return new Button((List<Object[]>) args[1]);
    }
}
