package cc.woverflow.hysentials.command;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.woverflow.hysentials.guis.club.HousingViewer;
import cc.woverflow.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import org.json.JSONObject;

public class VisitCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "visit";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/visit <player>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) return;
        String player = args[0];
        Multithreading.runAsync(() -> {
            String s = NetworkUtils.getString("https://hysentials.redstone.llc/api/club?alias=" + player);
            JSONObject clubData = new JSONObject(s);
            if (clubData.getBoolean("success")) {
                new HousingViewer(clubData.getJSONObject("club")).open();
            } else {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/visit " + player);
            }
        });
    }
}
