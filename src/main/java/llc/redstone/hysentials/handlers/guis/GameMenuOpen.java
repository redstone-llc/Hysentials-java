package llc.redstone.hysentials.handlers.guis;

import cc.polyfrost.oneconfig.gui.OneConfigGui;
import cc.polyfrost.oneconfig.utils.Multithreading;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.profileviewer.DefaultProfileGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

public class GameMenuOpen {
    public static Field field_lowerChestInventory;
    public static Field field_upperChestInventory;
    public static Field field_inventoryContents;
    public static Field field_currentPage;
    public static Field field_prevPage;

    public GameMenuOpen () {
        try {
            field_lowerChestInventory = ReflectionHelper.findField(GuiChest.class, "lowerChestInventory", "field_147015_w");
            field_lowerChestInventory.setAccessible(true);
            field_upperChestInventory = ReflectionHelper.findField(GuiChest.class, "upperChestInventory", "field_147016_v");
            field_upperChestInventory.setAccessible(true);
            field_inventoryContents = ReflectionHelper.findField(InventoryBasic.class, "inventoryContents", "field_70482_c");
            field_inventoryContents.setAccessible(true);
            field_currentPage = OneConfigGui.class.getDeclaredField("currentPage");
            field_currentPage.setAccessible(true);
            field_prevPage = OneConfigGui.class.getDeclaredField("prevPage");
            field_prevPage.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    @SubscribeEvent
    public void openMenu(GuiOpenEvent event) {
        Multithreading.schedule(() -> {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
                GuiChest chest = (GuiChest) Minecraft.getMinecraft().currentScreen;
                try {
                    if (field_lowerChestInventory.get(chest) instanceof IInventory) {
                        IInventory inventory = (IInventory) field_lowerChestInventory.get(chest);
                        IInventory upperInventory = (IInventory) field_upperChestInventory.get(chest);
                        String name = inventory.getName();

                        ItemStack item = inventory.getStackInSlot(23);
                        String itemName = "";
                        if (item != null && item.hasDisplayName()) {
                            itemName = item.getDisplayName();
                        }

                        if (name.equalsIgnoreCase(itemName.replace("§aReport", "").replace(" ", ""))) {
                            EntityPlayer player = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(name);
                            if (player == null) {
                                return;
                            }
                            Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(new DefaultProfileGui(player));
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 50, TimeUnit.MILLISECONDS);
    }

}
