package cc.woverflow.hysentials.command;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.woverflow.hysentials.guis.club.HousingViewer;
import cc.woverflow.hysentials.schema.HysentialsSchema;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class VisitPlayerCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "visitplayer";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/visitplayer <player>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) return;
        String player = args[0];
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/visit " + player);
    }
}
