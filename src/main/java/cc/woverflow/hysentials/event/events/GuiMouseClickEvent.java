package cc.woverflow.hysentials.event.events;

import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class GuiMouseClickEvent extends Event {
    private int x;
    private int y;
    private int button;
    private CallbackInfo ci;

    public GuiMouseClickEvent(int x, int y, int button, CallbackInfo ci) {
        this.x = x;
        this.y = y;
        this.button = button;
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
}
