package llc.redstone.hysentials.handlers

import llc.redstone.hysentials.config.hysentialmods.ScorebarsConfig
import llc.redstone.hysentials.handlers.sbb.Scoreboard
import net.minecraftforge.client.GuiIngameForge
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.GL11

class SbbRenderer {
    @SubscribeEvent
    fun renderOverlay(event: RenderGameOverlayEvent.Pre) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return
        GL11.glPushMatrix()
        try {
            if (ScorebarsConfig.scoreboard && ScorebarsConfig.showScoreboard) {
                Scoreboard.scoreboard()
            } else {
                GuiIngameForge.renderObjective = ScorebarsConfig.showScoreboard
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        GL11.glPopMatrix()
    }
}