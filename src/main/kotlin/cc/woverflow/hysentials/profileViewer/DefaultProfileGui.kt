package cc.woverflow.hysentials.profileViewer

import cc.polyfrost.oneconfig.libs.universal.UChat
import cc.polyfrost.oneconfig.libs.universal.UMatrixStack
import cc.polyfrost.oneconfig.libs.universal.UScreen
import cc.polyfrost.oneconfig.utils.Multithreading
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil
import cc.woverflow.hysentials.Hysentials
import cc.woverflow.hysentials.cosmetic.CosmeticGui
import cc.woverflow.hysentials.cosmetic.CosmeticGui.Companion.colorFromRarity
import cc.woverflow.hysentials.guis.actionLibrary.ActionViewer.toList
import cc.woverflow.hysentials.guis.container.GuiItem
import cc.woverflow.hysentials.guis.misc.HysentialsLevel
import cc.woverflow.hysentials.handlers.redworks.BwRanksUtils
import cc.woverflow.hysentials.util.*
import cc.woverflow.hysentials.util.BlockWAPIUtils.getRequest
import cc.woverflow.hysentials.util.Renderer.drawImage
import cc.woverflow.hysentials.utils.formatCapitalize
import cc.woverflow.hysentials.websocket.Socket
import com.google.common.collect.Lists
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import org.json.JSONArray
import org.json.JSONObject
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

private fun String.removeFormatting(): String {
    return this.replace("§[0-9a-fk-or]", "")
}

private fun String.addFormatting(): String {
    return this.replace("&", "§")
}

class DefaultProfileGui(var player: EntityPlayer) : UScreen() {
    companion object {
        fun getLevel(exp: Int): Float {
            var exp = exp
            var level = 0f
            while (exp >= HysentialsLevel.getExpForLevel(level.toInt())) {
                level++
                exp -= HysentialsLevel.getExpForLevel(level.toInt())
            }
            level += exp / HysentialsLevel.getExpForLevel(level.toInt()).toFloat()
            return level
        }
    }

    var xSize = 176
    var ySize = 93
    protected var guiLeft = 0
    protected var guiTop = 0
    val profileBackground = ResourceLocation("hysentials:gui/profile.png")
    val visitHouse = ResourceLocation("hysentials:gui/visit_house.png")
    val addFriend = ResourceLocation("hysentials:gui/add_friend.png")
    val partyInvite = ResourceLocation("hysentials:gui/party_invite.png")
    var online = "offline"
    private var badges = mutableListOf<TriVariable<String, String, DuoVariable<Int, Int>>>()
    var inventory: ProfileInventory? = null
    var theSlot: Slot? = null
    var inventorySlots: ArrayList<Slot> = Lists.newArrayList()
    var hypixelData: JSONObject? = null
    var hysentialData: JSONObject? = null
    var guildData: JSONObject? = null
    val fontRenderer: ImageIconRenderer = Hysentials.INSTANCE.imageIconRenderer


    override fun onDrawScreen(matrixStack: UMatrixStack, mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.onDrawScreen(matrixStack, mouseX, mouseY, partialTicks)

        GlStateManager.pushMatrix()
        this.drawDefaultBackground()

        theSlot = null
        try {
            drawImage(profileBackground, guiLeft.toDouble(), guiTop.toDouble(), xSize.toDouble(), ySize.toDouble())

            GlStateManager.enableBlend()
            GlStateManager.enableAlpha()
            GlStateManager.enableTexture2D()
            mc.textureManager.bindTexture(Minecraft.getMinecraft().netHandler.getPlayerInfo(player.uniqueID).locationSkin)
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
            Gui.drawScaledCustomSizeModalRect(
                (guiLeft + 17).toInt(),
                (guiTop + 22).toInt(),
                8.0f,
                8.0f,
                8,
                8,
                34,
                34,
                64.0f,
                64.0f
            )

            drawImage(
                ResourceLocation("hysentials:gui/${online}_status.png"),
                (guiLeft + 49).toDouble(),
                (guiTop + 52).toDouble(),
                9.0,
                9.0
            )

            val relativeX = mouseX - guiLeft
            val relativeY = mouseY - guiTop

            if (relativeX in 7..40 && relativeY in 69..82) {
                drawImage(
                    visitHouse,
                    (guiLeft + 7).toDouble(),
                    (guiTop + 69).toDouble(),
                    34.0,
                    14.0
                )
            }
            if (relativeX in 43..58 && relativeY in 69..82) {
                drawImage(
                    addFriend,
                    (guiLeft + 43).toDouble(),
                    (guiTop + 69).toDouble(),
                    16.0,
                    14.0
                )
            }
            if (relativeX in 61..76 && relativeY in 69..82) {
                drawImage(
                    partyInvite,
                    (guiLeft + 61).toDouble(),
                    (guiTop + 69).toDouble(),
                    16.0,
                    14.0
                )
            }

            if (hypixelData != null) {
                val rankUser = BwRanksUtils.getReplace(
                    (hypixelData!!["rank_formatted"] as String).addFormatting(),
                    player.name,
                    player.uniqueID
                )

                val guildTag = try {
                    if (guildData!!["guild"] == true && guildData!!["tag"] != null) {
                        "${guildData!!["tag_color"]}${guildData!!["tag"]}"
                    } else {
                        ""
                    }
                } catch (e: Exception) {
                    ""
                }


                var largeFormat = DecimalFormat("#,###")
                var lore = "§f${rankUser}${player.name} $guildTag\n" +
                        "&7Hypixel Level: &e${(hypixelData!!["level"] as BigDecimal).toDouble().roundToInt()}\n"
                if (hysentialData != null) {
                    lore += "&7Hysentials Level: &e${getLevel(hysentialData!!["exp"] as Int)}\n" +
                            "&7Emeralds: &a${largeFormat.format(hysentialData!!["emeralds"] as Int)}"
                }
                lore = lore.addFormatting()
                val loreList = lore.split("\n")

                GlStateManager.scale(0.70f, 0.70f, 0.70f)

                for (i in loreList.indices) {
                    Renderer.drawString(
                        fontRenderer,
                        loreList[i],
                        (guiLeft * 1.40 + 64 * 1.40 + 2 * Renderer.screen.getScale()).toFloat(),
                        (guiTop * 1.40 + 21 * 1.40 + (i * 10 * 0.70) * 1.40).toFloat(),
                        true
                    )
                }
                GlStateManager.scale(1f, 1f, 1f)
                GlStateManager.popMatrix()
                GlStateManager.pushMatrix()
            }


            val separation = 5 // Set the desired separation in pixels

            // Calculate the total width of all badges
            val totalWidth = badges.sumOf { badge -> badge.third.first } + (badges.size - 1) * separation

            var x = guiLeft + 145 - totalWidth
            val y = guiTop + 58
            for (i in badges.indices) {
                val badge = badges.elementAt(i)
                val badgeWidth = badge.third.first

                drawImage(
                    ResourceLocation("hysentials:gui/${badge.first}-badge.png"),
                    x.toDouble(),
                    y.toDouble() - (badge.third.second / 2),
                    badgeWidth.toDouble(),
                    badge.third.second.toDouble()
                )

                // Update the x coordinate for the next badge
                x += badgeWidth + separation
            }

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

            if (relativeX in 7..40 && relativeY in 69..82) {
                drawHoveringText(
                    listOf(
                        "§eClick to visit house"
                    ),
                    mouseX,
                    mouseY
                )
            }

            if (relativeX in 43..58 && relativeY in 69..82) {
                drawHoveringText(
                    listOf(
                        "§eClick to add friend"
                    ),
                    mouseX,
                    mouseY
                )
            }

            if (relativeX in 61..76 && relativeY in 69..82) {
                drawHoveringText(
                    listOf(
                        "§eClick to invite to party"
                    ),
                    mouseX,
                    mouseY
                )
            }

            val separationA = 5 // Set the desired separation in pixels

            // Calculate the total width of all badges
            val totalWidthA = badges.sumOf { badge -> badge.third.first } + (badges.size - 1) * separationA

            x = guiLeft + 145 - totalWidthA
            val yA = guiTop + 58
            for (i in badges.indices) {
                val badge = badges.elementAt(i)
                val badgeWidth = badge.third.first
                if (mouseX >= x && mouseX <= x + badgeWidth && mouseY >= yA - (badge.third.second / 2) && mouseY <= yA - (badge.third.second / 2) + badge.third.second) {
                    if (badge.first == "forums") {
                        drawHoveringText(
                            listOf(
                                "<#ffaa00>Forums User " + badge.second.replace("https://hypixel.net/members/", "")
                                    .replace("\\.\\d*/".toRegex(), "")
                            ),
                            mouseX,
                            mouseY
                        )
                    } else {
                        drawHoveringText(
                            listOf(badge.second),
                            mouseX,
                            mouseY
                        )
                    }
                }

                // Update the x coordinate for the next badge
                x += badgeWidth + separation
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val inventoryplayer = mc.thePlayer.inventory
        if (inventoryplayer.itemStack == null && theSlot != null && theSlot!!.hasStack) {
            val itemstack1 = theSlot!!.stack
            renderToolTip(itemstack1, mouseX, mouseY)
        }
        GlStateManager.popMatrix()
    }

    override fun onMouseClicked(mouseX: Double, mouseY: Double, mouseButton: Int) {
        super.onMouseClicked(mouseX, mouseY, mouseButton)
        val relativeX = (mouseX - guiLeft).toInt()
        val relativeY = (mouseY - guiTop).toInt()

        if (relativeX in 61..76 && relativeY in 69..82) {
            mc.thePlayer.sendChatMessage("/p invite ${player.name}")
        }
        if (relativeX in 43..58 && relativeY in 69..82) {
            mc.thePlayer.sendChatMessage("/f add ${player.name}")
        }
        if (relativeX in 8..39 && relativeY in 69..82) {
            if (LocrawUtil.INSTANCE.locrawInfo!!.gameMode == "lobby" && ScoreboardWrapper.getTitle()
                    .removeFormatting() == "HOUSING"
            ) {
                mc.thePlayer.closeScreen()
                mc.thePlayer.sendChatMessage("/visit ${player.name}")
            } else {
                mc.thePlayer.sendChatMessage("/lobby housing")
                Multithreading.schedule(
                    { mc.thePlayer.sendChatMessage("/visit ${player.name}") },
                    200,
                    TimeUnit.MILLISECONDS
                )
            }
        }
    }

    private fun isMouseOverSlot(slotIn: Slot, mouseX: Int, mouseY: Int): Boolean {
        return isPointInRegion(slotIn.xDisplayPosition, slotIn.yDisplayPosition, 16, 16, mouseX, mouseY)
    }

    private fun isPointInRegion(left: Int, top: Int, right: Int, bottom: Int, pointX: Int, pointY: Int): Boolean {
        var pointX = pointX
        var pointY = pointY
        val i = guiLeft
        val j = guiTop
        pointX -= i
        pointY -= j
        return pointX >= left - 1 && pointX < left + right + 1 && pointY >= top - 1 && pointY < top + bottom + 1
    }

    fun getSlot(x: Float, y: Float): Int {
        val slotSize = 16
        val slotSpacing = 2
        val numRows = 4
        val numCols = 1
        if (x < 151 || y < 12) return -1
        if (x > 168 || y > 82) return -1
        // Calculate the slot number based on the coordinates
        val row = ((y - 12) / (slotSize + slotSpacing)).toInt()
        val col = ((x - 153) / (slotSize + slotSpacing)).toInt()

        if (row < 0 || col < 0 || row >= numRows || col >= numCols) return -1
        // Calculate the slot number
        val slotNumber = row * numCols + col

        return slotNumber
    }

    override fun initScreen(width: Int, height: Int) {
        super.initScreen(width, height)
        guiLeft = (width - xSize) / 2
        guiTop = (height - ySize) / 2
        inventory = ProfileInventory(4)

        Multithreading.runAsync {

            val hypixel = NetworkUtils.getString(getRequest("hypixel") + "&lookup=" + player.uniqueID.toNoDash())
            if (hypixel == null) {
                Minecraft.getMinecraft().thePlayer.closeScreen()
                UChat.chat("&cFailed to get data from Hypixel API.")
                return@runAsync
            }
            hypixelData = JSONObject(hypixel)
            online = "offline"
            hypixelData.let {
                if (it!!.getBoolean("online")) online = "online"
            }

            val guild = NetworkUtils.getString(getRequest("hypixel/guild") + "&lookup=" + player.uniqueID.toNoDash())
            if (guild == null) {
                Minecraft.getMinecraft().thePlayer.closeScreen()
                UChat.chat("&cFailed to get data from Hypixel API.")
                return@runAsync
            }
            guildData = JSONObject(guild)

            hysentialData = Socket.cachedUsers.firstOrNull { it["uuid"] == player.uniqueID.toString() }

            badges = ArrayList()

            if (hysentialData != null && hysentialData!!.getBoolean("isEarlySupporter")) badges.add(
                TriVariable(
                    "early-supporter",
                    "<#ff3e3e>Early Supporter",
                    DuoVariable(10, 9)
                )
            )

            if (hysentialData != null && hysentialData!!.getBoolean("isBoosting")) badges.add(
                TriVariable(
                    "booster",
                    "<#dc69da>Discord Booster",
                    DuoVariable(8, 9)
                )
            )

            if (hypixelData!!.getJSONObject("links")!!["HYPIXEL"] != null && hypixelData!!.getJSONObject("links")!!["HYPIXEL"].toString() != "null") badges.add(
                TriVariable(
                    "forums",
                    hypixelData!!.getJSONObject("links")!!["HYPIXEL"].toString(),
                    DuoVariable(9, 13)
                )
            )
            if (hypixelData!!.getJSONObject("links")!!["DISCORD"] != null && hypixelData!!.getJSONObject("links")!!["DISCORD"].toString() != "null") badges.add(
                TriVariable(
                    "discord",
                    "<#7389da>Discord Tag ${hypixelData!!.getJSONObject("links")!!["DISCORD"].toString()}",
                    DuoVariable(12, 9)
                )
            )
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val instant = Instant.ofEpochMilli(hypixelData!!.getLong("first_login"))
            val date = Date.from(instant)
            badges.add(
                TriVariable(
                    "join-date",
                    "<#5fc654>First Joined ${sdf.format(date)}",
                    DuoVariable(9, 9)
                )
            )

            val cosmetics = CosmeticGui.getEquippedCosmetics(player.uniqueID).sortedBy { it["type"] as String }
            for (index in 0..3) {
                if (index >= cosmetics.size) {
                    Slot(inventory, index, guiLeft + 152, guiTop + 12 + index * 18)
                    continue
                }
                val o = cosmetics[index]
                val slot = Slot(inventory, index, guiLeft + 152, guiTop + 12 + index * 18)

                val lore: MutableList<String> = mutableListOf()
                val description = o["description"] as String
                val cost = o["cost"] as Int
                val emerald = if (Socket.cachedData == null) {
                    0
                } else {
                    Socket.cachedData!!["emeralds"] as Int
                }
                description.split("\n").forEach(lore::add)
                if (cost > 0) {
                    if (emerald < cost) {
                        lore.add("")
                        lore.add("&7Cost: &a$cost⏣")
                    } else {
                        lore.add("")
                        lore.add("&7Cost: &a$cost⏣")
                    }
                } else if (cost == 0) {
                    lore.add("")
                    lore.add("&7Cost: &aFREE")
                }
                val type = o["type"] as String
                val rarity = o["rarity"] as String
                val name = (o["name"] as String).formatCapitalize()
                val itemID = o["itemID"] as Int
                var item: ItemStack? = null
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
                slot.putStack(item)
                inventorySlots.add(slot)
            }
        }
    }

    private fun drawSlot(slotIn: Slot, mouseX: Int, mouseY: Int, partialTicks: Float) {
        val i: Int = slotIn.xDisplayPosition
        val j: Int = slotIn.yDisplayPosition
        GlStateManager.disableLighting()
        GlStateManager.disableDepth()
        GlStateManager.colorMask(true, true, true, false)

        val itemstack: ItemStack = slotIn.stack ?: return

        itemRender.renderItemAndEffectIntoGUI(itemstack, i, j)
        itemRender.renderItemOverlayIntoGUI(fontRendererObj, itemstack, i, j, "")
    }


}

private fun UUID.toNoDash(): Any {
    return this.toString().replace("-", "")
}
