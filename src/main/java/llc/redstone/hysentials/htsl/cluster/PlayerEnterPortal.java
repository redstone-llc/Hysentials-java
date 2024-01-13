package llc.redstone.hysentials.htsl.cluster;

import llc.redstone.hysentials.htsl.Cluster;
import llc.redstone.hysentials.htsl.Cluster;
import llc.redstone.hysentials.htsl.Loader;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.*;

public class PlayerEnterPortal extends Cluster {
    public PlayerEnterPortal(@Nullable List<Object[]> loaders) {
        super("Player Enter Portal", "playerEnterPortal", loaders);
        add(Loader.LoaderObject.close());
        add(Loader.LoaderObject.command("menu"));
        add(Loader.LoaderObject.click(31));
        add(Loader.LoaderObject.click(11));
        add(Loader.LoaderObject.click(20));
        if (loaders != null) {
            loadActions(loaders);
        }
        add(Loader.LoaderObject.back());
    }

    @Override
    public Cluster create(int index, List<String> compileErrors, Object... args) {
        return new CompleteParkour((List<Object[]>) args[1]);
    }
}
