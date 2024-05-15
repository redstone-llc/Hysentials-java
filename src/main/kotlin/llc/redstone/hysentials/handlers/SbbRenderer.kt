package llc.redstone.hysentials.handlers

import cc.polyfrost.oneconfig.libs.universal.ChatColor.Companion.stripControlCodes
import cc.polyfrost.oneconfig.libs.universal.UChat.chat
import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.config.HysentialsConfig
import llc.redstone.hysentials.config.hysentialMods.ScorebarsConfig
import llc.redstone.hysentials.guis.sbBoxes.SBBoxesEditor
import llc.redstone.hysentials.guis.utils.SBBoxes
import llc.redstone.hysentials.handlers.redworks.HousingScoreboard
import llc.redstone.hysentials.handlers.sbb.Actionbar
import llc.redstone.hysentials.handlers.sbb.SbbRenderer
import llc.redstone.hysentials.handlers.sbb.Scoreboard
import llc.redstone.hysentials.util.C
import llc.redstone.hysentials.util.Renderer
import llc.redstone.hysentials.util.ScoreboardWrapper
import llc.redstone.hysentials.util.ScoreboardWrapper.ScoreWrapper
import net.minecraft.client.Minecraft
import net.minecraft.scoreboard.Score
import net.minecraftforge.client.GuiIngameForge
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.GL11
import java.util.function.Supplier
import java.util.stream.Collectors

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