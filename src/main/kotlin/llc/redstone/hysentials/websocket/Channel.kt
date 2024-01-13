package llc.redstone.hysentials.websocket

import com.google.gson.JsonObject
import com.neovisionaries.ws.client.WebSocket


abstract class Channel (val name: String) {
    init {
        channels[name] = this
    }
    companion object {
        val channels = mutableMapOf<String, Channel>()
    }

    abstract fun onReceive(obj: JsonObject)
}