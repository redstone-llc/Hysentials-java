package llc.redstone.hysentials.event.events;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.Event;

public class GuiKeyboardEvent extends Event {
    public int key;
    public char character;
    public GuiScreen gui;

    public GuiKeyboardEvent(char character, int key, GuiScreen gui) {
        this.key = key;
        this.character = character;
        this.gui = gui;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
