package llc.redstone.hysentials.websocket.methods

import cc.polyfrost.oneconfig.libs.universal.UChat
import cc.polyfrost.oneconfig.utils.Multithreading
import com.google.gson.JsonObject
import llc.redstone.hysentials.config.HysentialsConfig
import llc.redstone.hysentials.util.MUtils
import llc.redstone.hysentials.websocket.Channel
import llc.redstone.hysentials.websocket.Socket
import java.util.concurrent.TimeUnit

class LinkRequest: Channel("link") {
    override fun onReceive(obj: JsonObject) {
        UChat.chat(HysentialsConfig.chatPrefix + " §fA link request has been made, please type §6`/hysentials link` §fto link your account. §fThis will expire in 5 minutes. If this was not you, please ignore this!")
        Socket.linking = true
        Socket.linkingData = obj

        Multithreading.schedule({
            Socket.linking = false
            Socket.linkingData = null
        }, 5, TimeUnit.MINUTES)
    }
}