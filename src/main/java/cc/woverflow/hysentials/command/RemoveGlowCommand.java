package cc.woverflow.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
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
        ItemStack item = Minecraft.getMinecraft().thePlayer.getHeldItem();
        if (item == null || item.getItem() == null) {
            UChat.chat("§cYou must be holding an item!");
            return;
        }
        List<DuoVariable<Enchantment, Integer>> enchantments = enchantmentTagListToHashMap(item.getEnchantmentTagList());
        if (Material.FISHING_ROD.getId() == Item.getIdFromItem(item.getItem())) {
            DuoVariable<Enchantment, Integer> ench = enchantments.stream().filter((e) -> e.getFirst().equals(Enchantment.infinity)).findFirst().orElse(null);
            if (ench != null && ench.getSecond() == 10) {
                NBTTagList enchantmentTagList = item.getEnchantmentTagList();
                enchantmentTagList.removeTag(enchantments.indexOf(ench));
                NBTTagCompound tag = item.getTagCompound();
                int hideFlags = item.getTagCompound().getInteger("HideFlags");
                tag.setTag("HideFlags", new NBTTagInt(hideFlags - 1));
                tag.setTag("ench", enchantmentTagList);
                item.setTagCompound(tag);
                RenameCommand.setCreativeAction(item, Minecraft.getMinecraft().thePlayer.inventory.currentItem);
                UChat.chat("§aRemoved glow from the item successfully!");
            } else {
                UChat.chat("§cThis item does not have glow!");
            }
        } else {
            DuoVariable<Enchantment, Integer> ench = enchantments.stream().filter((e) -> e.getFirst().equals(Enchantment.lure)).findFirst().orElse(null);
            if (ench != null && ench.getSecond() == 10) {
                NBTTagList enchantmentTagList = item.getEnchantmentTagList();
                enchantmentTagList.removeTag(enchantments.indexOf(ench));
                NBTTagCompound tag = item.getTagCompound();
                int hideFlags = item.getTagCompound().getInteger("HideFlags");
                tag.setTag("HideFlags", new NBTTagInt(hideFlags - 1));
                tag.setTag("ench", enchantmentTagList);
                item.setTagCompound(tag);
                RenameCommand.setCreativeAction(item, Minecraft.getMinecraft().thePlayer.inventory.currentItem);
                UChat.chat("§aRemoved glow from the item successfully!");
            } else {
                UChat.chat("§cThis item does not have glow!");
            }
        }
    }

    public List<DuoVariable<Enchantment, Integer>> enchantmentTagListToHashMap(NBTTagList enchantmentTagList) {
        List<DuoVariable<Enchantment, Integer>> enchantments = new ArrayList<>();

        for (int i = 0; i < enchantmentTagList.tagCount(); i++) {
            NBTTagCompound enchantmentTag = enchantmentTagList.getCompoundTagAt(i);

            // Assuming the key is stored as a string with the key "id"
            if (enchantmentTag.hasKey("id")) {
                Enchantment enchantmentId = Enchantment.getEnchantmentById(enchantmentTag.getInteger("id"));

                // Assuming the value is stored as an integer with the key "lvl"
                if (enchantmentTag.hasKey("lvl")) {
                    int enchantmentLevel = enchantmentTag.getInteger("lvl");
                    enchantments.add(new DuoVariable<>(enchantmentId, enchantmentLevel));
                }
            }
        }

        return enchantments;
    }

}
