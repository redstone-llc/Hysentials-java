package cc.woverflow.hysentials.command;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.guis.sbBoxes.SBBoxesEditor;

@Command(value = "sbboxes", aliases = {"scoreboardboxes", "sbb"})
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
