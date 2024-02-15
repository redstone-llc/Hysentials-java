package llc.redstone.hysentials.handlers.sbb;

import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.config.hysentialMods.ScorebarsConfig;
import llc.redstone.hysentials.util.Renderer;
import llc.redstone.hysentials.config.HysentialsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Actionbar {
    public static String actionBarMessage = "";
    public static long lastUpdated = System.currentTimeMillis();


    @SubscribeEvent
    public void onMessageReceived(ClientChatReceivedEvent event) {
        if (((int) event.type) != 2) return;
        if (ScorebarsConfig.actionBar) {
            event.setCanceled(true);
            actionBarMessage = event.message.getFormattedText();
            lastUpdated = System.currentTimeMillis();
        }
    }

    public static void actionBar() {
        if (actionBarMessage.equals("") || System.currentTimeMillis() > lastUpdated + 2200)
            return;
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        float width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(actionBarMessage);
        float x = res.getScaledWidth() / 2 - width / 2 - 1;
        float y = res.getScaledHeight() - 78;
        int padding = 12;

        SbbRenderer.drawBox(
            x - padding / 2,
            y - 5,
            width + padding,
            18,
            ScorebarsConfig.boxColor,
            ScorebarsConfig.boxShadows,
            new Integer[]{0, 2, 4}[ScorebarsConfig.actionBarBorderRadius]
        );
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(actionBarMessage, x, y, (int) Renderer.color(255, 255, 255, 255));

    }
}
