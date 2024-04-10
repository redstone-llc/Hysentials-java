package llc.redstone.hysentials.event.events;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.Event;

public class GuiLoadedEvent extends Event {
    public GuiScreen gui;
    public String name;

    public GuiLoadedEvent(GuiScreen gui, String name) {
        this.gui = gui;
        this.name = name;
    }
}
