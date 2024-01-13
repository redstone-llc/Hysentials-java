package llc.redstone.hysentials.command;

import llc.redstone.hysentials.config.hysentialMods.ChatConfig;
import llc.redstone.hysentials.handlers.chat.modules.bwranks.BWSReplace;
import llc.redstone.hysentials.util.BUtils;
import llc.redstone.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.handlers.chat.modules.bwranks.BWSReplace;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

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
    public static String gotoChannel = "All";
    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        List<String> chats = Arrays.asList("All", "Global", "Party", "Guild", "Officer", "Skyblock-Coop");
        List<String> chatAliases = Arrays.asList("a", "gl", "p", "g", "o", "coop");
        if (!BUtils.isHypixelOrSBX()) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/chat " + String.join(" ", args));
            return;
        }
        if (args.length == 0) {
            MUtils.chat("&cInvalid usage! /chat <channel>");
            MUtils.chat("&cValid channels: " + String.join(", ", chats));
            return;
        }
        boolean enabled = ChatConfig.globalChat && Hysentials.INSTANCE.getConfig().chatConfig.enabled;
        if (!enabled) {
            chats.remove(1);
            chatAliases.remove(1);
        }
        String command = args[0].toLowerCase();
        if ((command.equals("global") || command.equals("gl")) && enabled) {
            if (isInGlobalChat) {
                BWSReplace.diagnostics.add("Already in Global Chat");
                MUtils.chat("&cYou're already in this channel!");
            } else {
                isInGlobalChat = true;
                BWSReplace.diagnostics.add("Global Chat set to true");
                MUtils.chat("&aYou are now in the &6GLOBAL &achannel!");
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
                    gotoChannel = command;
                }
                BWSReplace.diagnostics.add("Sending /chat " + command);
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/chat " + command);
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        List<String> chats = Arrays.asList("All", "Global", "Party", "Guild", "Officer", "Skyblock-Coop");
        boolean enabled = ChatConfig.globalChat && Hysentials.INSTANCE.getConfig().chatConfig.enabled;
        if (!enabled) {
            chats.remove(1);
        }
        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, chats);
        }
        return null;
    }
}
