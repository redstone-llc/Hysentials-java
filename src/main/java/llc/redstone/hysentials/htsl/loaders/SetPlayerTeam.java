package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.*;

public class SetPlayerTeam extends Loader {
    public SetPlayerTeam(String teamName) {
        super("Set Player Team", "setTeam", teamName);
        if (isNAN(teamName) || teamName.equalsIgnoreCase("none")) {
            return;
        }
        add(LoaderObject.click(10));
        add(LoaderObject.option(teamName));
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new SetPlayerTeam(args.get(index));
    }

    @Override
    public String export(List<String> args) {
        return "setTeam \"" + args.get(0) + "\"";
    }
}
