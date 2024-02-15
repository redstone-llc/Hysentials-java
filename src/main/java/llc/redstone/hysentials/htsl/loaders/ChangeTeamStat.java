package llc.redstone.hysentials.htsl.loaders;

import llc.redstone.hysentials.htsl.Loader;
import llc.redstone.hysentials.htsl.Loader;

import java.util.List;

import static llc.redstone.hysentials.htsl.Loader.LoaderObject.*;

public class ChangeTeamStat extends Loader {
    public ChangeTeamStat(String stat, String teamName, String mode, String value) {
        super("Change Team Stat", "teamstat", stat, teamName, mode, value);
        if (stat != null) {
            add(LoaderObject.click(10));
            add(LoaderObject.chat(stat));
        }

        if (teamName != null && !teamName.equalsIgnoreCase("none")) {
            add(LoaderObject.click(13));
            add(LoaderObject.option(teamName));
        }

        if (mode != null && !mode.equals("increment")) {
            add(LoaderObject.click(11));
            if (mode.equals("decrement")) {
                add(LoaderObject.click(11));
            }
            if (mode.equals("set")) {
                add(LoaderObject.click(12));
            }
            if (mode.equals("multiply")) {
                add(LoaderObject.click(13));
            }
            if (mode.equals("divide")) {
                add(LoaderObject.click(14));
            }
        }

        if (value != null && !value.equals("1")) {
            add(LoaderObject.click(12));
            add(LoaderObject.anvil(value));
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        String mode = validOperator(args.get(2));
        if (mode == null) {
            compileErorrs.add("&cUnknown operator on line &e" + (index + 1) + "&c!");
            return null;
        }
        return new ChangeTeamStat(args.get(0), args.get(1), mode, args.get(3));
    }

    @Override
    public String export(List<String> args) {
        return "teamstat \"" + args.get(0) + "\" "  + "\"" + args.get(1) + "\" " + args.get(2) + " " + args.get(3);
    }
}
