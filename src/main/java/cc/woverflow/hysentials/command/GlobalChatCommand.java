package cc.woverflow.hysentials.command;

import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.hysentialMods.ChatConfig;
import cc.woverflow.hysentials.util.BUtils;
import cc.woverflow.hysentials.util.MUtils;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.websocket.Socket;
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
            MUtils.chat(HysentialsConfig.chatPrefix + "&cYou are not in a Hypixel server!");
            return;
        }
        if (!ChatConfig.globalChat || !Hysentials.INSTANCE.getConfig().chatConfig.enabled) {
            MUtils.chat(HysentialsConfig.chatPrefix + "&cGlobal chat is disabled!");
            return;
        }
        if (args.length == 0) {
            MUtils.chat(HysentialsConfig.chatPrefix + "&cInvalid usage! /globalchat <message>");
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
