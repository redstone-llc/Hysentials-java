package llc.redstone.hysentials.websocket.methods

import cc.polyfrost.oneconfig.libs.universal.UChat
import com.google.gson.JsonObject
import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.websocket.Channel
import llc.redstone.hysentials.websocket.Socket
import org.polyfrost.chatting.chat.ChatTab
import org.polyfrost.chatting.chat.ChatTabs
import java.util.*
import kotlin.collections.ArrayList

class Group : Channel("group") {
    override fun onReceive(obj: JsonObject) {
        if (obj.has("message")) {
            val success = obj["success"].asBoolean
            val message = obj["message"].asString
            Hysentials.INSTANCE.sendMessage(if (success) "§a$message" else "§c$message")
        }
        if (!obj.has("action")) return
        val action = obj["action"].asString
        when (action) {
            "message" -> {
                val groupId = obj["groupId"].asString
                val message = obj["chat"].asString
                val sender = obj["sender"].asString
                val senderName = obj["senderName"].asString
                val group = Socket.cachedGroups.find { it.id == groupId } ?: return
                // One day we will probably do fancy things with this
                UChat.chat(message)
                val tab = ChatTabs.INSTANCE.tabs.stream().filter { t: ChatTab ->
                    t.name == group.name.uppercase(
                        Locale.getDefault()
                    )
                }.findFirst().orElse(null)
                if (tab != null) {
                    tab.messages = ArrayList(tab.messages).apply { add(0, message) }
                }
            }
        }
    }
}