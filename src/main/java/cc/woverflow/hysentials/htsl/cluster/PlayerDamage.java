package cc.woverflow.hysentials.htsl.cluster;

import cc.woverflow.hysentials.htsl.Cluster;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class PlayerDamage extends Cluster {
    public PlayerDamage(@Nullable List<Object[]> loaders) {
        super("Player Damage", "playerDamage", loaders);
        add(close());
        add(command("menu"));
        add(click(39));
        add(click(28));
        add(click(21));
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
