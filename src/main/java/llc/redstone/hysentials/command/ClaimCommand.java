package llc.redstone.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.util.DuoVariable;
import llc.redstone.hysentials.websocket.Request;
import llc.redstone.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

@Command(value = "claim")
public class ClaimCommand {

    @Main()
    public void processCommand() throws CommandException {
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
            }
        }));
    }
}
