package cc.woverflow.hysentials.command;

import cc.woverflow.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.utils.StringUtils;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.util.HypixelAPIUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.List;

public class HypixelChatCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "chat";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/chat <channel>";
    }
    public static boolean isInGlobalChat = false;
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        List<String> chats = Arrays.asList("All", "Global", "Party", "Guild", "Officer", "Skyblock-Coop");
        List<String> chatAliases = Arrays.asList("a", "gl", "p", "g", "o", "coop");
        if (args.length == 0) {
            MUtils.chat("&cInvalid usage! /chat <channel>");
            MUtils.chat("&cValid channels: " + String.join(", ", chats));
        }
        if (!HysentialsConfig.globalChatEnabled) {
            chats.remove(1);
            chatAliases.remove(1);
        }
        if (!HypixelUtils.INSTANCE.isHypixel()) {
            MUtils.chat("&cYou are not in a Hypixel server!");
            return;
        }
        String command = args[0].toLowerCase();
        if (command.equals("global") || command.equals("gl") && HysentialsConfig.globalChatEnabled) {
            if (isInGlobalChat) {
                MUtils.chat("&cYou're already in this channel!");
            } else {
                isInGlobalChat = true;
                MUtils.chat("&aYou are now in the &6GLOBAL &achat!");
            }
        } else {
            String finalCommand = command;
            if (chats.stream().noneMatch(c -> c.toLowerCase().equals(finalCommand)) && !chatAliases.contains(command)) {
                MUtils.chat("&cInvalid Channel! Valid channels: " + String.join(", ", chats));
            } else {
                if (isInGlobalChat) {
                    if (chatAliases.contains(command)) {
                        command = chats.get(chatAliases.indexOf(command));
                    }
//                    MUtils.chat("&aYou are now in the &6" + command.toUpperCase() + " &achat!");
                }
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/chat " + command);
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        List<String> chats = Arrays.asList("All", "Global", "Party", "Guild", "Officer", "Skyblock-Coop");
        if (!HysentialsConfig.globalChatEnabled) {
            chats.remove(1);
        }
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, chats);
        }
        return null;
    }
}
