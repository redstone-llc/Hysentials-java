package cc.woverflow.hysentials.command;

import cc.woverflow.hysentials.util.BUtils;
import cc.woverflow.hysentials.util.MUtils;
import cc.woverflow.hysentials.guis.container.GuiItem;
import cc.woverflow.hysentials.util.DuoVariable;
import cc.woverflow.hysentials.util.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            MUtils.chat("§cYou must be holding an item!");
            return;
        }

        item = item.copy();

        if (item.hasTagCompound() && item.getTagCompound().hasKey("ench")) {
            GuiItem.setEnchanted(item, false);
            GuiItem.hideFlag(item, 0);
            RenameCommand.setCreativeAction(item, Minecraft.getMinecraft().thePlayer.inventory.currentItem);
            MUtils.chat("§aRemoved glow from the item successfully!");
        } else {
            MUtils.chat("§cThe item does not have an enchant glint!");
        }
    }
}
