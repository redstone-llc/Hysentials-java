package llc.redstone.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import llc.redstone.hysentials.guis.actionLibrary.ActionLibrary;
import net.minecraft.client.Minecraft;

@Command(value = "actionlibrary", aliases = {"al"})
public class ActionLibraryCommand {
    @Main()
    public void main() {
        new ActionLibrary().open(UMinecraft.getPlayer());
    }
}
