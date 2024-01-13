package llc.redstone.hysentials.guis.container;

import cc.polyfrost.oneconfig.libs.universal.UKeyboard;
import llc.redstone.hysentials.event.events.GuiMouseClickEvent;
import llc.redstone.hysentials.util.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

import static llc.redstone.hysentials.guis.container.Container.INSTANCE;

public class ContainerHandler {
    @SubscribeEvent
    public void mouseClick(GuiMouseClickEvent event) {
        if (UKeyboard.isCtrlKeyDown() && UKeyboard.isKeyDown(UKeyboard.KEY_C)) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
                GuiChest chest = (GuiChest) Minecraft.getMinecraft().currentScreen;
                Slot s = chest.getSlotUnderMouse();
                if (s == null) return;
                int slot = s.getSlotIndex();
                if (!s.getHasStack()) return;
                if (s.getStack().hasDisplayName()) {
                    //copy nbt to clipboard without using UKeyboard since it doesn't work
                    String nbt = nbtToColorfulItem(s.getStack());
                    StringSelection selection = new StringSelection(nbt);
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection, selection);
                    return;
                }
            }
        }
        if (INSTANCE == null) return;
        if (!INSTANCE.isOpen) return;
        Slot s = INSTANCE.guiChest.getSlotUnderMouse();
        if (s == null) return;
        int slot = s.getSlotIndex();
        if (!s.getHasStack()) return;
        INSTANCE.defaultAction.execute(new GuiAction.GuiClickEvent(event.getCi(), slot, INSTANCE.guiChest.inventorySlots.inventoryItemStacks.get(slot), event.getButton()));
        if (INSTANCE.slotActions.containsKey(slot)) {
            GuiAction action = INSTANCE.slotActions.get(slot);
            action.execute(new GuiAction.GuiClickEvent(event.getCi(), slot, INSTANCE.guiChest.inventorySlots.inventoryItemStacks.get(slot), event.getButton()));
        }
    }

    public String nbtToColorfulItem(ItemStack item) {
        if (Item.getIdFromItem(item.getItem()) == Material.SKULL_ITEM.getId()) {
            NBTTagCompound tag = item.getTagCompound();
            if (tag == null) return null;
            String skullURL = getSkullURL(tag);
            if (skullURL == null) return null;

            return "makeColorfulSkullItem(\"" + item.getDisplayName() + "\", \"" + skullURL + "\", " + item.stackSize + ", \"" + String.join("\", \"", getLore(tag)) + "\")";
        } else {
            NBTTagCompound tag = item.getTagCompound();
            if (tag == null) return null;
            String displayname = item.getDisplayName();
            String lore = String.join("\", \"", getLore(tag));
            Material material = Material.getMaterial(Item.getIdFromItem(item.getItem()));
            return "makeColorfulItem(Material." + material.name() + ", \"" + displayname + "\", " + item.stackSize + ", " + item.getItemDamage() + ", \"" + lore + "\")";
        }
    }

    public String getSkullURL(NBTTagCompound tag) {
        if (!tag.hasKey("SkullOwner")) return null;
        NBTTagCompound skullOwner = tag.getCompoundTag("SkullOwner");
        if (!skullOwner.hasKey("Properties")) return null;
        NBTTagCompound properties = skullOwner.getCompoundTag("Properties");
        if (!properties.hasKey("textures")) return null;
        NBTTagList textures = properties.getTagList("textures", 10);
        if (textures.tagCount() == 0) return null;
        NBTTagCompound texture = textures.getCompoundTagAt(0);
        if (!texture.hasKey("Value")) return null;
        String value = texture.getString("Value");
        String decoded = new String(Base64.getDecoder().decode(value));
        String skinURL = Pattern.compile("\"url\" : \"(.+)\"").matcher(decoded).group(1);
        return skinURL;
    }

    public List<String> getLore(NBTTagCompound tag) {
        if (!tag.hasKey("display")) return null;
        NBTTagCompound display = tag.getCompoundTag("display");
        if (!display.hasKey("Lore")) return null;
        NBTTagList lore = display.getTagList("Lore", 8);
        List<String> loreList = new ArrayList<>();
        for (int i = 0; i < lore.tagCount(); i++) {
            loreList.add(lore.getStringTagAt(i));
        }
        return loreList;
    }

}
