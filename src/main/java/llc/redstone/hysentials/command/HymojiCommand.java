package llc.redstone.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import llc.redstone.hysentials.handlers.imageicons.ImageIcon;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.Arrays;
import java.util.List;

public class HymojiCommand extends CommandBase {
    @Override
    public String getCommandName() {
        return "hymoji";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/hymoji";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("hymojis");
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        StringBuilder sb = new StringBuilder();
        sb.append("§aHymojis: \n");
        for (String s : ImageIcon.imageIcons.values().stream().filter(imageIcon -> imageIcon.emoji).map(ImageIcon::getName).toArray(String[]::new)) {
            sb.append("§6:").append(s).append("?: §7- §f").append(":").append(s).append(":\n");
        }
        UChat.chat(sb.toString());
    }
}
