package cc.woverflow.hysentials.command;

import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.woverflow.hysentials.guis.actionLibrary.ActionLibrary;
import net.minecraft.client.Minecraft;

@Command(value = "actionlibrary", aliases = {"al"})
public class ActionLibraryCommand {
    @Main()
    public void main(){
        new ActionLibrary().open(Minecraft.getMinecraft().thePlayer);
    }
}
