package cc.woverflow.hysentials.command;

import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.NetworkUtils;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.woverflow.hysentials.guis.club.HousingViewer;
import cc.woverflow.hysentials.websocket.Socket;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
            try {
                Gson gson = new GsonBuilder().create();
                String s = NetworkUtils.getString("http://127.0.0.1:8080/api/club?alias=" + player);
                JsonObject clubData = gson.fromJson(s, JsonObject.class);
                if (clubData.get("success").getAsBoolean()) {
                    System.out.println(clubData.get("club"));
                    new HousingViewer(gson.fromJson(clubData.get("club").getAsString(), JsonObject.class)).open();
                } else {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/visit " + player);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
