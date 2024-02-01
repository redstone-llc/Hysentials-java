package llc.redstone.hysentials.cosmetic

import cc.polyfrost.oneconfig.libs.universal.UKeyboard
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import cc.polyfrost.oneconfig.libs.universal.UScreen
import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.config.HysentialsConfig
import llc.redstone.hysentials.updateGui.Button
import llc.redstone.hysentials.updateGui.HysentialsGui
import llc.redstone.hysentials.guis.container.GuiItem
import llc.redstone.hysentials.schema.HysentialsSchema
import llc.redstone.hysentials.util.*
import llc.redstone.hysentials.utils.formatCapitalize
import llc.redstone.hysentials.utils.splitToWords
import llc.redstone.hysentials.websocket.Socket
import com.google.common.collect.Lists
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.resources.model.IBakedModel
import net.minecraft.entity.EntityLivingBase
import net.minecraft.init.Items
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.ResourceLocation
import org.json.JSONObject
import java.text.DecimalFormat
import java.util.*
import java.util.stream.Collectors


//TODO REWRITE THIS - ITS TERRIBLE
open class CosmeticGui : UScreen(), HysentialsGui {
    companion object {
        var instance: CosmeticGui? = null
        var buttons = ArrayList<Button>()

        fun equippedCosmetic(uuid: UUID, name: String): Boolean {
            try {
                val cosmetics = BlockWAPIUtils.getCosmetics()
                cosmetics.find { it.name == name }?.let {
                    if (it.equipped.contains(uuid.toString())) {
                        return true
                    }
                }
            } catch (_: Exception) {
            }
            return false
        }

        fun hasCosmetic(uuid: UUID, name: String): Boolean {
            try {
                val cosmetics = BlockWAPIUtils.getCosmetics()
                cosmetics.find { it.name == name }?.let {
                    if (it.users.contains(uuid.toString())) {
                        return true
                    }
                }
            } catch (_: Exception) {
            }
            return false
        }

        fun getOwnedCosmetics(uuid: UUID): ArrayList<HysentialsSchema.Cosmetic> {
            val cosmetics = BlockWAPIUtils.getCosmetics()
            val ownedCosmetics = ArrayList<HysentialsSchema.Cosmetic>()
            for (cosmetic in cosmetics) {
                if (cosmetic.users.contains(uuid.toString())) {
                    ownedCosmetics.add(cosmetic)
                }
            }
            return ownedCosmetics
        }

        fun getEquippedCosmetics(uuid: UUID): ArrayList<HysentialsSchema.Cosmetic> {
            val cosmetics = BlockWAPIUtils.getCosmetics()
            val equippedCosmetics = ArrayList<HysentialsSchema.Cosmetic>()
            for (cosmetic in cosmetics) {
                if (cosmetic.equipped.contains(uuid.toString())) {
                    equippedCosmetics.add(cosmetic)
                }
            }
            return equippedCosmetics
        }

        fun colorFromRarity(rarity: String): String {
            return when (rarity) {
                "COMMON" -> "#828282"
                "RARE" -> "#0099DB"
                "EPIC" -> "#8B5CF6"
                "LEGENDARY" -> "#F49E0B"
                "EXCLUSIVE" -> "#EA323C"
                else -> "#FFFFFF"
            }
        }

        fun indexFromRarity(rarity: String): Int {
            return when (rarity) {
                "COMMON" -> 0
                "RARE" -> 1
                "EPIC" -> 2
                "LEGENDARY" -> 3
                "EXCLUSIVE" -> 4
                else -> 0
            }
        }

        var search: String = ""
        var page: Int = 1
        var maxPage: Int = 1
        var type: String = "owned"
        var inventorySlots: ArrayList<Slot> = Lists.newArrayList()
    }

    var xSize = 290
    var ySize = 164
    protected var guiLeft = 0
    protected var guiTop = 0

    var inventory: CosmeticInventory? = null
    var theSlot: Slot? = null
    var inventoryMap: HashMap<String, ArrayList<HysentialsSchema.Cosmetic>> = HashMap()
    var cosmeticBackground = ResourceLocation("hysentials:gui/wardrobe/background.png")
    var lightBackground = ResourceLocation("hysentials:gui/wardrobe/background-light.png")
    var selectedSlot = ResourceLocation("hysentials:gui/wardrobe/selected_slot.png")

    // Tabs
    var ownedTab = ResourceLocation("hysentials:gui/wardrobe/tab/owned.png")
    var headTab = ResourceLocation("hysentials:gui/wardrobe/tab/headwear.png")
    var backTab = ResourceLocation("hysentials:gui/wardrobe/tab/chestwear.png")
    var pantaloonsTab = ResourceLocation("hysentials:gui/wardrobe/tab/pantaloons.png")
    var bootsTab = ResourceLocation("hysentials:gui/wardrobe/tab/boots.png")
    var petsTab = ResourceLocation("hysentials:gui/wardrobe/tab/pets.png")
    var chatTab = ResourceLocation("hysentials:gui/wardrobe/tab/chatbox.png")
    var bundlesTab = ResourceLocation("hysentials:gui/wardrobe/tab/bundles.png")


    var mcFive = HysentialsFontRenderer("Minecraft Five", 12f)
    var fontRenderer: ImageIconRenderer? = Hysentials.INSTANCE.imageIconRenderer

    var paginationList: PaginationList<HysentialsSchema.Cosmetic>? = null

    var focused: Boolean = false
    var blinkTimer: Int = 0

    val input: Input = Input(
        0,
        0,
        0,
        0
    ).let {
        it.setEnabled(true)
        it.isFocused = true
        it
    }

    val soundHandler = Minecraft.getMinecraft().soundHandler

    var xAngle = 0f
    var yAngle = 0f
    var isDragging = false
    var dragPos = Pair(0.0, 0.0)

    override fun onDrawScreen(matrixStack: UMatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.onDrawScreen(matrixStack, mouseX, mouseY, partialTicks)
        this.theSlot = null
        GlStateManager.pushMatrix()
        this.drawDefaultBackground()

        if (isDragging) {
            xAngle += (mouseX - dragPos.first).toFloat() * 0.5f
            yAngle += (mouseY - dragPos.second).toFloat() * 0.5f
            dragPos = Pair(mouseX.toDouble(), mouseY.toDouble())

            if (yAngle > 90) {
                yAngle = 90f
            } else if (yAngle < -90) {
                yAngle = -90f
            }
        }
        try {
            if (HysentialsConfig.wardrobeDarkMode) {
                Renderer.drawImage(
                    cosmeticBackground,
                    guiLeft.toDouble(),
                    guiTop.toDouble(),
                    xSize.toDouble(),
                    ySize.toDouble()
                )
            } else {
                Renderer.drawImage(
                    lightBackground,
                    guiLeft.toDouble(),
                    guiTop.toDouble(),
                    xSize.toDouble(),
                    ySize.toDouble()
                )
            }

            val emerald = Socket.cachedUser?.emeralds ?: 0

            when {
                type === "owned" -> {
                    Renderer.drawImage(
                        ownedTab,
                        guiLeft + 5.0,
                        guiTop + 4.0,
                        10.0,
                        10.0
                    )
                }

                type === "head" -> {
                    Renderer.drawImage(
                        headTab,
                        guiLeft + 5.0,
                        guiTop + 22.0,
                        9.0,
                        10.0
                    )
                }

                type === "back" -> {
                    Renderer.drawImage(
                        backTab,
                        guiLeft + 5.0,
                        guiTop + 38.0,
                        9.0,
                        9.0
                    )
                }

                type === "pantaloons" -> {
                    Renderer.drawImage(
                        pantaloonsTab,
                        guiLeft + 5.0,
                        guiTop + 53.0,
                        9.0,
                        11.0
                    )
                }

                type === "boots" -> {
                    Renderer.drawImage(
                        bootsTab,
                        guiLeft + 4.0,
                        guiTop + 70.0,
                        11.0,
                        7.0
                    )
                }

                type === "pet" -> {
                    Renderer.drawImage(
                        petsTab,
                        guiLeft + 5.0,
                        guiTop + 83.0,
                        9.0,
                        10.0
                    )
                }

                type === "chat" -> {
                    Renderer.drawImage(
                        chatTab,
                        guiLeft + 5.0,
                        guiTop + 99.0,
                        9.0,
                        7.0
                    )
                }

                type === "bundle" -> {
                    Renderer.drawImage(
                        bundlesTab,
                        guiLeft + 5.0,
                        guiTop + 112.0,
                        9.0,
                        10.0
                    )
                }
            }

            var typeFinal = type
            if (typeFinal === "back") {
                typeFinal = "chest wear"
            }
            if (typeFinal === "head") {
                typeFinal = "head wear"
            }
            if (typeFinal === "pet") {
                typeFinal = "pets"
            }
            mcFive.drawString(
                " > ${typeFinal.splitToWords().uppercase()} ($page/$maxPage)",
                guiLeft + 72f,
                guiTop + 5f,
                0x1b1a18
            )
            mcFive.drawString(
                " > ${typeFinal.splitToWords().uppercase()} ($page/$maxPage)",
                guiLeft + 71f,
                guiTop + 5f,
                0xFFFFFF
            )


            var largeFormat = DecimalFormat("#,###")
            mcFive.drawString(
                " ${largeFormat.format(emerald)}",
                guiLeft + 72f,
                guiTop + 149f,
                0x153F15
            )
            mcFive.drawString(
                " ${largeFormat.format(emerald)}",
                guiLeft + 71f,
                guiTop + 149f,
                0x55FF55
            )

            GlStateManager.enableLighting()
            GlStateManager.enableDepth()
            RenderHelper.enableStandardItemLighting()
            GlStateManager.enableRescaleNormal()
            drawEntityOnScreen(
                guiLeft + 256,
                guiTop + 124,
                40,
                xAngle,
                -yAngle,
                mc.thePlayer
            )

            for (slot in inventorySlots) {
                drawSlot(slot, mouseX, mouseY, partialTicks)
                if (getSlot(
                        mouseX.toFloat() - guiLeft,
                        mouseY.toFloat() - guiTop
                    ) == slot.slotIndex && slot.canBeHovered()
                ) {
                    theSlot = slot
                    GlStateManager.disableLighting()
                    GlStateManager.disableDepth()
                    val j1 = slot.xDisplayPosition
                    val l2 = slot.yDisplayPosition
                    GlStateManager.colorMask(true, true, true, false)
                    drawGradientRect(j1, l2, j1 + 25, l2 + 26, -2130706433, -2130706433)
                    GlStateManager.colorMask(true, true, true, true)
                    GlStateManager.enableLighting()
                    GlStateManager.enableDepth()
                }
            }
            val inventoryplayer = mc.thePlayer.inventory
            if (inventoryplayer.itemStack == null && theSlot != null && theSlot!!.hasStack) {
                val itemstack1 = theSlot!!.stack
                val list: MutableList<String> =
                    itemstack1.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips)

                for (i in list.indices) {
                    if (i == 0) {
                        list[i] = itemstack1.rarity.rarityColor.toString() + list[i]
                    } else {
                        list[i] = EnumChatFormatting.GRAY.toString() + list[i]
                    }
                }

                this.drawHoveringText(list, mouseX, mouseY, fontRenderer)
            }

            var rX = mouseX.toFloat() - guiLeft
            var rY = mouseY.toFloat() - guiTop
            when {
                rX in 5.0..15.0 && rY in 4.0..14.0 && type !== "owned" -> {
                    drawHoveringText(listOf("§8➔ <#32a852>Owned Cosmetics"), mouseX, mouseY, fontRenderer)
                }

                rX in 5.0..14.0 && rY in 22.0..31.0 && type !== "head" -> {
                    drawHoveringText(listOf("§8➔ <#1787e3>Headwear"), mouseX, mouseY, fontRenderer)
                }

                rX in 5.0..14.0 && rY in 38.0..47.0 && type !== "back" -> {
                    drawHoveringText(listOf("§8➔ <#e69927>Chestwear"), mouseX, mouseY, fontRenderer)
                }

                rX in 5.0..14.0 && rY in 53.0..64.0 && type !== "pantaloons" -> {
                    drawHoveringText(listOf("§8➔ <#8327e6>Bottomwear"), mouseX, mouseY, fontRenderer)
                }

                rX in 4.0..15.0 && rY in 70.0..77.0 && type !== "boots" -> {
                    drawHoveringText(listOf("§8➔ <#ea323b>Shoes"), mouseX, mouseY, fontRenderer)
                }

                rX in 5.0..14.0 && rY in 83.0..93.0 && type !== "pet" -> {
                    drawHoveringText(listOf("§8➔ <#e4ea32>Pets"), mouseX, mouseY, fontRenderer)
                }

                rX in 5.0..14.0 && rY in 99.0..106.0 && type !== "chat" -> {
                    drawHoveringText(listOf("§8➔ <#32eade>Chatting"), mouseX, mouseY, fontRenderer)
                }

                rX in 5.0..14.0 && rY in 112.0..122.0 && type !== "bundle" -> {
                    drawHoveringText(listOf("§8➔ <#e832e6>Bundles"), mouseX, mouseY, fontRenderer)
                }

                rX in 277.0..284.0 && rY in 5.0..11.0 -> {
                    drawHoveringText(
                        listOf(
                            "§aEmeralds",
                            "§8Currency",
                            "",
                            "§7Hysentials uses Emeralds as",
                            "§7the main currency of the mod.",
                            "§7Useful for things like quest",
                            "§7rerolls cosmetic purchases,",
                            "§7trading, and more! Emeralds are",
                            "§7obtained by playing games and ",
                            "§7earning small increments at a time",
                            "§7(Winning a game will earn more)",
                            "§7or by purchasing them on our website,",
                            "§7at §9§nwww.redstone.llc/store§7."
                        ), mouseX, mouseY, fontRenderer
                    )
                }

                rX in 261.0..268.0 && rY in 5.0..11.0 -> {
                    drawHoveringText(
                        listOf(
                            "§fOwned Cosmetics: §a${getOwnedCosmetics(mc.thePlayer.uniqueID).size}§7/§8${BlockWAPIUtils.getCosmetics().size}",
                            "§fAmount Spent: §a${largeFormat.format(Socket.cachedUser.amountSpent ?: 0)} emeralds",
                        ), mouseX, mouseY, fontRenderer
                    )
                }

                rX in 245.0..254.0 && rY in 5.0..12.0 -> {
                    if (HysentialsConfig.wardrobeDarkMode) {
                        drawHoveringText(listOf("Turn on Light Mode."), mouseX, mouseY)
                    } else {
                        drawHoveringText(listOf("Turn on Dark Mode."), mouseX, mouseY)
                    }
                }
            }

            if (focused && ++blinkTimer >= 60) {
                if (blinkTimer >= 120) blinkTimer = 0
                mcFive.drawString(
                    "|",
                    guiLeft + 61f + (if (search.isEmpty()) 0f else mcFive.getWidth(search.uppercase()) + 4f),
                    guiTop + 131f,
                    0xFFFFFF
                )
            }

            mcFive.drawString(search.uppercase(), guiLeft + 62f, guiTop + 131f, 0x1b1a18)
            mcFive.drawString(search.uppercase(), guiLeft + 61f, guiTop + 131f, 0xFFFFFF)

            Renderer.translate(guiLeft.toDouble(), guiTop.toDouble(), 0.0)
            buttons.forEach {
                if (it.hoverImage?.endsWith("-light.png") == true && !HysentialsConfig.wardrobeDarkMode) {
                    it.draw(mouseX, mouseY)
                } else if (HysentialsConfig.wardrobeDarkMode && it.hoverImage?.endsWith("-light.png") == false) {
                    it.draw(mouseX, mouseY)
                }
            }
        } catch (ignored: Exception) {
        }
        Renderer.untranslate(0.0, 0.0, 0.0)
        GlStateManager.popMatrix()
    }

    open fun drawEntityOnScreen(posX: Int, posY: Int, scale: Int, xAngle: Float, yAngle: Float, ent: EntityLivingBase) {
        GlStateManager.enableColorMaterial()
        GlStateManager.pushMatrix()
        GlStateManager.translate(posX.toFloat(), posY.toFloat(), 50.0f)
        GlStateManager.scale((-scale).toFloat(), scale.toFloat(), scale.toFloat())
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f)
        val f = ent.renderYawOffset
        val g = ent.rotationYaw
        val h = ent.rotationPitch
        val i = ent.prevRotationYawHead
        val j = ent.rotationYawHead

        ent.renderYawOffset = 0.0f
        ent.rotationYaw = 0.0f
        ent.rotationPitch = 0.0f
        ent.prevRotationYawHead = 0.0f
        ent.rotationYawHead = 0.0f
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f)
        RenderHelper.enableStandardItemLighting()
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(-Math.atan((yAngle / 40.0f).toDouble()).toFloat() * 20.0f, 1.0f, 0.0f, 0.0f)
        val renderManager = Minecraft.getMinecraft().renderManager
        GlStateManager.rotate(xAngle, 0.0f, 1.0f, 0.0f)
//        GlStateManager.rotate(yAngle, 1.0f, 0.0f, 0.0f)
        renderManager.setPlayerViewY(180.0f + yAngle)
        renderManager.isRenderShadow = false
        renderManager.renderEntityWithPosYaw(ent, 0.0, 0.0, 0.0, 0.0f, 1.0f)
        renderManager.isRenderShadow = true
        GlStateManager.rotate(-xAngle, 0.0f, 1.0f, 0.0f)
//        GlStateManager.rotate(-yAngle, 1.0f, 0.0f, 0.0f)
        ent.renderYawOffset = f
        ent.rotationYaw = g
        ent.rotationPitch = h
        ent.prevRotationYawHead = i
        ent.rotationYawHead = j
        GlStateManager.popMatrix()
        RenderHelper.disableStandardItemLighting()
        GlStateManager.disableRescaleNormal()
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit)
        GlStateManager.disableTexture2D()
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit)
    }

    private fun drawSlot(slotIn: Slot, mouseX: Int, mouseY: Int, partialTicks: Float) {
        val i: Int = slotIn.xDisplayPosition
        val j: Int = slotIn.yDisplayPosition
        GlStateManager.disableLighting()
        GlStateManager.disableDepth()
        GlStateManager.colorMask(true, true, true, false)
        val page = paginationList!!.getPage(page)
        if (page.size > slotIn.slotIndex) {
            val cosmetic = page[slotIn.slotIndex]
            val name = cosmetic.name
            val uuid = Minecraft.getMinecraft().thePlayer.uniqueID
            if (equippedCosmetic(uuid, name)) {
                Renderer.drawImage(selectedSlot, i.toDouble(), j.toDouble(), 25.0, 26.0)
            }
        }
        val itemstack: ItemStack = slotIn.stack ?: return

        val ibakedmodel: IBakedModel = Minecraft.getMinecraft().renderItem.itemModelMesher.getItemModel(itemstack)
        val width = ibakedmodel.particleTexture.iconWidth
        val height = ibakedmodel.particleTexture.iconHeight
        itemRender.renderItemAndEffectIntoGUI(itemstack, i + (25 - width) / 2, j + (26 - height) / 2)
        itemRender.renderItemOverlayIntoGUI(fontRendererObj, itemstack, i + (25 - width) / 2, j + (26 - height) / 2, "")

        itemRender.zLevel = 0.0f
        GlStateManager.enableLighting()
        GlStateManager.enableDepth()
        GlStateManager.colorMask(true, true, true, true)
    }

    override fun onMouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int) {
        super.onMouseClicked(mouseX, mouseY, mouseButton)
        buttons.forEach {
            if (it.hoverImage?.endsWith("-light.png") == true && !HysentialsConfig.wardrobeDarkMode) {
                it.click(mouseX, mouseY, mouseButton)
            } else if (HysentialsConfig.wardrobeDarkMode) {
                it.click(mouseX, mouseY, mouseButton)
            }
        }

        if (mouseButton == 0) {

            val rX = mouseX - guiLeft
            val rY = mouseY - guiTop

            when {
                rX in 5.0..15.0 && rY in 4.0..14.0 && type !== "owned" -> {
                    type = "owned"
                    page = 1
                    updatePage()
                    this.mc.soundHandler.playSound(
                        PositionedSoundRecord.create(
                            ResourceLocation("gui.button.press"),
                            1.0f
                        )
                    )
                }

                rX in 5.0..14.0 && rY in 22.0..31.0 && type !== "head" -> {
                    type = "head"
                    page = 1
                    updatePage()
                    this.mc.soundHandler.playSound(
                        PositionedSoundRecord.create(
                            ResourceLocation("gui.button.press"),
                            1.0f
                        )
                    )
                }

                rX in 5.0..14.0 && rY in 38.0..47.0 && type !== "back" -> {
                    type = "back"
                    page = 1
                    updatePage()
                    this.mc.soundHandler.playSound(
                        PositionedSoundRecord.create(
                            ResourceLocation("gui.button.press"),
                            1.0f
                        )
                    )
                }

                rX in 5.0..14.0 && rY in 53.0..64.0 && type !== "pantaloons" -> {
                    type = "pantaloons"
                    page = 1
                    updatePage()
                    this.mc.soundHandler.playSound(
                        PositionedSoundRecord.create(
                            ResourceLocation("gui.button.press"),
                            1.0f
                        )
                    )
                }

                rX in 4.0..15.0 && rY in 70.0..77.0 && type !== "boots" -> {
                    type = "boots"
                    page = 1
                    updatePage()
                    this.mc.soundHandler.playSound(
                        PositionedSoundRecord.create(
                            ResourceLocation("gui.button.press"),
                            1.0f
                        )
                    )
                }

                rX in 5.0..14.0 && rY in 83.0..93.0 && type !== "pet" -> {
                    type = "pet"
                    page = 1
                    updatePage()
                    this.mc.soundHandler.playSound(
                        PositionedSoundRecord.create(
                            ResourceLocation("gui.button.press"),
                            1.0f
                        )
                    )
                }

                rX in 5.0..14.0 && rY in 99.0..106.0 && type !== "chat" -> {
                    type = "chat"
                    page = 1
                    updatePage()
                    this.mc.soundHandler.playSound(
                        PositionedSoundRecord.create(
                            ResourceLocation("gui.button.press"),
                            1.0f
                        )
                    )
                }

                rX in 5.0..14.0 && rY in 112.0..122.0 && type !== "bundle" -> {
                    type = "bundle"
                    page = 1
                    updatePage()
                    this.mc.soundHandler.playSound(
                        PositionedSoundRecord.create(
                            ResourceLocation("gui.button.press"),
                            1.0f
                        )
                    )
                }

                rX in 223.0..288.0 && rY in 20.0..159.0 -> {
                    isDragging = true
                    dragPos = Pair(mouseX, mouseY)
                }

                rX in 245.0..254.0 && rY in 5.0..12.0 -> {
                    HysentialsConfig.wardrobeDarkMode = !HysentialsConfig.wardrobeDarkMode
                    this.mc.soundHandler.playSound(
                        PositionedSoundRecord.create(
                            ResourceLocation("gui.button.press"),
                            1.0f
                        )
                    )
                }
            }

            focused = rX in 47.0..192.0 && rY in 128.0..141.0

            val slot = getSlot(
                mouseX.toFloat() - guiLeft,
                mouseY.toFloat() - guiTop
            )
            if (slot != -1 && slot < inventorySlots.size && inventoryMap.containsKey(type)) {
                val cosmetics: List<HysentialsSchema.Cosmetic> = inventoryMap[type]!!
                val paginationList = PaginationList(cosmetics, 32)
                val page = paginationList.getPage(page)
                if (slot >= page.size) return
                val cosmetic = page[slot]
                val cosmeticName = cosmetic.name
                val uuid = Minecraft.getMinecraft().thePlayer.uniqueID
                val emerald = Socket.cachedUser?.emeralds ?: 0
                if (equippedCosmetic(uuid, cosmeticName) && hasCosmetic(uuid, cosmeticName)) {
                    Hysentials.INSTANCE.cosmeticManager.unEquipCosmetic(cosmeticName) {
                        if (!(JSONObject(it)["success"] as Boolean)) return@unEquipCosmetic
                        initScreen(width, height)
                    }
                } else if (hasCosmetic(uuid, cosmeticName)) {
                    Hysentials.INSTANCE.cosmeticManager.equipCosmetic(cosmeticName) {
                        if (!(JSONObject(it)["success"] as Boolean)) return@equipCosmetic
                        initScreen(width, height)
                    }
                } else if (!hasCosmetic(uuid, cosmeticName) && emerald >= cosmetic.cost) {
                    Hysentials.INSTANCE.cosmeticManager.purchaseCosmetic(cosmeticName) {
                        if (!(JSONObject(it)["success"] as Boolean)) return@purchaseCosmetic
                        initScreen(width, height)
                    }
                }
            }
        }
    }

    override fun onMouseReleased(mouseX: Double, mouseY: Double, state: Int) {
        super.onMouseReleased(mouseX, mouseY, state)
        if (state == 0) {
            isDragging = false
        }
    }

    override fun onKeyPressed(keyCode: Int, typedChar: Char, modifiers: UKeyboard.Modifiers?) {
        super.onKeyPressed(keyCode, typedChar, modifiers)
        if (focused) {
            if (search.isEmpty() && keyCode == UKeyboard.KEY_SPACE) return
            input.textboxKeyTyped(typedChar, keyCode)
            if (keyCode == 1) { // keycode for escape key
                Minecraft.getMinecraft().thePlayer.closeScreen()
                return
            }
            if (keyCode == 15) { // keycode for tab key
                return
            }
            if (keyCode == 28) { // keycode for enter key
                return
            }
            search = input.text
            page = 1
            updatePage()
        }
    }

    fun getSlot(x: Float, y: Float): Int {
        val slotSpacing = 2
        val numRows = 4
        val numCols = 8
        if (x < 21 || y < 21) return -1
        if (x > 222 || y > 121) return -1
        // Calculate the slot number based on the coordinates
        val row = ((y - 21) / (24 + slotSpacing)).toInt()
        val col = ((x - 21) / (23 + slotSpacing)).toInt()

        if (row < 0 || col < 0 || row >= numRows || col >= numCols) return -1
        // Calculate the slot number
        val slotNumber = row * numCols + col

        return slotNumber
    }

    private fun updatePage() {
        inventorySlots.clear()
        if (!inventoryMap.containsKey(type)) return
        var cosmetics: List<HysentialsSchema.Cosmetic> = inventoryMap[type]!!
        cosmetics = cosmetics.stream().filter { item: HysentialsSchema.Cosmetic ->
            C.removeColor(
                item.name.lowercase()
            ).contains(search.lowercase(Locale.getDefault()))
        }.collect(Collectors.toList())

        paginationList = PaginationList(cosmetics, 32)
        val page = paginationList!!.getPage(page)
        if (page == null || page.isEmpty()) return
        maxPage = paginationList!!.pageCount
        for (i in 0..3) {
            for (j in 0..7) {
                val index = i * 8 + j
                val slot = Slot(inventory, index, guiLeft + 20 + j * 25, guiTop + 20 + i * 26)
                if (index < page.size) {
                    val cosmetic = page[index]
                    if (cosmetic.item == null) {
                        println("Cosmetic item ${cosmetic.name} is null")
                        slot.putStack(null)
                    } else {
                        val item = cosmetic.item!!
                        slot.putStack(item)
                    }
                } else {
                    slot.putStack(null)
                }
                inventorySlots.add(slot)
            }
        }
    }

    override fun initScreen(width: Int, height: Int) {
        super.initScreen(width, height)
        instance = this
        inventory = CosmeticInventory()
        inventoryMap.clear()
        inventorySlots.clear()
        guiLeft = (width - xSize) / 2
        guiTop = (height - ySize) / 2

        soundHandler.playSound(
            PositionedSoundRecord.create(
                ResourceLocation("entity.item.pickup"),
                0.6f
            )
        )
        val uuid = Minecraft.getMinecraft().thePlayer.uniqueID

        //sort cosmetics by rarity and if they have it or not
        val cosmetics = BlockWAPIUtils.getCosmetics().sortedWith(
            compareBy({ hasCosmetic(uuid, it.name) },
                { indexFromRarity(it.rarity) })
        ).asReversed()
        for (o in cosmetics) {
            var type = o.type
            var subType = o.subType ?: type
            val name = o.name.lowercase()
            val itemID = o.itemID ?: -1
            val description = o.description
            val rarity = o.rarity
            val cost = o.cost
            val emerald = Socket.cachedUser?.emeralds ?: 0
            var item: ItemStack? = null
            val displayName =
                "&f:${rarity.lowercase()}: <${colorFromRarity(rarity)}>" + (o.displayName ?: name.formatCapitalize())
            val lore: MutableList<String> = mutableListOf()
            description.split("\n").forEach(lore::add)
            if (equippedCosmetic(uuid, name)) {
                lore.add("")
                lore.add("&aEquipped")
            } else if (hasCosmetic(uuid, name)) {
                lore.add("")
                lore.add("&eClick to equip!")
            } else {
                if (cost > 0) {
                    if (emerald < cost) {
                        lore.add("")
                        lore.add("&7Cost: &a$cost⏣")
                        lore.add("")
                        lore.add("&cNot enough emeralds!")
                    } else {
                        lore.add("")
                        lore.add("&7Cost: &a$cost⏣")
                        lore.add("")
                        lore.add("&eClick to purchase!")
                    }
                } else if (cost == 0) {
                    lore.add("")
                    lore.add("&7Cost: &aFREE")
                    lore.add("")
                    lore.add("&eClick to purchase!")
                } else {
                    lore.add("")
                    lore.add("&cNot purchasable!")
                }
            }
            when (subType) {
                "pet" -> {
                    item = GuiItem.makeMonsterEgg(
                        displayName,
                        1,
                        itemID,
                        lore
                    )
                }

                "cape" -> {
                    item = GuiItem.makeColorfulItem(
                        Material.LEATHER_CHESTPLATE,
                        displayName,
                        1,
                        0,
                        lore
                    )
                    GuiItem.setColor(item, o.color)
                }

                "hat" -> {
                    item = GuiItem.makeColorfulItem(
                        Material.LEATHER_HELMET,
                        displayName,
                        1,
                        0,
                        lore
                    )
                    GuiItem.setColor(item, o.color)
                }

                "backpack" -> {
                    item = GuiItem.makeColorfulItem(
                        Material.LEATHER_CHESTPLATE,
                        displayName,
                        1,
                        0,
                        lore
                    )
                    GuiItem.setColor(item, o.color)
                }

                "chat" -> {
                    item = GuiItem.makeColorfulItem(
                        Material.valueOf(o.material!!),
                        displayName,
                        1,
                        0,
                        lore
                    )
                }

                "bundle" -> {
                    if (Material.valueOf(o.material!!) == Material.SKULL_ITEM) {
                        item = GuiItem.makeColorfulSkullItem(
                            displayName,
                            o.skullOwner!!,
                            1,
                            lore
                        )
                    } else {
                        item = GuiItem.makeColorfulItem(
                            Material.valueOf(o.material!!),
                            displayName,
                            1,
                            0,
                            lore
                        )
                    }
                }

                "pantaloons" -> {
                    item = GuiItem.makeColorfulItem(
                        Material.LEATHER_LEGGINGS,
                        displayName,
                        1,
                        0,
                        lore
                    )
                    GuiItem.setColor(item, o.color)
                }

                "boots" -> {
                    item = GuiItem.makeColorfulItem(
                        Material.LEATHER_BOOTS,
                        displayName,
                        1,
                        0,
                        lore
                    )
                    GuiItem.setColor(item, o.color)
                }

                else -> {
                    item = GuiItem.makeColorfulItem(
                        Material.valueOf(o.material!!),
                        displayName,
                        1,
                        0,
                        lore
                    )
                }
            }
            o.item = item

            if (!inventoryMap.containsKey(type)) {
                inventoryMap[type] = ArrayList()
            }
            inventoryMap[type]?.add(o)

            if (!inventoryMap.containsKey("owned")) {
                inventoryMap["owned"] = ArrayList()
            }

            if (hasCosmetic(uuid, name.lowercase())) {
                inventoryMap["owned"]?.add(o)
            }
        }

        buttons.let {
            it.add(
                Button(
                    18,
                    126,
                    29,
                    20,
                    "hysentials:gui/wardrobe/left.png",
                    instance,
                    onHover = { _, _ -> page > 1 }) { _, _, _ ->
                    if (page > 1) {
                        page--
                        updatePage()
                        this.mc.soundHandler.playSound(
                            PositionedSoundRecord.create(
                                ResourceLocation("gui.button.press"),
                                1.0f
                            )
                        )
                    }
                })
            it.add(
                Button(
                    192,
                    126,
                    29,
                    20,
                    "hysentials:gui/wardrobe/right.png",
                    instance,
                    onHover = { _, _ -> page < maxPage }) { _, _, _ ->
                    if (page < maxPage) {
                        page++
                        updatePage()
                        this.mc.soundHandler.playSound(
                            PositionedSoundRecord.create(
                                ResourceLocation("gui.button.press"),
                                1.0f
                            )
                        )
                    }
                })
            it.add(Button(18, 126, 29, 20, "hysentials:gui/wardrobe/left-light.png", instance) { _, _, _ ->
                if (page > 1) {
                    page--
                    updatePage()
                    this.mc.soundHandler.playSound(
                        PositionedSoundRecord.create(
                            ResourceLocation("gui.button.press"),
                            1.0f
                        )
                    )
                }
            })
            it.add(Button(192, 126, 29, 20, "hysentials:gui/wardrobe/right-light.png", instance) { _, _, _ ->
                if (page < maxPage) {
                    page++
                    updatePage()
                    this.mc.soundHandler.playSound(
                        PositionedSoundRecord.create(
                            ResourceLocation("gui.button.press"),
                            1.0f
                        )
                    )
                }
            })
        }

        updatePage()
    }

    override fun getTop(): Int {
        return guiTop
    }

    override fun getLeft(): Int {
        return guiLeft
    }


}