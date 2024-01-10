package cc.woverflow.hysentials.command;

import cc.woverflow.hysentials.util.DuoVariable;
import cc.woverflow.hysentials.websocket.Request;
import cc.woverflow.hysentials.websocket.Socket;
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
            if (data.has("success") && (boolean) data.get("success")) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/claim");
            }
        }));
    }
}
