package llc.redstone.hysentials.cosmetic

import cc.polyfrost.oneconfig.utils.Multithreading
import com.google.gson.JsonElement
import llc.redstone.hysentials.HYSENTIALS_API
import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.schema.HysentialsSchema
import llc.redstone.hysentials.schema.HysentialsSchema.Cosmetic.Companion.deserialize
import llc.redstone.hysentials.util.BlockWAPIUtils
import llc.redstone.hysentials.util.NetworkUtils
import llc.redstone.hysentials.util.Renderer
import llc.redstone.hysentials.websocket.Socket
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.resources.model.IBakedModel
import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import org.json.JSONObject
import java.util.*

object CosmeticManager {
    var actions = mutableListOf<Actions>()

    data class Actions(
        var action: String,
        var name: String
    )

    var previewing = mutableListOf<String>()

    fun unEquipCosmetic(name: String, preview: Boolean = false) {
        if (name == "kzero bundle") kzero(false, preview)
        val cosmetics = BlockWAPIUtils.getCosmetics()
        val cosmetic = cosmetics.find { it.name == name }
        cosmetic?.let {
            if (it.equipped.contains(Minecraft.getMinecraft().thePlayer.uniqueID.toString())) {
                it.equipped.remove(Minecraft.getMinecraft().thePlayer.uniqueID.toString())
            }
            if (preview) {
                previewing.remove(it.name)
            } else {
                actions.add(Actions("unequip", it.name))
            }
        }
    }

    fun equipCosmetic(name: String, preview: Boolean = false) {
        if (name == "kzero bundle") kzero(true, preview)
        val cosmetics = BlockWAPIUtils.getCosmetics()
        val cosmetic = cosmetics.find { it.name == name }
        cosmetic?.let {
            if (BlockWAPIUtils.getCosmetic(it.type).isNotEmpty()) {
                BlockWAPIUtils.getCosmetic(it.type).forEach { cosmetic ->
                    if (cosmetic.equipped.contains(Minecraft.getMinecraft().thePlayer.uniqueID.toString())) {
                        unEquipCosmetic(cosmetic.name)
                        previewing.remove(cosmetic.name)
                    }
                }
            }
            if (!it.equipped.contains(Minecraft.getMinecraft().thePlayer.uniqueID.toString())) {
                it.equipped.add(Minecraft.getMinecraft().thePlayer.uniqueID.toString())
            }
            if (preview) {
                previewing.add(it.name)
            } else {
                actions.add(Actions("equip", it.name))
            }
        }
    }

    fun kzero(equip: Boolean, preview: Boolean = false) {
        var list = listOf(
            "kzero hair",
            "kzero robe",
            "kzero slipper"
        )
        for (cosmetic in list) {
            if (equip) {
                equipCosmetic(cosmetic, preview)
            } else {
                unEquipCosmetic(cosmetic, preview)
            }
        }
    }

    fun purchaseCosmetic(cosmeticName: String) {
        val cosmetics = BlockWAPIUtils.getCosmetics()
        val cosmetic = cosmetics.find { it.name == cosmeticName }
        cosmetic?.let {
            if (!it.users.contains(Minecraft.getMinecraft().thePlayer.uniqueID.toString())) {
                it.users.add(Minecraft.getMinecraft().thePlayer.uniqueID.toString())
                Socket.cachedUser.amountSpent = Socket.cachedUser.amountSpent?.plus(it.cost)
                Socket.cachedUser.emeralds = Socket.cachedUser.emeralds.minus(it.cost)
                actions.add(Actions("purchase", it.name))
            }
        }
    }

    fun updateCosmetics() {
        Multithreading.runAsync {
            for (action in actions) {
                val func = action.action
                val name = action.name

                val response =
                    NetworkUtils.postString(HYSENTIALS_API + "/cosmetic?name=$name&function=${func}&uuid=${Minecraft.getMinecraft().thePlayer.uniqueID}&key=${Socket.serverId}")
                val json = JSONObject(response)
                if (json.get("success") == false) {
                    Hysentials.INSTANCE.sendMessage(
                        "&cFailed to $func $name: ${json.get("message")}",
                    )
                }
            }
            previewing.clear()
            actions = mutableListOf()
            var cosmetics: JsonElement? =
                NetworkUtils.getJsonElement("$HYSENTIALS_API/cosmetic", true) ?: return@runAsync
            val `object` = cosmetics!!.asJsonObject
            val array = `object`.getAsJsonArray("cosmetics")
            BlockWAPIUtils.cosmetics = ArrayList()
            for (cosmeticObj in array) {
                val cosmetic = deserialize(cosmeticObj.asJsonObject)
                BlockWAPIUtils.cosmetics.add(cosmetic)
            }
        }
    }

    val slotResource = ResourceLocation("hysentials:gui/wardrobe/selected_slot.png")

    fun drawSlot(slotIn: Slot, cosmetic: HysentialsSchema.Cosmetic) {
        val i: Int = slotIn.xDisplayPosition
        val j: Int = slotIn.yDisplayPosition
        GlStateManager.disableLighting()
        GlStateManager.disableDepth()
        GlStateManager.colorMask(true, true, true, false)
        val name = cosmetic.name
        val uuid = Minecraft.getMinecraft().thePlayer.uniqueID
        if (equippedCosmetic(uuid, name)) {
            Renderer.drawImage(slotResource, i.toDouble(), j.toDouble(), 25.0, 26.0)
        }

        val itemstack: ItemStack = slotIn.stack ?: return

        val ibakedmodel: IBakedModel = Minecraft.getMinecraft().renderItem.itemModelMesher.getItemModel(itemstack)
        val width = ibakedmodel.particleTexture.iconWidth
        val height = ibakedmodel.particleTexture.iconHeight
        Minecraft.getMinecraft().renderItem.renderItemAndEffectIntoGUI(
            itemstack,
            i + (25 - width) / 2,
            j + (26 - height) / 2
        )
        Minecraft.getMinecraft().renderItem.renderItemOverlayIntoGUI(
            Minecraft.getMinecraft().fontRendererObj,
            itemstack,
            i + (25 - width) / 2,
            j + (26 - height) / 2,
            ""
        )

        Minecraft.getMinecraft().renderItem.zLevel = 0.0f
        GlStateManager.enableLighting()
        GlStateManager.enableDepth()
        GlStateManager.colorMask(true, true, true, true)


    }


    @JvmStatic
    fun equippedCosmetic(uuid: UUID, name: String): Boolean {
        try {
            val cosmetics = BlockWAPIUtils.getCosmetics()
            cosmetics.find { it.name == name }?.let {
                if (it.equipped.contains(uuid.toString()) && it.users.contains(uuid.toString())) {
                    return true
                }
            }
        } catch (_: Exception) {
        }
        return false
    }

    @JvmStatic
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

    @JvmStatic
    fun isPreviewing(uuid: UUID, name: String): Boolean {
        if (uuid != Minecraft.getMinecraft().thePlayer.uniqueID) return false
        return previewing.contains(name)
    }

    @JvmStatic
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

    @JvmStatic
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
        return CosmeticTab.tabs.filter { it.name == type }.firstOrNull()
    }
}