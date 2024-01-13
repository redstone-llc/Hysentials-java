package llc.redstone.hysentials.websocket.methods

import llc.redstone.hysentials.websocket.Channel
import com.google.gson.JsonObject

class DoorbellAuthenticate : Channel("doorbellSendToken") {
    override fun onReceive(obj: JsonObject) {
        val token = obj["token"].asString //JWT token for doorbell

        println("Doorbell Authenticated with token: $token")
    }
}