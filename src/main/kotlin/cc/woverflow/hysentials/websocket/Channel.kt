package cc.woverflow.hysentials.websocket

import com.google.gson.JsonObject
import com.neovisionaries.ws.client.WebSocket

abstract class Channel (val name: String) {
    companion object {
        val socket: WebSocket = Socket.CLIENT

    }

    abstract fun onReceive(obj: JsonObject)
}