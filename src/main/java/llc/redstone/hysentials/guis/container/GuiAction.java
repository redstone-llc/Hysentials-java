package llc.redstone.hysentials.guis.container;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@FunctionalInterface
public interface GuiAction {
    void execute(GuiClickEvent event);

    public static class GuiClickEvent{
        CallbackInfo event;
        int slot;
        ItemStack itemStack;
        int button;

        public GuiClickEvent(CallbackInfo event, int slot, ItemStack itemStack, int button) {
            this.event = event;
            this.slot = slot;
            this.itemStack = itemStack;
            this.button = button;
        }

        public CallbackInfo getEvent() {
            return event;
        }

        public int getSlot() {
            return slot;
        }

        public ItemStack getItemStack() {
            return itemStack;
        }

        public int getButton() {
            return button;
        }
    }
}
