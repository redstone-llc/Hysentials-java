package cc.woverflow.hysentials.cosmetic

import cc.woverflow.hysentials.HysentialsKt
import cc.woverflow.hysentials.util.BlockWAPIUtils
import cc.woverflow.hysentials.util.NetworkUtils
import cc.woverflow.hysentials.websocket.Socket
import kotlinx.coroutines.launch
import net.minecraft.client.Minecraft

class CosmeticManager {
    fun unEquipCosmetic(name: String, cb: (response: String) -> Unit) {
        HysentialsKt.IO.launch {
            val name = name.replace(" ", "%20")
            val response =
                NetworkUtils.postString("https://hysentials.redstone.llc/api/cosmetic?name=$name&function=unequip&uuid=${Minecraft.getMinecraft().thePlayer.uniqueID}&key=${Socket.serverId}")
            BlockWAPIUtils.getOnline()
            cb(response)
        }
    }

    fun equipCosmetic(name: String, cb: (response: String) -> Unit) {
        HysentialsKt.IO.launch {
            val name = name.replace(" ", "%20")
            val response =
                NetworkUtils.postString("https://hysentials.redstone.llc/api/cosmetic?name=$name&function=equip&uuid=${Minecraft.getMinecraft().thePlayer.uniqueID}&key=${Socket.serverId}")
            BlockWAPIUtils.getOnline()
            cb(response)
        }
    }

    fun purchaseCosmetic(cosmeticName: String, cb: (response: String) -> Unit) {
        HysentialsKt.IO.launch {
            val cosmeticName = cosmeticName.replace(" ", "%20")
            val response =
                NetworkUtils.postString("https://hysentials.redstone.llc/api/cosmetic?name=$cosmeticName&function=purchase&uuid=${Minecraft.getMinecraft().thePlayer.uniqueID}&key=${Socket.serverId}")
            BlockWAPIUtils.getOnline()
            cb(response)
        }
    }
}