package llc.redstone.hysentials.cosmetic

import cc.polyfrost.oneconfig.libs.universal.UKeyboard
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import cc.polyfrost.oneconfig.libs.universal.UScreen
import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.config.HysentialsConfig
import llc.redstone.hysentials.updategui.Button
import llc.redstone.hysentials.updategui.HysentialsGui
import llc.redstone.hysentials.schema.HysentialsSchema
import llc.redstone.hysentials.util.*
import llc.redstone.hysentials.utils.splitToWords
import llc.redstone.hysentials.websocket.Socket
import com.google.common.collect.Lists
import llc.redstone.hysentials.cosmetic.CosmeticManager.drawSlot
import llc.redstone.hysentials.cosmetic.CosmeticManager.equipCosmetic
import llc.redstone.hysentials.cosmetic.CosmeticManager.equippedCosmetic
import llc.redstone.hysentials.cosmetic.CosmeticManager.getOwnedCosmetics
import llc.redstone.hysentials.cosmetic.CosmeticManager.hasCosmetic
import llc.redstone.hysentials.cosmetic.CosmeticManager.indexFromRarity
import llc.redstone.hysentials.cosmetic.CosmeticManager.previewing
import llc.redstone.hysentials.cosmetic.CosmeticManager.purchaseCosmetic
import llc.redstone.hysentials.cosmetic.CosmeticManager.tabFromType
import llc.redstone.hysentials.cosmetic.CosmeticManager.unEquipCosmetic
import llc.redstone.hysentials.cosmetic.CosmeticManager.updateCosmetics
import llc.redstone.hysentials.cosmetic.CosmeticTab.Companion.tabs
import llc.redstone.hysentials.utils.drawEntityOnScreen
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.inventory.Slot
import net.minecraft.scoreboard.Score
import net.minecraft.scoreboard.ScoreDummyCriteria
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

        var search: String = ""
        var page: Int = 1
        var maxPage: Int = 1
        var type: String = "owned"
        var inventorySlots: ArrayList<Slot> = Lists.newArrayList()
        var paginationList: PaginationList<HysentialsSchema.Cosmetic>? = null
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

    var mcFive = HysentialsFontRenderer("Minecraft Five", 12f)
    var fontRenderer: ImageIconRenderer? = Hysentials.INSTANCE.imageIconRenderer

    var focused: Boolean = false
    var blinkTimer: Int = 0

    val input: Input = Input(0, 0, 0, 0).let {
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
            // Draw the background (dark and light)
            Renderer.drawImage(
                if (HysentialsConfig.wardrobeDarkMode) cosmeticBackground else lightBackground,
                guiLeft.toDouble(),
                guiTop.toDouble(),
                xSize.toDouble(),
                ySize.toDouble()
            )

            val emerald = Socket.cachedUser?.emeralds ?: 0
            for (tab in tabs) {
                tab.draw(guiLeft, guiTop)
            }
            // Draw the selected tab
            val typeFinal = tabFromType(type)?.displayName ?: "Owned"
            mcFive.drawStringShadow(
                " > ${typeFinal.splitToWords().uppercase()} ($page/$maxPage)",
                guiLeft + 71f,
                guiTop + 5f,
                0xFFFFFF
            )
            // Draw the emerald count
            var largeFormat = DecimalFormat("#,###")
            mcFive.drawStringShadow(" ${largeFormat.format(emerald)}", guiLeft + 71f, guiTop + 149f, 0x55FF55)

            GlStateManager.enableLighting()
            GlStateManager.enableDepth()
            RenderHelper.enableStandardItemLighting()
            GlStateManager.enableRescaleNormal()
            //Draw player entity
            drawEntityOnScreen(guiLeft + 256, guiTop + 124, 40, xAngle, -yAngle, mc.thePlayer)

            for (slot in inventorySlots) {
                val page = paginationList!!.getPage(CosmeticGui.page)
                if (page.size > slot.slotIndex) {
                    val cosmetic = page[slot.slotIndex]
                    drawSlot(slot, cosmetic)
                }
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

            for (tab in tabs) {
                if (tab.isHovered(rX.toDouble(), rY.toDouble())) {
                    drawHoveringText(
                        listOf("§8➔ <#${tab.color}>${tab.displayName} Cosmetics"),
                        mouseX,
                        mouseY,
                        fontRenderer
                    )
                }
            }

            when {

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

            mcFive.drawStringShadow(search.uppercase(), guiLeft + 61f, guiTop + 131f, 0xFFFFFF)

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

            for (tab in tabs) {
                if (tab.isHovered(rX, rY)) {
                    type = tab.name
                    page = 1
                    updatePage()
                    this.mc.soundHandler.playSound(
                        PositionedSoundRecord.create(
                            ResourceLocation("gui.button.press"),
                            1.0f
                        )
                    )
                }
            }

            when {
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
                    unEquipCosmetic(cosmeticName)
                    initScreen(width, height)
                } else if (hasCosmetic(uuid, cosmeticName)) {
                    equipCosmetic(cosmeticName)
                    initScreen(width, height)
                } else if (!hasCosmetic(uuid, cosmeticName) && emerald >= cosmetic.cost) {
                    purchaseCosmetic(cosmeticName)
                    initScreen(width, height)
                }
            }
        } else if (mouseButton == 1) { // Right click to preview
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
                if (!previewing.contains(cosmeticName) && !hasCosmetic(Minecraft.getMinecraft().thePlayer.uniqueID, cosmeticName)) {
                    equipCosmetic(cosmeticName, true)
                } else {
                    unEquipCosmetic(cosmeticName, true)
                }
                initScreen(width, height)
            }
        }
    }

    override fun onScreenClose() {
        super.onScreenClose()
        updateCosmetics()
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
            val name = o.name
            val type = o.type
            o.item = o.toItem(uuid)

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
            it.add(Button(192, 126, 29, 20, "hysentials:gui/wardrobe/right.png", instance,
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