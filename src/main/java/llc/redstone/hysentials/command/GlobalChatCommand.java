package llc.redstone.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.hysentialmods.ChatConfig;
import llc.redstone.hysentials.util.BUtils;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.websocket.Socket;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class GlobalChatCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "globalchat";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("glc", "glchat");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/globalchat <message>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (!BUtils.isHypixelOrSBX()) {
            UChat.chat(HysentialsConfig.chatPrefix + " &cYou are not on a Hypixel server!");
            return;
        }
        if (!ChatConfig.globalChat || !Hysentials.INSTANCE.getConfig().chatConfig.enabled) {
            UChat.chat(HysentialsConfig.chatPrefix + " &cGlobal chat is disabled!");
            return;
        }
        if (args.length == 0) {
            UChat.chat(HysentialsConfig.chatPrefix + " &cInvalid usage! /globalchat <message>");
            return;
        }

        String message = String.join(" ", args);
        JSONObject json = new JSONObject();
        json.put("method", "chat");
        json.put("message", message);
        json.put("username", sender.getName());
        json.put("uuid", sender.getCommandSenderEntity().getUniqueID().toString());
        json.put("server", false);
        json.put("displayName", sender.getDisplayName().getFormattedText()); //This gets overwritten by the server lol!
        json.put("key", Socket.serverId);

        Socket.CLIENT.sendText(json.toString());
    }

}
