package cc.woverflow.hysentials.guis.container;

import cc.polyfrost.oneconfig.libs.universal.ChatColor;
import cc.woverflow.hysentials.util.ItemNBT;
import cc.woverflow.hysentials.util.Material;
import cc.woverflow.hysentials.util.Renderer;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTUtil;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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

    public static List<String> stringToLore(String string, int characterLimit, ChatColor prefixColor) {
        String[] words = string.split(" ");
        List<String> lines = new ArrayList();
        StringBuilder currentLine = new StringBuilder();
        String[] var6 = words;
        int var7 = words.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            String word = var6[var8];
            if (!word.equals("/newline")) {
                if (currentLine.toString().equals("")) {
                    currentLine = new StringBuilder(word);
                } else {
                    currentLine.append(" ").append(word);
                }
            }

            if (word.equals("/newline") || currentLine.length() + word.length() >= characterLimit) {
                String newLine = currentLine.toString();
                lines.add("" + prefixColor + newLine);
                currentLine = new StringBuilder();
            }
        }

        if (currentLine.length() > 0) {
            lines.add("" + prefixColor + currentLine);
        }

        return lines;
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

    public static ItemStack makeColorfulSkullItem(String displayname, String owner, int amount, List<String> lore) {
        ItemStack item = makeColorfulItem(Material.SKULL_ITEM, displayname, amount, 3, lore);

        NBTTagCompound skullOwner = new NBTTagCompound();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        owner = new String(Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", owner).getBytes()));
        profile.getProperties().put("textures", new Property("textures", owner));
        NBTUtil.writeGameProfile(skullOwner, profile);
        item.setTagInfo("SkullOwner", skullOwner);

        return item;
    }

    public static ItemStack makeMonsterEgg(String displayName, int amount, int durability, String... lore) {
        return makeColorfulItem(Material.MONSTER_EGG, displayName, amount, durability, lore);
    }

    public static ItemStack makeMonsterEgg(String displayName, int amount, int durability, List<String> lore) {
        return makeColorfulItem(Material.MONSTER_EGG, displayName, amount, durability, lore);
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

    public static List<String> getLore(ItemStack item) {
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

        NBTTagList lore = displayTag.getTagList("Lore", 8);
        List<String> loreLines = new ArrayList<>();

        for (int i = 0; i < lore.tagCount(); i++) {
            loreLines.add(lore.getStringTagAt(i));
        }

        return loreLines;
    }

    public static ItemStack setColor(ItemStack item, String color) {
        if (item.getTagCompound() == null) {
            item.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound nbtTag = item.getTagCompound();
        if (!nbtTag.hasKey("display")) {
            nbtTag.setTag("display", new NBTTagCompound());
        }

        NBTTagCompound displayTag = nbtTag.getCompoundTag("display");
        //hex to int
        int colorInt = Integer.parseInt(color, 16);
        displayTag.setInteger("color", colorInt);
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

    public static ItemStack setEnchanted(ItemStack item, boolean enchanted) {
        if (item.getTagCompound() == null) {
            item.setTagCompound(new NBTTagCompound());
        }

        NBTTagCompound nbtTag = item.getTagCompound();
        if (enchanted && !nbtTag.hasKey("ench")) {
            nbtTag.setTag("ench", new NBTTagList());
        } else {
            nbtTag.removeTag("ench");
        }

        return item;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
