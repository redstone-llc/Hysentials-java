package cc.woverflow.hysentials.event.events;

import akka.actor.Cancellable;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderItemInGuiEvent extends Event {
    ItemStack stack;
    int xPosition;
    int yPosition;

    public RenderItemInGuiEvent(ItemStack stack, int xPosition, int yPosition) {
        this.stack = stack;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public ItemStack getStack() {
        return stack;
    }

    public int getXPosition() {
        return xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
