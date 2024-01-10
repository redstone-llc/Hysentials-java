package cc.woverflow.hysentials.htsl.cluster;

import cc.woverflow.hysentials.htsl.Cluster;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class PlayerChangeHeldItem extends Cluster {
    public PlayerChangeHeldItem(@Nullable List<Object[]> loaders) {
        super("Player Change Held Item", "playerChangeHeldItem", loaders);
        add(close());
        add(command("menu"));
        add(click(31));
        add(click(11));
        add(click(29));
        if (loaders != null) {
            loadActions(loaders);
        }
        add(back());
    }
}
