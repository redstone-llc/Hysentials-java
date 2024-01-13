package llc.redstone.hysentials.handlers

import cc.polyfrost.oneconfig.libs.universal.ChatColor.Companion.stripControlCodes
import cc.polyfrost.oneconfig.libs.universal.UChat.chat
import llc.redstone.hysentials.GuiIngameHysentials
import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.config.HysentialsConfig
import llc.redstone.hysentials.guis.actionLibrary.ClubActionViewer
import llc.redstone.hysentials.guis.actionLibrary.ClubActionViewer.*
import llc.redstone.hysentials.guis.sbBoxes.SBBoxesEditor
import llc.redstone.hysentials.guis.utils.SBBoxes
import llc.redstone.hysentials.handlers.redworks.HousingScoreboard
import llc.redstone.hysentials.handlers.sbb.Actionbar
import llc.redstone.hysentials.handlers.sbb.SbbRenderer
import llc.redstone.hysentials.handlers.sbb.Scoreboard
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
            Actionbar.actionBar()
            if (HysentialsConfig.scoreboard && HysentialsConfig.showScoreboard) {
                Scoreboard.scoreboard()
            } else {
                GuiIngameHysentials.renderObjective = HysentialsConfig.showScoreboard
            }
            val lines = ScoreboardWrapper.getScoreboard().getSortedScores(
                ScoreboardWrapper.getSidebar()).stream()
                .map { score: Score? ->
                    ScoreWrapper(
                        score
                    )
                }.collect(
                    Collectors.toCollection { ArrayList() }
                )
            for (line in lines) {
                val s = SBBoxesEditor.removeHiddenCharacters(line.toString())
                val box = SBBoxes.getFromMatch(s) ?: continue

                box.apply {
                    if (!isEnabled) return@apply
                    if (title != "" && stripControlCodes(ScoreboardWrapper.getTitle()) != title) return@apply
                    text = display
                    if (text == "") return@apply
                    draw()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        GL11.glPopMatrix()
    }
}