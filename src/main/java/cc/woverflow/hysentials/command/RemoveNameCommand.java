package cc.woverflow.hysentials.command;

import cc.woverflow.hysentials.util.BUtils;
import cc.woverflow.hysentials.util.MUtils;
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
        if (!Minecraft.getMinecraft().playerController.getCurrentGameType().isCreative() || BUtils.isSBX()) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/removename " + String.join(" ", args));
            return;
        }
        ItemStack item = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (item == null || item.getItem() == null) {
            MUtils.chat("§cYou must be holding an item!");
            return;
        }
        item = item.copy();
        item.clearCustomName();
        setCreativeAction(item, Minecraft.getMinecraft().thePlayer.inventory.currentItem);
        MUtils.chat("§aRemoved name from item successfully!");
    }
}
