package llc.redstone.hysentials.websocket

import cc.polyfrost.oneconfig.utils.Multithreading
import com.google.gson.JsonParser
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketAdapter
import com.neovisionaries.ws.client.WebSocketFrame
import com.neovisionaries.ws.client.WebSocketListener
import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.VERSION
import llc.redstone.hysentials.config.HysentialsConfig
import llc.redstone.hysentials.util.MUtils
import llc.redstone.hysentials.websocket.Channel.Companion.channels
import llc.redstone.hysentials.websocket.Socket.*
import net.minecraft.client.Minecraft
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit

class HysentialsListener : WebSocketAdapter() {
    fun WebSocket.send(message: String) {
        this.sendText(message)
    }

    override fun onConnected(websocket: WebSocket, headers: Map<String, List<String>>) {
        println("Connected to websocket server")
        relogAttempts = 0
        future?.cancel(true)
        val obj = JSONObject()
        obj.put("method", "login")
        obj.put("username", Minecraft.getMinecraft().session.username)
        obj.put("version", VERSION)
        obj.put("key", serverId)
        websocket.send(obj.toString())
    }

    @Throws(Exception::class)
    override fun onDisconnected(
        websocket: WebSocket?,
        serverCloseFrame: WebSocketFrame?,
        clientCloseFrame: WebSocketFrame?,
        closedByServer: Boolean
    ) {
        if (manualDisconnect) {
            manualDisconnect = false
            relogAttempts = 0
            return
        }

        linking = false
        linkingData = null
        relogAttempts++
        if (relogAttempts > 2) {
            MUtils.chat(HysentialsConfig.chatPrefix + " §cFailed to connect to websocket server. This is probably because it is offline. Please try again later with `/hs reconnect`.")
            return
        }
        MUtils.chat(HysentialsConfig.chatPrefix + " §cDisconnected from websocket server. Attempting to reconnect in 20 seconds")
        future = Multithreading.submitScheduled({ createSocket() }, 20, TimeUnit.SECONDS)
    }

    override fun onTextMessage(websocket: WebSocket, text: String) {
        val json = JsonParser().parse(text).asJsonObject
        for (await in awaiting) {
            if (await.first == json["method"]?.asString) {
                await.second.accept(json)
                awaiting.remove(await)
            }
        }

        if (channels[json["method"]?.asString] == null) return
        val channel = channels[json["method"]?.asString]!!
        if (channel.name == json["method"]?.asString) {
            channel.socket = websocket
            channel.onReceive(JsonParser().parse(json.toString()).getAsJsonObject())
        }
    }
}