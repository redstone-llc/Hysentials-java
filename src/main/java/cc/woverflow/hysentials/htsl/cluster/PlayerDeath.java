package cc.woverflow.hysentials.htsl.cluster;

import cc.woverflow.hysentials.htsl.Cluster;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class PlayerDeath extends Cluster {
    public PlayerDeath(@Nullable List<Object[]> loaders) {
        super("Player Death", "playerDeath", loaders);
        add(close());
        add(command("menu"));
        add(click(31));
        add(click(11));
        add(click(12));
        if (loaders != null) {
            loadActions(loaders);
        }
        add(back());
    }

    @Override
    public Cluster create(int index, List<String> compileErrors, Object... args) {
        return new CompleteParkour((List<Object[]>) args[1]);
    }
}
