package llc.redstone.hysentials.hyphone

import cc.polyfrost.oneconfig.libs.universal.UChat
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import cc.polyfrost.oneconfig.libs.universal.UScreen
import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.cosmetic.CosmeticGui
import llc.redstone.hysentials.updateGui.Button
import llc.redstone.hysentials.updateGui.HysentialsGui
import llc.redstone.hysentials.guis.misc.HysentialsLevel
import llc.redstone.hysentials.util.ImageIconRenderer
import llc.redstone.hysentials.util.Renderer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation

class HyPhoneGUI : UScreen(), HysentialsGui {
    companion object {
        var instance: HyPhoneGUI? = null
        var buttons = ArrayList<Button>()
    }

    private var xSize = 168
    private var ySize = 172
    private var guiLeft = 0
    private var guiTop = 0
    var background = ResourceLocation("hysentials:gui/button_menu_main.png")

    var fontRenderer: ImageIconRenderer? = Hysentials.INSTANCE.imageIconRenderer
    var fontRendererObj: FontRenderer = Minecraft.getMinecraft().fontRendererObj

    override fun onDrawScreen(matrixStack: UMatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.onDrawScreen(matrixStack, mouseX, mouseY, partialTicks)
        GlStateManager.pushMatrix()
        drawDefaultBackground()

        try {
            Renderer.translate(guiLeft.toDouble(), guiTop.toDouble(), 0.0)

            Renderer.drawImage(
                background,
                0.0,
                0.0,
                xSize.toDouble(),
                ySize.toDouble()
            )

            buttons.forEach { it.draw(mouseX, mouseY) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Renderer.untranslate(0.0, 0.0, 0.0)
        GlStateManager.popMatrix()
    }

    override fun onMouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int) {
        super.onMouseClicked(mouseX, mouseY, mouseButton)
        buttons.forEach { it.click(mouseX, mouseY, mouseButton) }
    }

    override fun initScreen(width: Int, height: Int) {
        instance = this
        super.initScreen(width, height)
        guiLeft = (Renderer.screen.getWidth() - xSize) / 2
        guiTop = (Renderer.screen.getHeight() - ySize) / 2

        buttons.clear()
        buttons.let {
            it.add(Button(4, 28, 70, 52, "hysentials:gui/hysentials_level.png", instance) { _, _, _ ->
                Minecraft.getMinecraft().thePlayer.closeScreen()
                HysentialsLevel(0).open()
            })
            it.add(Button(4, 82, 70, 52, "hysentials:gui/game_menu.png", instance) { _, _, _ ->
                UChat.chat("&cComing soon!")
            })
            it.add(Button(76, 28, 88, 34, "hysentials:gui/config_menu.png", instance) { _, _, _ ->
                Minecraft.getMinecraft().thePlayer.closeScreen()
                Hysentials.INSTANCE.config.openGui()
            })
            it.add(Button(76, 64, 88, 34, "hysentials:gui/cosmetic_menu.png", instance) { _, _, _ ->
                Minecraft.getMinecraft().thePlayer.closeScreen()
                Hysentials.INSTANCE.guiDisplayHandler.setDisplayNextTick(CosmeticGui())
            })
            it.add(Button(76, 100, 88, 34, "hysentials:gui/coming_soon.png", instance) { _, _, _ ->
                UChat.chat("&cComing soon!")
            })
//            it.add(Button(76, 144, 17, 17, null, instance) { _, _, _ ->
//                //nothing for now
//            })
            it
        }
    }

    override fun getTop(): Int {
        return guiTop
    }

    override fun getLeft(): Int {
        return guiLeft
    }
}