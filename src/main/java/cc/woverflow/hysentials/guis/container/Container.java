package cc.woverflow.hysentials.guis.container;

import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.event.EventBus;
import cc.woverflow.hysentials.event.events.GuiMouseClickEvent;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class Container extends InventoryBasic {
    private String title;
    private int rows = 1;
    private final Map<Integer, GuiItem> guiItems;
    private static Map<Integer, GuiAction> slotActions;
    private static GuiChest guiChest;
    private static boolean isOpen;
    private static GuiAction defaultAction;

    public Container(String title, int rows) {
        super(title, true, rows * 9);
        this.title = title;
        this.rows = rows;
        guiItems = new HashMap<>();
        slotActions = new HashMap<>();
        EventBus.INSTANCE.register(this);
    }

    public void setItem(int slot, GuiItem item) {
        guiItems.put(slot, item);
    }

    public void setAction(int slot, GuiAction action) {
        slotActions.put(slot, action);
    }

    public void setDefaultAction(GuiAction action) {
        defaultAction = action;
    }

    public abstract void setItems();

    public abstract void handleMenu(MouseEvent event);

    public abstract void setClickActions();

    public void open(@NotNull EntityPlayer owner) {
        setItems();
        for (Map.Entry<Integer, GuiItem> entry : guiItems.entrySet()) {
            Integer slot = entry.getKey();
            GuiItem item = entry.getValue();
            setInventorySlotContents(slot, item.getItemStack());
        }
        setClickActions();
        isOpen = true;
        guiChest = new GuiChest(owner.inventory, this) {
            @Override
            public void onGuiClosed() {
                super.onGuiClosed();
                isOpen = false;
                guiChest = null;
            }
        };
        Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(guiChest);
    }

    @SubscribeEvent
    public void mouseClick(GuiMouseClickEvent event) {
        if (!isOpen) return;
        Slot s = guiChest.getSlotUnderMouse();
        if (s == null) return;
        int slot = s.getSlotIndex();
        defaultAction.execute(new GuiAction.GuiClickEvent(event.getCi(), slot, guiChest.inventorySlots.inventoryItemStacks.get(slot), event.getButton()));
        if (slotActions.containsKey(slot)) {
            GuiAction action = slotActions.get(slot);
            action.execute(new GuiAction.GuiClickEvent(event.getCi(), slot, guiChest.inventorySlots.inventoryItemStacks.get(slot), event.getButton()));
        }
    }
}
