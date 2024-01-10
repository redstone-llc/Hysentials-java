package cc.woverflow.hysentials.htsl.cluster;

import cc.woverflow.hysentials.htsl.Cluster;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class PlayerPickUpItem extends Cluster {
    public PlayerPickUpItem(@Nullable List<Object[]> loaders) {
        super("Player Pick Up Item", "playerPickupItem", loaders);
        add(close());
        add(command("menu"));
        add(click(31));
        add(click(11));
        add(click(28));
        if (loaders != null) {
            loadActions(loaders);
        }
        add(back());
    }
}
