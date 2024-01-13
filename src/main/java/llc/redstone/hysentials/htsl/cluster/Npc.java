package llc.redstone.hysentials.htsl.cluster;

import llc.redstone.hysentials.htsl.Cluster;
import llc.redstone.hysentials.htsl.Cluster;
import llc.redstone.hysentials.htsl.Loader;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.*;

public class Npc extends Cluster {
    public Npc(String name, @Nullable List<Object[]> loaders) {
        super("Npc", "npc", name, loaders);
        add(Loader.LoaderObject.close());
        if (name == null) {
            add(Loader.LoaderObject.manualOpen("npc", "&aPlease 'Shift + Left Click' on the NPC you want to edit."));
        } else {
            add(Loader.LoaderObject.manualOpen("npc", "&aPlease 'Shift + Left Click' on the NPC that has the name '" + name + "'."));
        }
        add(Loader.LoaderObject.click(12));
        if (loaders != null) {
            loadActions(loaders);
        }
        add(Loader.LoaderObject.close());
    }

    @Override
    public Cluster create(int index, List<String> compileErrors, Object... args) {
        return new Npc(args[0].toString(), (List<Object[]>) args[1]);
    }
}
