package llc.redstone.hysentials.command;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import llc.redstone.hysentials.HysentialsUtilsKt;
import llc.redstone.hysentials.guis.container.containers.club.HousingViewer;
import llc.redstone.hysentials.schema.HysentialsSchema;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import llc.redstone.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

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
        if (Socket.CLIENT == null || !Socket.CLIENT.isOpen()) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/visit " + player);
            return;
        }
        Multithreading.runAsync(() -> {
            try {
                String s = NetworkUtils.getString(HysentialsUtilsKt.getHYSENTIALS_API() + "/club?alias=" + player);
                JsonObject clubData = new JsonParser().parse(s).getAsJsonObject();
                if (clubData.get("success").getAsBoolean()) {
                    HysentialsSchema.Club club = HysentialsSchema.Club.Companion.deserialize(clubData.get("club").getAsJsonObject());
                    new HousingViewer(club).open();
                } else {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/visit " + player);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
