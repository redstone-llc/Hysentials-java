package cc.woverflow.hysentials.htsl.cluster;

import cc.woverflow.hysentials.htsl.Cluster;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class Button extends Cluster {
    public Button(@Nullable List<Object[]> loaders) {
        super("Button", "button", loaders);
        add(close());
        add(manualOpen("button", "&aPlease 'Shift + Right Click' on the Button you want to edit."));
        if (loaders != null) {
            loadActions(loaders);
        }
        add(close());
    }

    @Override
    public Cluster create(int index, List<String> compileErrors, Object... args) {
        return new Button((List<Object[]>) args[1]);
    }
}
