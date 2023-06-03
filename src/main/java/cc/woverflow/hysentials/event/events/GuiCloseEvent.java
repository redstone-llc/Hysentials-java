package cc.woverflow.hysentials.event.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.Event;

public class GuiCloseEvent extends Event {
    public GuiScreen gui;

    public GuiCloseEvent(GuiScreen gui) {
        this.gui = gui;
    }

}
