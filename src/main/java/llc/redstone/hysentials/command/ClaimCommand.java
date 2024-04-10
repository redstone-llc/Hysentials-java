package llc.redstone.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.util.DuoVariable;
import llc.redstone.hysentials.websocket.Request;
import llc.redstone.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class ClaimCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "claim";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/claim";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 0) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/claim " + String.join(" ", args));
            return;
        }
        Socket.CLIENT.sendText(new Request(
            "method", "claim",
            "uuid", Minecraft.getMinecraft().getSession().getProfile().getId().toString(),
            "username", Minecraft.getMinecraft().getSession().getProfile().getName(),
            "key", Socket.serverId
            ).toString()
        );
        Socket.awaiting.add(new DuoVariable<>("claim", (data) -> {
            if (data.has("success") && !data.get("success").getAsBoolean()) {
                if (data.has("message")) {
                    UChat.chat(HysentialsConfig.chatPrefix + " " + data.get("message").getAsString());
                }
//                Minecraft.getMinecraft().thePlayer.sendChatMessage("/claim");
            }
        }));
    }
}
