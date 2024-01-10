package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;

import java.util.List;

import static cc.woverflow.hysentials.htsl.Loader.LoaderObject.*;

public class ChangeTeamStat extends Loader {
    public ChangeTeamStat(String stat, String mode, String value, String teamName) {
        super("Change Team Stat", "teamstat", stat, mode, value, teamName);
        if (stat != null) {
            add(click(10));
            add(chat(stat));
        }

        if (mode != null && !mode.equals("increment")) {
            add(click(11));
            if (mode.equals("decrement")) {
                add(click(11));
            }
            if (mode.equals("set")) {
                add(click(12));
            }
            if (mode.equals("multiply")) {
                add(click(13));
            }
            if (mode.equals("divide")) {
                add(click(14));
            }
        }

        if (value != null && !value.equals("1")) {
            add(click(12));
            add(anvil(value));
        }

        if (teamName != null && !teamName.equalsIgnoreCase("none")) {
            add(click(13));
            add(option(teamName));
        }
    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        String mode = validOperator(args.get(1));
        if (mode == null) {
            compileErorrs.add("&cUnknown operator on line &e" + (index + 1) + "&c!");
            return null;
        }
        return new ChangeTeamStat(args.get(0), mode, args.get(2), args.get(3));
    }

    @Override
    public String export(List<String> args) {
        return "teamstat \"" + args.get(0) + "\" " + args.get(1) + " " + args.get(2) + " \"" + args.get(3) + "\"";
    }
}
