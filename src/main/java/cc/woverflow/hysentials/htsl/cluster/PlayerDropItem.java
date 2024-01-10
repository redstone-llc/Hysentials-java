package cc.woverflow.hysentials.htsl.cluster;

import cc.woverflow.hysentials.htsl.Cluster;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;
import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.click;

public class PlayerDropItem extends Cluster {
    public PlayerDropItem(@Nullable List<Object[]> loaders) {
        super("Player Drop Item", "playerDropItem", loaders);
        add(close());
        add(command("menu"));
        add(click(31));
        add(click(11));
        add(click(25));
        if (loaders != null) {
            loadActions(loaders);
        }
        add(back());
    }
}
