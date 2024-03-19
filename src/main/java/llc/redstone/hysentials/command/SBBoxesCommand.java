package llc.redstone.hysentials.command;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.guis.sbBoxes.SBBoxesEditor;

@Command(value = "sbboxes", aliases = {"scoreboardboxes", "sbb"},
    customHelpMessage = {
        "§9&m                                                        ",
        "&aScoreboard Boxes &c[BETA]",
        "&e/sbboxes config &7- &bOpen the config.",
        "&e/sbboxes editor &7- &bOpen the editor.",
        "§9&m                                                        "
    })
public class SBBoxesCommand {
    @SubCommand(description = "open config", aliases = "config")
    private static void config() {
        Hysentials.INSTANCE.getConfig().openGui();
    }

    @SubCommand(description = "Scoreboard Boxes editor", aliases = "edit")
    private static void editor() {
        new SBBoxesEditor().show();
    }
}
