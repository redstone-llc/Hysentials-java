package llc.redstone.hysentials.htsl.cluster;

import llc.redstone.hysentials.htsl.Cluster;
import llc.redstone.hysentials.htsl.Cluster;
import llc.redstone.hysentials.htsl.Loader;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.*;
import static llc.redstone.hysentials.htsl.Loader.LoaderObject.click;

public class PlayerDropItem extends Cluster {
    public PlayerDropItem(@Nullable List<Object[]> loaders) {
        super("Player Drop Item", "playerDropItem", loaders);
        add(Loader.LoaderObject.close());
        add(Loader.LoaderObject.command("menu"));
        add(Loader.LoaderObject.click(31));
        add(Loader.LoaderObject.click(11));
        add(Loader.LoaderObject.click(25));
        if (loaders != null) {
            loadActions(loaders);
        }
        add(Loader.LoaderObject.back());
    }
}
