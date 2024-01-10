package cc.woverflow.hysentials.htsl.cluster;

import cc.woverflow.hysentials.htsl.Cluster;
import cc.woverflow.hysentials.htsl.Loader;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class PlayerJoin extends Cluster {
    public PlayerJoin(@Nullable List<Object[]> loaders) {
        super("Player Join", "playerJoin", loaders);
        add(close());
        add(command("menu"));
        add(click(31));
        add(click(11));
        add(click(10));
        if (loaders != null) {
            loadActions(loaders);
        }
        add(back());
    }

    @Override
    public Cluster create(int index, List<String> compileErrors, Object... args) {
        return new PlayerJoin((List<Object[]>) args[1]);
    }
}
