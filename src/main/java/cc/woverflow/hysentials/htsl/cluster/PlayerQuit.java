package cc.woverflow.hysentials.htsl.cluster;

import cc.woverflow.hysentials.htsl.Cluster;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class PlayerQuit extends Cluster {
    public PlayerQuit(@Nullable List<Object[]> loaders) {
        super("Player Quit", "playerQuit", loaders);
        add(close());
        add(command("menu"));
        add(click(39));
        add(click(28));
        add(click(11));
        if (loaders != null) {
            loadActions(loaders);
        }
        add(back());
    }

    @Override
    public Cluster create(int index, List<String> compileErrors, Object... args) {
        return new PlayerQuit((List<Object[]>) args[1]);
    }
}
