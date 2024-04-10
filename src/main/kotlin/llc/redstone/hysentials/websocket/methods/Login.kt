package llc.redstone.hysentials.websocket.methods

import cc.polyfrost.oneconfig.utils.Multithreading
import com.google.gson.JsonObject
import llc.redstone.hysentials.HYSENTIALS_API
import llc.redstone.hysentials.config.HysentialsConfig
import llc.redstone.hysentials.util.BlockWAPIUtils
import llc.redstone.hysentials.util.MUtils
import llc.redstone.hysentials.util.NetworkUtils
import llc.redstone.hysentials.websocket.Channel
import llc.redstone.hysentials.websocket.Socket
import org.json.JSONObject

class Login: Channel("login") {
    override fun onReceive(json: JsonObject) {
        Socket.relogAttempts = 0
        if (json["success"]?.asBoolean == false) {
            Socket.banned = true
            Socket.banReason = json["status"]?.asString ?: "Unknown"
            Socket.relogAttempts = 3
        }

        if (json["success"]?.asBoolean == true) {
            MUtils.chat(HysentialsConfig.chatPrefix + " §aLogged in successfully!")
            Socket.CLIENT = socket
            Socket.sockets.add(socket)
            if (Socket.sockets.size > 1) {
                for (i in Socket.sockets.indices) {
                    val socket = Socket.sockets[i]
                    if (i != Socket.sockets.size - 1) {
                        socket.disconnect()
                        Socket.sockets.remove(socket)
                    }
                }
            }
            Multithreading.runAsync {
                BlockWAPIUtils.getOnline()
                val levelRewards =
                    NetworkUtils.getString("$HYSENTIALS_API/rewards")
                if (levelRewards != null) {
                    val rewards = JSONObject(levelRewards)
                    if (rewards.has("rewards")) {
                        Socket.cachedRewards = rewards.getJSONObject("rewards")
                    }
                }
            }
        }

        if (json["linked"]?.asBoolean == false) {
            Socket.linked = false
            MUtils.chat(HysentialsConfig.chatPrefix + " §cYou are not linked to a discord account! Some features will not work.")
        } else {
            Socket.linked = true
        }

        Socket.cachedUsers.clear()
        Socket.cachedUser = null
        Socket.cachedGroups.clear()
    }
}