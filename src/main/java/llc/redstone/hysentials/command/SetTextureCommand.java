package llc.redstone.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import llc.redstone.hysentials.guis.misc.SetTextureGui;
import llc.redstone.hysentials.util.BUtils;
import llc.redstone.hysentials.guis.misc.SetTextureGui;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SetTextureCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "settexture";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/settexture (search/id)";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public List<String> getCommandAliases() {
        return Collections.singletonList("st");
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (!Minecraft.getMinecraft().playerController.getCurrentGameType().isCreative()) {
            UChat.chat("&cYou must be in creative mode to use this command.");
            return;
        }

        if (Minecraft.getMinecraft().thePlayer.getHeldItem() == null) {
            UChat.chat("&cYou must be holding an item to use this command.");
            return;
        }

        String arg = args.length == 1 ? args[0] : null;
        if (args.length > 1) {
            arg = String.join(" ", args);
        }
        if (arg == null || arg.isEmpty() || !BUtils.isInteger(arg)) {
            SetTextureGui gui = new SetTextureGui();
            gui.page = 1;
            gui.search = arg;
            gui.open();
        } else if (arg.length() == 3) {
            ItemStack item = Minecraft.getMinecraft().thePlayer.getHeldItem();
            if (item.getDisplayName().matches("§[0-9A-FK-OR]§[0-9A-FK-OR]§[0-9A-FK-OR][a-zA-Z0-9 ]*")) {
                item.setStackDisplayName(item.getDisplayName().replaceFirst("§[0-9A-FK-OR]§[0-9A-FK-OR]§[0-9A-FK-OR]", "§" + arg.charAt(0) + "§" + arg.charAt(1) + "§" + arg.charAt(2)));
            } else {
                item.setStackDisplayName("§" + arg.charAt(0) + "§" + arg.charAt(1) + "§" + arg.charAt(2) + item.getDisplayName());
            }
            RenameCommand.setCreativeAction(item, Minecraft.getMinecraft().thePlayer.inventory.currentItem);
            UChat.chat("&aItem texture set to: " + arg);
        }
    }
}
