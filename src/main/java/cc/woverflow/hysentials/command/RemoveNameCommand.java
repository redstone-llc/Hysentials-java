package cc.woverflow.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.item.ItemStack;
import scala.tools.nsc.ScalaDoc;

import static cc.woverflow.hysentials.command.RenameCommand.setCreativeAction;

public class RemoveNameCommand extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "removename";
    }

    @Override
    public String getCommandUsage(net.minecraft.command.ICommandSender sender) {
        return "/removename";
    }

    @Override
    public void processCommand(net.minecraft.command.ICommandSender sender, String[] args) {
        ItemStack item = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (item == null || item.getItem() == null) {
            UChat.chat("§cYou must be holding an item!");
            return;
        }
        item.clearCustomName();
        setCreativeAction(item, Minecraft.getMinecraft().thePlayer.inventory.currentItem);
        UChat.chat("§aRemoved name from item successfully!");
    }
}
