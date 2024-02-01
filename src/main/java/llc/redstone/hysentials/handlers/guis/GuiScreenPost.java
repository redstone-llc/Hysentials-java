package llc.redstone.hysentials.handlers.guis;

import cc.polyfrost.oneconfig.utils.gui.OneUIScreen;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.utils.OneScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiScreenPost {
    @SubscribeEvent
    public void onGuiPost(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (event.gui instanceof OneScreen) {
            OneScreen screen = ((OneScreen) event.gui);
            screen.drawPost(event);
        }
    }
}
