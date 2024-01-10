package cc.woverflow.hysentials.htsl.loaders;

import cc.woverflow.hysentials.htsl.Loader;

import java.util.List;

public class BalanceTeam extends Loader {
    public BalanceTeam() {
        super("Balance Team", "balanceTeam");

    }

    @Override
    public Loader load(int index, List<String> args, List<String> compileErorrs) {
        return new BalanceTeam();
    }

    @Override
    public String export(List<String> args) {
        return "balanceTeam";
    }
}
