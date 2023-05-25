package cc.woverflow.hysentials.handlers.guis;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class GameMenuOpen {
    public static Field field_lowerChestInventory;
    public static Field field_inventoryContents;

    public GameMenuOpen () {
        try {
            field_lowerChestInventory = GuiChest.class.getDeclaredField("field_147015_w");
            field_lowerChestInventory.setAccessible(true);
            field_inventoryContents = InventoryBasic.class.getDeclaredField("field_70482_c");
            field_inventoryContents.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    @SubscribeEvent
    public void openMenu(GuiOpenEvent event) {
        if (event.gui instanceof GuiChest) {
            GuiChest chest = (GuiChest) event.gui;
            try {
                if (field_lowerChestInventory.get(chest) instanceof IInventory) {
                    IInventory inventory = (IInventory) field_lowerChestInventory.get(chest);
                    if (inventory.getName().equals("Game Menu")) {
                        InventoryBasic inventoryBasic = (InventoryBasic) inventory;
                        ItemStack[] contents = (ItemStack[]) field_inventoryContents.get(inventoryBasic);
                        List<ItemStack> items = Arrays.asList(contents);
//                        new RevampedGameMenu(items).show();
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
