package llc.redstone.hysentials.event.events;

import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class GuiMouseClickEvent extends Event {
    private int x;
    private int y;
    private int button;
    private CallbackInfo ci;

    private Integer slot;

    public GuiMouseClickEvent(int x, int y, int button, CallbackInfo ci) {
        this.x = x;
        this.y = y;
        this.button = button;
        this.ci = ci;
    }

    public GuiMouseClickEvent(int slot, CallbackInfo ci) {
        this.slot = slot;
        this.ci = ci;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getButton() {
        return button;
    }

    public CallbackInfo getCi() {
        return ci;
    }

    public Integer getSlot() {
        return slot;
    }
}
