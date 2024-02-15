package llc.redstone.hysentials.cosmetic

import llc.redstone.hysentials.HYSENTIALS_API
import llc.redstone.hysentials.IO
import llc.redstone.hysentials.util.BlockWAPIUtils
import llc.redstone.hysentials.util.NetworkUtils
import llc.redstone.hysentials.websocket.Socket
import kotlinx.coroutines.launch
import llc.redstone.hysentials.cosmetic.CosmeticGui.Companion.paginationList
import llc.redstone.hysentials.schema.HysentialsSchema
import llc.redstone.hysentials.util.Renderer
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.model.IBakedModel
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import java.util.*

class CosmeticManager {
    fun unEquipCosmetic(name: String, cb: (response: String) -> Unit) {
        IO.launch {
            if (name == "kzero bundle") kzero(false)
            val name = name.replace(" ", "%20")
            val response =
                NetworkUtils.postString(HYSENTIALS_API + "/cosmetic?name=$name&function=unequip&uuid=${Minecraft.getMinecraft().thePlayer.uniqueID}&key=${Socket.serverId}")
            BlockWAPIUtils.getOnline()
            cb(response)
        }
    }

    fun equipCosmetic(name: String, cb: (response: String) -> Unit) {
        IO.launch {
            if (name == "kzero bundle") kzero(true)
            val name = name.replace(" ", "%20")
            val response =
                NetworkUtils.postString(HYSENTIALS_API + "/cosmetic?name=$name&function=equip&uuid=${Minecraft.getMinecraft().thePlayer.uniqueID}&key=${Socket.serverId}")
            BlockWAPIUtils.getOnline()
            cb(response)
        }
    }

    suspend fun kzero (equip: Boolean) {
        var list = listOf(
            "kzero hair",
            "kzero robe",
            "kzero slipper"
        )
        for (cosmetic in list) {
            val name = cosmetic.replace(" ", "%20")
            if (equip) {
                NetworkUtils.postString(HYSENTIALS_API + "/cosmetic?name=$name&function=equip&uuid=${Minecraft.getMinecraft().thePlayer.uniqueID}&key=${Socket.serverId}")
            } else {
                NetworkUtils.postString(HYSENTIALS_API + "/cosmetic?name=$name&function=unequip&uuid=${Minecraft.getMinecraft().thePlayer.uniqueID}&key=${Socket.serverId}")
            }
        }
    }

    fun purchaseCosmetic(cosmeticName: String, cb: (response: String) -> Unit) {
        IO.launch {
            val cosmeticName = cosmeticName.replace(" ", "%20")
            val response =
                NetworkUtils.postString(HYSENTIALS_API + "/cosmetic?name=$cosmeticName&function=purchase&uuid=${Minecraft.getMinecraft().thePlayer.uniqueID}&key=${Socket.serverId}")
            BlockWAPIUtils.getOnline()
            cb(response)
        }
    }
}

val slotResource = ResourceLocation("hysentials:gui/wardrobe/selected_slot.png")
fun drawSlot(slotIn: Slot, mouseX: Int, mouseY: Int, partialTicks: Float) {
    val i: Int = slotIn.xDisplayPosition
    val j: Int = slotIn.yDisplayPosition
    GlStateManager.disableLighting()
    GlStateManager.disableDepth()
    GlStateManager.colorMask(true, true, true, false)
    val page = paginationList!!.getPage(CosmeticGui.page)
    if (page.size > slotIn.slotIndex) {
        val cosmetic = page[slotIn.slotIndex]
        val name = cosmetic.name
        val uuid = Minecraft.getMinecraft().thePlayer.uniqueID
        if (equippedCosmetic(uuid, name)) {
            Renderer.drawImage(slotResource, i.toDouble(), j.toDouble(), 25.0, 26.0)
        }
    }
    val itemstack: ItemStack = slotIn.stack ?: return

    val ibakedmodel: IBakedModel = Minecraft.getMinecraft().renderItem.itemModelMesher.getItemModel(itemstack)
    val width = ibakedmodel.particleTexture.iconWidth
    val height = ibakedmodel.particleTexture.iconHeight
    Minecraft.getMinecraft().renderItem.renderItemAndEffectIntoGUI(itemstack, i + (25 - width) / 2, j + (26 - height) / 2)
    Minecraft.getMinecraft().renderItem.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRendererObj, itemstack, i + (25 - width) / 2, j + (26 - height) / 2, "")

    Minecraft.getMinecraft().renderItem.zLevel = 0.0f
    GlStateManager.enableLighting()
    GlStateManager.enableDepth()
    GlStateManager.colorMask(true, true, true, true)
}


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

fun tabFromType(type: String): CosmeticTab? {
    return CosmeticGui.tabs.filter { it.name == type }.firstOrNull()
}