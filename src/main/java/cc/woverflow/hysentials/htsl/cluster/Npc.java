package cc.woverflow.hysentials.htsl.cluster;

import cc.woverflow.hysentials.htsl.Cluster;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class Npc extends Cluster {
    public Npc(String name, @Nullable List<Object[]> loaders) {
        super("Npc", "npc", name, loaders);
        add(close());
        if (name == null) {
            add(manualOpen("npc", "&aPlease 'Shift + Left Click' on the NPC you want to edit."));
        } else {
            add(manualOpen("npc", "&aPlease 'Shift + Left Click' on the NPC that has the name '" + name + "'."));
        }
        add(click(12));
        if (loaders != null) {
            loadActions(loaders);
        }
        add(close());
    }

    @Override
    public Cluster create(int index, List<String> compileErrors, Object... args) {
        return new Npc(args[0].toString(), (List<Object[]>) args[1]);
    }
}
