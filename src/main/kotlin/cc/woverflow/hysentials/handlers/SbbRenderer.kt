package cc.woverflow.hysentials.handlers

import cc.polyfrost.oneconfig.libs.universal.ChatColor.Companion.stripControlCodes
import cc.polyfrost.oneconfig.libs.universal.UChat.chat
import cc.woverflow.hysentials.Hysentials
import cc.woverflow.hysentials.config.HysentialsConfig
import cc.woverflow.hysentials.guis.actionLibrary.ClubActionViewer
import cc.woverflow.hysentials.guis.actionLibrary.ClubActionViewer.*
import cc.woverflow.hysentials.guis.sbBoxes.SBBoxesEditor
import cc.woverflow.hysentials.guis.utils.SBBoxes
import cc.woverflow.hysentials.handlers.redworks.HousingScoreboard
import cc.woverflow.hysentials.handlers.sbb.Actionbar
import cc.woverflow.hysentials.handlers.sbb.SbbRenderer
import cc.woverflow.hysentials.handlers.sbb.Scoreboard
import cc.woverflow.hysentials.util.Renderer
import cc.woverflow.hysentials.util.ScoreboardWrapper
import cc.woverflow.hysentials.util.ScoreboardWrapper.ScoreWrapper
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
                GuiIngameForge.renderObjective = HysentialsConfig.showScoreboard
            }
            val lines = ScoreboardWrapper.getScoreboard().getSortedScores(ScoreboardWrapper.getSidebar()).stream()
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