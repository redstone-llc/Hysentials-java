package llc.redstone.hysentials.cosmetic

import llc.redstone.hysentials.cosmetic.CosmeticManager.drawItem
import llc.redstone.hysentials.cosmetic.CosmeticManager.drawSlot
import llc.redstone.hysentials.cosmetic.CosmeticManager.purchaseCosmetic
import llc.redstone.hysentials.schema.HysentialsSchema
import llc.redstone.hysentials.updategui.Button
import llc.redstone.hysentials.util.Renderer
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.inventory.Slot
import net.minecraft.util.ResourceLocation

class CosmeticPurchaseConfirm {
    var confirmBackground = ResourceLocation("hysentials:gui/wardrobe/confirm.png")
    val mc = Minecraft.getMinecraft()!!
    lateinit var parent: CosmeticGui
    var open = false
    var cosmetic: HysentialsSchema.Cosmetic? = null
    val x = (20 + (200 - 100) / 2).toDouble()
    val y = (20 + (106 - 32) / 2).toDouble()
    var slot = Slot(CosmeticPurchaseInventory(), 0, 45 + x.toInt(), 23 + y.toInt())

    lateinit var buttons: List<Button>

    fun draw(mouseX: Int, mouseY: Int) {
        if (open) {
            Renderer.drawImage(confirmBackground, x, y, 100.0, 32.0)
            buttons.forEach {
                it.draw(mouseX, mouseY)
            }
            slot.xDisplayPosition = 45 + x.toInt() + 100 / 2
            slot.yDisplayPosition = 23 + y.toInt() + 32 / 2
            drawItem(slot, cosmetic!!)
        }
    }

    fun click(mouseX: Double, mouseY: Double, mouseButton: Int) {
        if (open) {
            buttons.forEach { it.click(mouseX, mouseY, mouseButton) }
        }
    }

    fun open(cosmetic: HysentialsSchema.Cosmetic, parent: CosmeticGui) {
        this.cosmetic = cosmetic
        this.parent = parent
        this.buttons = arrayListOf(
            Button(30 + x.toInt(), 19 + y.toInt(), 15, 12, "hysentials:gui/wardrobe/check.png",
                parent,
                onHover = { _, _ -> open }) { _, _, _ ->
                if (open) {
                    open = false
                    purchaseCosmetic(cosmetic.name)
                    parent.initScreen(parent.width, parent.height)
                    this.mc.soundHandler.playSound(
                        PositionedSoundRecord.create(
                            ResourceLocation("gui.button.press"),
                            1.0f
                        )
                    )
                }
            },
            Button(55 + x.toInt(), 19 + y.toInt(), 15, 12, "hysentials:gui/wardrobe/cross.png",
                parent,
                onHover = { _, _ -> open }) { _, _, _ ->
                if (open) {
                    open = false
                    this.mc.soundHandler.playSound(
                        PositionedSoundRecord.create(
                            ResourceLocation("gui.button.press"),
                            1.0f
                        )
                    )
                }
            }
        )
        open = true
        slot.putStack(cosmetic.item)
    }

    fun close() {
        open = false
    }
}