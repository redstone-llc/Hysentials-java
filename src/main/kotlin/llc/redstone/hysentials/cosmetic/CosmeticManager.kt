package llc.redstone.hysentials.cosmetic

import llc.redstone.hysentials.HYSENTIALS_API
import llc.redstone.hysentials.IO
import llc.redstone.hysentials.util.BlockWAPIUtils
import llc.redstone.hysentials.util.NetworkUtils
import llc.redstone.hysentials.websocket.Socket
import kotlinx.coroutines.launch
import net.minecraft.client.Minecraft

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