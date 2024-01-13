package llc.redstone.hysentials.guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ResolutionUtil {
    private static ScaledResolution resolution;

    public static ScaledResolution current() {
        return resolution != null ? resolution : (resolution = new ScaledResolution(Minecraft.getMinecraft()));
    }

    @SubscribeEvent
    public void tick(TickEvent event) {
        resolution = null;
    }
}
