package cc.woverflow.hysentials.handlers.htsl;

import cc.woverflow.hysentials.event.events.GuiMouseClickEvent;
import cc.woverflow.hysentials.guis.actionLibrary.ActionLibrary;
import cc.woverflow.hysentials.guis.container.GuiItem;
import cc.woverflow.hysentials.util.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import static cc.woverflow.hysentials.handlers.guis.GameMenuOpen.field_lowerChestInventory;

public class HousingMenuHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void onGuiRender(GuiScreenEvent.BackgroundDrawnEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().thePlayer.openContainer == null)
            return;
        if (!isInHousingMenu()) return;
        Slot slot = Minecraft.getMinecraft().thePlayer.openContainer.getSlot(22);
        if (!slot.getHasStack()) {
            ItemStack item = GuiItem.makeColorfulItem(Material.STORAGE_MINECART, "&aAction Library", 1, 0, "&7Opens the Action Library", "&7GUI.", "", "&eLeft-Click to browse!");
            slot.putStack(item);
        }
    }

    @SubscribeEvent
    public void onMouseClick(GuiMouseClickEvent event) {
        if (!isInHousingMenu()) return;
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen == null) return;
        if (screen instanceof GuiChest) {
            Slot slot = ((GuiChest) screen).getSlotUnderMouse();
            if (slot == null) return;
            if (slot.getSlotIndex() == 22) {
                event.getCi().cancel();
                Minecraft.getMinecraft().thePlayer.closeScreen();
                new ActionLibrary().open(Minecraft.getMinecraft().thePlayer);
            }
        }
    }

    public static boolean isInHousingMenu() {
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChest) {
            GuiChest chest = (GuiChest) Minecraft.getMinecraft().currentScreen;
            try {
                if (field_lowerChestInventory.get(chest) instanceof IInventory) {
                    IInventory inventory = (IInventory) field_lowerChestInventory.get(chest);
                    if (inventory.getName().equals("Housing Menu") && inventory.getSizeInventory() == 45) {
                        return true;
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }
}
