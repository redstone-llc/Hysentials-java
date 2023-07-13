package cc.woverflow.hysentials.guis.container;

import cc.woverflow.hysentials.event.events.GuiMouseClickEvent;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static cc.woverflow.hysentials.guis.container.Container.INSTANCE;

public class ContainerHandler {
    @SubscribeEvent
    public void mouseClick(GuiMouseClickEvent event) {
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
}
