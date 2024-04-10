package llc.redstone.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import llc.redstone.hysentials.util.BUtils;
import llc.redstone.hysentials.guis.container.GuiItem;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;

public class RemoveGlowCommand extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "removeglow";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/removeglow";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (!Minecraft.getMinecraft().playerController.getCurrentGameType().isCreative() || BUtils.isSBX()) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/removeglow " + String.join(" ", args));
            return;
        }
        ItemStack item = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (item == null || item.getItem() == null) {
            UChat.chat("§cYou must be holding an item!");
            return;
        }

        item = item.copy();

        if (item.hasTagCompound() && item.getTagCompound().hasKey("ench")) {
            GuiItem.setEnchanted(item, false);
            GuiItem.hideFlag(item, 0);
            RenameCommand.setCreativeAction(item, Minecraft.getMinecraft().thePlayer.inventory.currentItem);
            UChat.chat("§aRemoved glow from the item successfully!");
        } else {
            UChat.chat("§cThe item does not have an enchant glint!");
        }
    }
}
