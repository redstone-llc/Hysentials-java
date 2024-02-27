package llc.redstone.hysentials.macrowheel.overlay

import cc.polyfrost.oneconfig.config.annotations.Color
import cc.polyfrost.oneconfig.config.annotations.Switch
import cc.polyfrost.oneconfig.config.core.OneColor
import cc.polyfrost.oneconfig.hud.Hud
import cc.polyfrost.oneconfig.hud.Position.AnchorPosition
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import cc.polyfrost.oneconfig.renderer.NanoVGHelper
import cc.polyfrost.oneconfig.utils.InputHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraftforge.client.event.GuiScreenEvent
import org.lwjgl.input.Mouse

var background = false
var border = false

/**
 * @param enabled      If the hud is enabled
 * @param x            X-coordinate of hud on a 1080p display
 * @param y            Y-coordinate of hud on a 1080p display
 * @param scale        Scale of the hud
 * @param background   If the HUD should have a background
 * @param bgColor      Background color
 */
class MacroWheelHudConfigThing(
    enabled: Boolean,
    x: Float,
    y: Float,
    scale: Float,
    @Switch(
        name = "Background",
        description = "If the background of the HUD is enabled",
        category = "HUD",
        subcategory = "Macro Wheel"
    )
    var background: Boolean,
    @Color(
        name = "Background Color:",
        description = "The color of the background of the HUD",
        category = "HUD",
        subcategory = "Macro Wheel"
    ) var bgColor: OneColor,
): Hud(enabled, x, y, scale) {
    init {
    }

    override fun draw(matrices: UMatrixStack?, x: Float, y: Float, scale: Float, example: Boolean) {
        NanoVGHelper.INSTANCE.setupAndDraw(true) {
            MacroWheelOverlay(x + 0.5f, y + 0.5f, scale, bgColor).draw(
                it, 0f, InputHandler()
            )
        }
    }

    override fun drawAll(matrices: UMatrixStack?, example: Boolean) {
        if (!example && !shouldShow()) return
        preRender(example)
        position.setSize(getWidth(scale, example), getHeight(scale, example))
        draw(matrices, position.x, position.y, scale, example)
    }

    override fun getWidth(scale: Float, example: Boolean): Float {
        return 34f * scale
    }

    override fun getHeight(scale: Float, example: Boolean): Float {
        return 34f * scale
    }

    override fun shouldShow(): Boolean {
        return false
    }

}