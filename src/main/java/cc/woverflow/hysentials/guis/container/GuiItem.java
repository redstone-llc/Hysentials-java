package cc.woverflow.hysentials.guis.container;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.woverflow.hysentials.util.ItemNBT;
import cc.woverflow.hysentials.util.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GuiItem {
    private GuiAction action;
    private ItemStack itemStack;
    private final UUID uuid;

    public GuiItem(@NotNull ItemStack itemStack, @Nullable GuiAction action) {
        this.uuid = UUID.randomUUID();
        Validate.notNull(itemStack, "The ItemStack for the GUI Item cannot be null!");
        this.action = action;
        this.itemStack = ItemNBT.setString(itemStack, "mf-gui", this.uuid.toString());
    }

    public static GuiItem fromStack(@NotNull ItemStack itemStack) {
        Validate.notNull(itemStack, "The ItemStack for the GUI Item cannot be null!");
        return new GuiItem(itemStack, null);
    }

    public static String colorize(String s) {
        return ChatColor.Companion.translateAlternateColorCodes('&', s);
    }

    public static List<String> colorize(List<String> s) {
        List<String> colored = new ArrayList<>();
        for (String b : s) {
            colored.add(ChatColor.Companion.translateAlternateColorCodes('&', b));
        }
        return colored;
    }
    public static ItemStack makeColorfulItem(Material material, String displayName, int amount, int durability, List<String> lore) {
        ItemStack item = new ItemStack(Item.getItemById(material.getId()), amount, (short) durability);
        item.setStackDisplayName(colorize(displayName));


        hideFlag(item, 34);
        setLore(item, colorize(lore));

        return item;
    }

    public static ItemStack makeColorfulItem(Material material, String displayName, int amount, int durability, String... lore) {
        ItemStack item = new ItemStack(Item.getItemById(material.getId()), amount, (short) durability);
        item.setStackDisplayName(colorize(displayName));

        hideFlag(item, 34);
        setLore(item, colorize(Arrays.asList(lore)));

        return item;
    }

    public static ItemStack setLore(ItemStack item, List<String> loreLines) {
        if (item.getTagCompound() == null) {
            item.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound nbtTag = item.getTagCompound();
        if (!nbtTag.hasKey("display")) {
            nbtTag.setTag("display", new NBTTagCompound());
        }

        NBTTagCompound displayTag = nbtTag.getCompoundTag("display");
        if (!displayTag.hasKey("Lore")) {
            displayTag.setTag("Lore", new NBTTagList());
        }

        NBTTagList lore = new NBTTagList();

        for (String line : loreLines) {
            lore.appendTag(new NBTTagString("§r" + line)); // Assuming you want to reset text formatting for each line
        }

        displayTag.setTag("Lore", lore);
        nbtTag.setTag("display", displayTag);

        item.setTagCompound(nbtTag);
        return item;
    }

    public static ItemStack hideFlag(ItemStack item, int flags) {
        if (item.getTagCompound() == null) {
            item.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound nbtTag = item.getTagCompound();
        nbtTag.setInteger("HideFlags", flags);
        item.setTagCompound(nbtTag);
        return item;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
