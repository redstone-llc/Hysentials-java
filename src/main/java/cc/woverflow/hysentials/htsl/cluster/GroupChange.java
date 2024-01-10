package cc.woverflow.hysentials.htsl.cluster;

import cc.woverflow.hysentials.htsl.Cluster;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class GroupChange extends Cluster {
    public GroupChange(@Nullable List<Object[]> loaders) {
        super("Group Change", "groupChange", loaders);
        add(close());
        add(command("menu"));
        add(click(31));
        add(click(11));
        add(click(15));
        if (loaders != null) {
            loadActions(loaders);
        }
        add(back());
    }

    @Override
    public Cluster create(int index, List<String> compileErrors, Object... args) {
        return new GroupChange((List<Object[]>) args[1]);
    }
}
