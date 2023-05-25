package cc.woverflow.hysentials.handlers.display;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class GuiDisplayHandler {

    private GuiScreen displayNextTick;

    public void setDisplayNextTick(GuiScreen displayNextTick) {
        this.displayNextTick = displayNextTick;
    }

    @SubscribeEvent
    public void tick(TickEvent event) {
        if (displayNextTick != null) {
            Minecraft.getMinecraft().displayGuiScreen(displayNextTick);
            displayNextTick = null;
        }
    }
}

