package llc.redstone.hysentials.htsl;

import cc.polyfrost.oneconfig.utils.Multithreading;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import static llc.redstone.hysentials.handlers.htsl.Navigator.click;

public class ModifyAnvilOutput {
    public static void modifyOutput(String input) {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiRepair) {
            ContainerRepair container = (ContainerRepair) Minecraft.getMinecraft().thePlayer.openContainer;
            try {
                Field outputSlotField = ContainerRepair.class.getDeclaredField("field_82852_f");
                outputSlotField.setAccessible(true);
                InventoryCraftResult outputSlot = (InventoryCraftResult) outputSlotField.get(container);
                Field stackResultField = InventoryCraftResult.class.getDeclaredField("field_70467_a");
                stackResultField.setAccessible(true);
                ItemStack[] outputSlotItem = (ItemStack[]) stackResultField.get(outputSlot);
                outputSlotItem[0] =  new ItemStack(Item.getItemById(339)).setStackDisplayName(input);

                stackResultField.set(outputSlot, outputSlotItem);

                Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.openContainer.windowId, 2, 0, 0, Minecraft.getMinecraft().thePlayer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
