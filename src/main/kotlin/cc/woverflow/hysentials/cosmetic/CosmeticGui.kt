package cc.woverflow.hysentials.cosmetic

import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import cc.polyfrost.oneconfig.libs.universal.UScreen
import cc.polyfrost.oneconfig.renderer.NanoVGHelper
import cc.woverflow.hysentials.Hysentials
import cc.woverflow.hysentials.guis.container.GuiItem
import cc.woverflow.hysentials.util.*
import cc.woverflow.hysentials.utils.formatCapitalize
import cc.woverflow.hysentials.utils.toTitleCase
import cc.woverflow.hysentials.websocket.Socket
import com.google.common.collect.Lists
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.client.config.GuiUtils
import org.json.JSONObject
import java.util.*

open class CosmeticGui : UScreen() {
    companion object {
        fun equippedCosmetic(uuid: UUID, name: String): Boolean {
            val cosmetics = BlockWAPIUtils.getCosmetics()
            cosmetics.find { it["name"] == name }?.let {
                if (it.getJSONArray("equipped").contains(uuid.toString())) {
                    return true
                }
            }
            return false
        }

        fun hasCosmetic(uuid: UUID, name: String): Boolean {
            val cosmetics = BlockWAPIUtils.getCosmetics()
            cosmetics.find { it["name"] == name }?.let {
                if (it.getJSONArray("users").contains(uuid.toString())) {
                    return true
                }
            }
            return false
        }

        fun getOwnedCosmetics(uuid: UUID): ArrayList<JSONObject> {
            val cosmetics = BlockWAPIUtils.getCosmetics()
            val ownedCosmetics = ArrayList<JSONObject>()
            for (cosmetic in cosmetics) {
                if (cosmetic.getJSONArray("users").contains(uuid.toString())) {
                    ownedCosmetics.add(cosmetic)
                }
            }
            return ownedCosmetics
        }

        fun getEquippedCosmetics(uuid: UUID): ArrayList<JSONObject> {
            val cosmetics = BlockWAPIUtils.getCosmetics()
            val equippedCosmetics = ArrayList<JSONObject>()
            for (cosmetic in cosmetics) {
                if (cosmetic.getJSONArray("equipped").contains(uuid.toString())) {
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
    }

    var xSize = 176
    var ySize = 125
    protected var guiLeft = 0
    protected var guiTop = 0
    var inventory: CosmeticInventory? = null
    var theSlot: Slot? = null
    var textureManager = Minecraft.getMinecraft().textureManager
    var inventorySlots: ArrayList<Slot> = Lists.newArrayList()
    var inventoryMap: HashMap<String, ArrayList<JSONObject>> = HashMap()
    var opaqueSlot = ResourceLocation("hysentials:gui/inventory_slot_opaque.png")
    var selectedSlot = ResourceLocation("hysentials:gui/inventory_slot_selected.png")
    var cosmeticBackground = ResourceLocation("hysentials:gui/cosmeticBackground.png")
    var guiSidebar = ResourceLocation("hysentials:gui/guisidebar.png")
    var firstPage = ResourceLocation("hysentials:gui/first_page.png")
    var lastPage = ResourceLocation("hysentials:gui/last_page.png")
    var fontRenderer: ImageIconRenderer? = Hysentials.INSTANCE.imageIconRenderer
    var page: Int = 1
    var maxPage: Int = 1
    var type: String = "pet"
    var paginationList: PaginationList<JSONObject>? = null

    override fun onDrawScreen(matrixStack: UMatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.onDrawScreen(matrixStack, mouseX, mouseY, partialTicks)
        this.theSlot = null
        GlStateManager.pushMatrix()
        this.drawDefaultBackground()

        try {
            Renderer.drawImage(
                cosmeticBackground,
                guiLeft.toDouble(),
                guiTop.toDouble(),
                xSize.toDouble(),
                ySize.toDouble()
            )

            Renderer.drawString(
                fontRenderer,
                "&8Cosmetics",
                (guiLeft + 30).toFloat(),
                (guiTop + 12).toFloat()
            )

            Renderer.drawString(
                fontRenderer,
                "${type.toTitleCase()} ($page/$maxPage)",
                (guiLeft + ((xSize) - fontRenderer!!.getStringWidth("${type.toTitleCase()} ($page/$maxPage)")) / 2).toFloat(),
                (guiTop + ySize - 22).toFloat()
            )

            val emerald = if (Socket.cachedData == null) {
                0
            } else {
                Socket.cachedData!!["emeralds"] as Int
            }

            val lore = "&fEmeralds: &a${emerald}⏣\n" +
                    "&fOwned: &a${getOwnedCosmetics(Minecraft.getMinecraft().thePlayer.uniqueID).size}&8/&c${BlockWAPIUtils.getCosmetics().size}\n" +
                    "\n" +
                    "&8Earn &aEmeralds ⏣ &8by &8playing\n" +
                    "&8regular &3minigames&8! View your\n" +
                    "&8progress &8by doing &e/hs level\n" +
                    "\n" +
                    "&7Alternatively, purchase\n" +
                    "&7them at our online\n" +
                    "&7webstore:\n" +
                    "<#3366CC>www.redstone.llc/store"
            val loreList = lore.split("\n")

            val maxLoreWidth = loreList.maxOf { fontRenderer!!.getStringWidth(it) }*0.6
            Renderer.drawImage(
                guiSidebar,
                (guiLeft + xSize).toDouble(),
                (guiTop).toDouble(),
                (maxLoreWidth + 10).toDouble(),
                (124).toDouble()
            )

            GlStateManager.enableRescaleNormal()
            GlStateManager.scale(0.75f, 0.75f, 0.75f)

            for (i in loreList.indices) {
                Renderer.drawString(
                    fontRenderer,
                    loreList[i],
                    (guiLeft * 1.35 + xSize * 1.30 + 6*1.25).toFloat(),
                    (guiTop * 1.35 + 20*1.35 + (i * 10*0.75)*1.35).toFloat(),
                    true
                )
            }
            GlStateManager.disableRescaleNormal()
            GlStateManager.popMatrix()
            GlStateManager.pushMatrix()


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
                    drawGradientRect(j1, l2, j1 + 16, l2 + 16, -2130706433, -2130706433)
                    GlStateManager.colorMask(true, true, true, true)
                    GlStateManager.enableLighting()
                    GlStateManager.enableDepth()
                }
            }
            val inventoryplayer = mc.thePlayer.inventory
            if (inventoryplayer.itemStack == null && theSlot != null && theSlot!!.hasStack) {
                val itemstack1 = theSlot!!.stack
                renderToolTip(itemstack1, mouseX, mouseY)
            }

            if (mouseX >= guiLeft + 8 && mouseX <= guiLeft + 43 && mouseY >= guiTop + 100 && mouseY <= guiTop + 113) {
                Renderer.drawImage(firstPage, guiLeft + 9.0, guiTop + 101.0, 34.0, 12.0)
                val next = if (page == maxPage && type == "pet") {
                    "Go to Cape"
                } else if (page == maxPage && type == "cape") {
                    "Go to Hat"
                } else if (page == maxPage && type == "hat") {
                    "Go to Pet"
                } else {
                    "Go to " + type.formatCapitalize() + " Page " + (page + 1)
                }
                drawHoveringText(
                    listOf(next),
                    mouseX,
                    mouseY,
                    fontRenderer,
                )
            }
            if (mouseX >= guiLeft + 133 && mouseX <= guiLeft + 167 && mouseY >= guiTop + 100 && mouseY <= guiTop + 113) {
                Renderer.drawImage(lastPage, guiLeft + 133.0, guiTop + 101.0, 34.0, 12.0)
                val next = if (page == 1 && type == "pet") {
                    "Go to Hat"
                } else if (page == 1 && type == "hat") {
                    "Go to Cape"
                } else if (page == 1 && type == "cape") {
                    "Go to Pet"
                } else {
                    "Go to " + type.formatCapitalize() + " Page " + (page - 1)
                }
                drawHoveringText(
                    listOf(next),
                    mouseX,
                    mouseY,
                    fontRenderer,
                )
            }
        } catch (ignored: Exception) {
        }

        GlStateManager.popMatrix()
    }

    private fun drawSlot(slotIn: Slot, mouseX: Int, mouseY: Int, partialTicks: Float) {
        val i: Int = slotIn.xDisplayPosition
        val j: Int = slotIn.yDisplayPosition
        GlStateManager.disableLighting()
        GlStateManager.disableDepth()
        GlStateManager.colorMask(true, true, true, false)
        Renderer.drawImage(opaqueSlot, i.toDouble(), j.toDouble(), 16.0, 16.0)
        val page = paginationList!!.getPage(page)
        if (page.size > slotIn.slotIndex) {
            val cosmetic = page[slotIn.slotIndex]
            val name = cosmetic["name"] as String
            val uuid = Minecraft.getMinecraft().thePlayer.uniqueID
            if (equippedCosmetic(uuid, name)) {
                Renderer.drawImage(selectedSlot, i.toDouble() - 1, j.toDouble() - 1, 18.0, 18.0)
            }
        }
        val itemstack: ItemStack = slotIn.stack ?: return

        itemRender.renderItemAndEffectIntoGUI(itemstack, i, j)
        itemRender.renderItemOverlayIntoGUI(fontRendererObj, itemstack, i, j, "")
    }

    override fun onMouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int) {
        super.onMouseClicked(mouseX, mouseY, mouseButton)
        if (mouseButton == 0) {
            //Close button
            if (mouseX >= guiLeft + 8 && mouseX <= guiLeft + 23 && mouseY >= guiTop + 8 && mouseY <= guiTop + 21) {
                Minecraft.getMinecraft().thePlayer.playSound("entity.item.pickup", 10.0f, 0.4f)
                Minecraft.getMinecraft().thePlayer.closeScreen()
            }
            //First page
            if (mouseX >= guiLeft + 8 && mouseX <= guiLeft + 43 && mouseY >= guiTop + 100 && mouseY <= guiTop + 113) {
                if (page == maxPage && type == "pet") {
                    type = "cape"
                    page = 1
                    updatePage()
                } else if (page == maxPage && type == "cape") {
                    type = "hat"
                    page = 1
                    updatePage()
                } else if (page == maxPage && type == "hat") {
                    type = "pet"
                    page = 1
                    updatePage()
                } else if (page != maxPage) {
                    page++
                    updatePage()
                }
            }
            //Last page
            if (mouseX >= guiLeft + 133 && mouseX <= guiLeft + 167 && mouseY >= guiTop + 100 && mouseY <= guiTop + 113) {
                if (page == 1 && type == "pet") {
                    type = "hat"
                    page = maxPage
                    updatePage()
                } else if (page == 1 && type == "hat") {
                    type = "cape"
                    page = maxPage
                    updatePage()
                } else if (page == 1 && type == "cape") {
                    type = "pet"
                    page = maxPage
                    updatePage()
                } else if (page != 1) {
                    page--
                    updatePage()
                }
            }

            val slot = getSlot(
                mouseX.toFloat() - guiLeft,
                mouseY.toFloat() - guiTop
            )
            if (slot != -1) {
                val cosmetics: List<JSONObject> = inventoryMap[type]!!
                val paginationList = PaginationList(cosmetics, 36)
                val page = paginationList.getPage(page)
                if (slot >= page.size) return
                val cosmetic = page[slot]
                val cosmeticName = cosmetic.getString("name")
                val uuid = Minecraft.getMinecraft().thePlayer.uniqueID
                val emerald = if (Socket.cachedData == null) {
                    0
                } else {
                    Socket.cachedData!!["emeralds"] as Int
                }
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
                } else if (!hasCosmetic(uuid, cosmeticName) && emerald >= cosmetic.getInt("cost")) {
                    Hysentials.INSTANCE.cosmeticManager.purchaseCosmetic(cosmeticName) {
                        if (!(JSONObject(it)["success"] as Boolean)) return@purchaseCosmetic
                        initScreen(width, height)
                    }
                }
            }
        }
    }

    fun getSlot(x: Float, y: Float): Int {
        val slotSize = 16
        val slotSpacing = 2
        val numRows = 4
        val numCols = 9
        if (x < 7 || y < 24) return -1
        if (x > 168 || y > 96) return -1
        // Calculate the slot number based on the coordinates
        val row = ((y - 24) / (slotSize + slotSpacing)).toInt()
        val col = ((x - 7) / (slotSize + slotSpacing)).toInt()

        if (row < 0 || col < 0 || row >= numRows || col >= numCols) return -1
        // Calculate the slot number
        val slotNumber = row * numCols + col

        return slotNumber
    }

    private fun updatePage() {
        if (!inventoryMap.containsKey(type)) return
        val cosmetics: List<JSONObject> = inventoryMap[type]!!
        paginationList = PaginationList(cosmetics, 36)
        val page = paginationList!!.getPage(page)
        maxPage = paginationList!!.pageCount
        inventorySlots.clear()
        for (i in 0..3) {
            for (j in 0..8) {
                val index = i * 9 + j
                val slot = Slot(inventory, index, guiLeft + 8 + j * 18, guiTop + 26 + i * 18)
                if (index < page.size) {
                    val cosmetic = page[index]
                    val item = cosmetic["item"] as ItemStack
                    slot.putStack(item)
                } else {
                    slot.putStack(null)
                }
                inventorySlots.add(slot)
            }
        }
    }

    override fun initScreen(width: Int, height: Int) {
        super.initScreen(width, height)
        inventory = CosmeticInventory()
        inventoryMap.clear()
        inventorySlots.clear()
        guiLeft = (width - xSize) / 2
        guiTop = (height - ySize) / 2
        Minecraft.getMinecraft().thePlayer.playSound("entity.item.pickup", 10.0f, 0.6f)
        val uuid = Minecraft.getMinecraft().thePlayer.uniqueID

        //sort cosmetics by rarity and if they have it or not
        val cosmetics = BlockWAPIUtils.getCosmetics().sortedWith(compareBy({ hasCosmetic(uuid, it["name"] as String) }, { indexFromRarity(it["rarity"] as String) })).asReversed()
        for (o in cosmetics) {
            val type = o["type"] as String
            val name = (o["name"] as String).formatCapitalize()
            val itemID = o["itemID"] as Int
            val description = o["description"] as String
            val rarity = o["rarity"] as String
            val cost = o["cost"] as Int
            val emerald = if (Socket.cachedData == null) {
                0
            } else {
                Socket.cachedData!!["emeralds"] as Int
            }
            var item: ItemStack? = null
            val lore: MutableList<String> = mutableListOf()
            description.split("\n").forEach(lore::add)
            if (equippedCosmetic(uuid, name.lowercase())) {
                lore.add("")
                lore.add("&aEquipped")
            } else if (hasCosmetic(uuid, name.lowercase())) {
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
            when (type) {
                "pet" -> {
                    item = GuiItem.makeMonsterEgg(
                        "&f:${rarity.lowercase()}: <${colorFromRarity(rarity)}>${name} Pet",
                        1,
                        itemID,
                        lore
                    )
                }

                "cape" -> {
                    item = GuiItem.makeColorfulItem(
                        Material.LEATHER_CHESTPLATE,
                        "&f:${rarity.lowercase()}: <${colorFromRarity(rarity)}>${name} Cape",
                        1,
                        0,
                        lore
                    )
                    GuiItem.setColor(item, o["color"] as String)
                }

                "hat" -> {
                    item = GuiItem.makeColorfulItem(
                        Material.LEATHER_HELMET,
                        "&f:${rarity.lowercase()}: <${colorFromRarity(rarity)}>${name} Hat",
                        1,
                        0,
                        lore
                    )
                    GuiItem.setColor(item, o["color"] as String)
                }
            }
            o.put("item", item)
            if (!inventoryMap.containsKey(type)) {
                inventoryMap[type] = ArrayList()
            }
            inventoryMap[type]?.add(o)
        }

        updatePage()
    }


}