package cc.woverflow.hysentials.cosmetic

import cc.woverflow.hysentials.HYSENTIALS_API
import cc.woverflow.hysentials.IO
import cc.woverflow.hysentials.util.BlockWAPIUtils
import cc.woverflow.hysentials.util.NetworkUtils
import cc.woverflow.hysentials.websocket.Socket
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