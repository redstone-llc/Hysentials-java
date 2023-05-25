package cc.woverflow.hysentials.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemNBT {
    public static ItemStack setString(ItemStack itemStack, String key, String value) {
        NBTTagCompound compound = itemStack.getTagCompound();
        if (compound == null) {
            compound = new NBTTagCompound();
            itemStack.setTagCompound(compound);
        }
        compound.setString(key, value);
        return itemStack;
    }

    public static String getString(ItemStack itemStack, String key) {
        NBTTagCompound compound = itemStack.getTagCompound();
        if (compound != null && compound.hasKey(key)) {
            return compound.getString(key);
        }
        return "";
    }
}
