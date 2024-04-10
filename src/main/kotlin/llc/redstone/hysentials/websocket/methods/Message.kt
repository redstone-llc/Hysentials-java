package llc.redstone.hysentials.websocket.methods

import cc.polyfrost.oneconfig.libs.universal.UChat
import com.google.gson.JsonObject
import llc.redstone.hysentials.guis.misc.HysentialsLevel
import llc.redstone.hysentials.util.MUtils
import llc.redstone.hysentials.websocket.Channel

class Message: Channel("message") {
    override fun onReceive(json: JsonObject) {
        if (json.has("type")) {
            when (json["type"].asString) {
                "level" -> {
                    HysentialsLevel.checkLevel(json["data"].asJsonObject)
                }
            }
        } else if (json.has("message")) {
            UChat.chat(json["message"].asString)
        }
    }
}