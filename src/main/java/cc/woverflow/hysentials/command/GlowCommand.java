package cc.woverflow.hysentials.command;

import cc.woverflow.hysentials.util.MUtils;
import cc.woverflow.hysentials.guis.container.GuiItem;
import cc.woverflow.hysentials.util.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class GlowCommand extends CommandBase {
    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "glow";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/glow";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        ItemStack item = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (item == null || item.getItem() == null) {
            MUtils.chat("§cYou must be holding an item!");
            return;
        }
        item = item.copy();
        if (Material.FISHING_ROD.getId() == Item.getIdFromItem(item.getItem())) {
            item.addEnchantment(Enchantment.infinity, 10);
            GuiItem.hideFlag(item, 1);
            RenameCommand.setCreativeAction(item, Minecraft.getMinecraft().thePlayer.inventory.currentItem);
        } else {
            item.addEnchantment(Enchantment.lure, 10);
            GuiItem.hideFlag(item, 1);
            RenameCommand.setCreativeAction(item, Minecraft.getMinecraft().thePlayer.inventory.currentItem);
        }
        MUtils.chat("§aAdded glow to the item successfully!");
    }
}
