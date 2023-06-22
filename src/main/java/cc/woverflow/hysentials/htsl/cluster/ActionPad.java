package cc.woverflow.hysentials.htsl.cluster;

import cc.woverflow.hysentials.htsl.Cluster;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.close;
import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.manualOpen;

public class ActionPad extends Cluster {
    public ActionPad(@Nullable List<Object[]> loaders) {
        super("Button", "button", loaders);
        add(close());
        add(manualOpen("button", "&aPlease 'Right Click' on the Action Pad you want to edit."));
        if (loaders != null) {
            loadActions(loaders);
        }
        add(close());
    }

    @Override
    public Cluster create(int index, List<String> compileErrors, Object... args) {
        return new ActionPad((List<Object[]>) args[1]);
    }
}
