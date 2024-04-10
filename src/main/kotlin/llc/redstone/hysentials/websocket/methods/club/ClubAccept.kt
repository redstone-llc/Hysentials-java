package llc.redstone.hysentials.websocket.methods.club

import cc.polyfrost.oneconfig.libs.universal.UChat
import com.google.gson.JsonObject
import llc.redstone.hysentials.config.HysentialsConfig
import llc.redstone.hysentials.websocket.Channel

class ClubAccept: Channel("clubAccept") {
    override fun onReceive(obj: JsonObject) {
        if (obj.get("success")?.asBoolean == true) {
            UChat.chat(HysentialsConfig.chatPrefix + " §aSuccessfully joined club!")
        } else {
            UChat.chat(HysentialsConfig.chatPrefix + " §cFailed to join club!")
        }
    }

}