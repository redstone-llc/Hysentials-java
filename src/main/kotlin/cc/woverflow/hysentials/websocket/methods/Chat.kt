package cc.woverflow.hysentials.websocket.methods

import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent
import cc.woverflow.hysentials.config.HysentialsConfig
import cc.woverflow.hysentials.util.BlockWAPIUtils.Rank
import cc.woverflow.hysentials.util.MUtils
import cc.woverflow.hysentials.websocket.Channel
import cc.woverflow.hysentials.websocket.Socket
import com.google.gson.JsonObject
import net.minecraft.client.Minecraft
import net.minecraft.event.HoverEvent.Action.SHOW_TEXT
import net.minecraft.util.IChatComponent
import java.util.*

class Chat : Channel("chat") {
    var username: String? = null
    var uuid: String? = null
    var message: String? = null
    override fun onReceive(obj: JsonObject) {
        username = obj["username"].asString
        uuid = obj["uuid"]?.asString
        message = obj["message"].asString

        if (!HysentialsConfig.globalChatEnabled) return
        if (username == "HYPIXELCONSOLE" && uuid == null) {
            MUtils.chat(HysentialsConfig.chatPrefix + " §c" + message)
            return
        }
        val rank = Socket.cachedUsersNew[uuid]?.rank ?: "DEFAULT"
        val cachedUsername = Socket.cachedUsersNew[uuid]?.username ?: username
        val rankObj = Rank.valueOf(rank.uppercase())
        if (HysentialsConfig.fancyFormatting && HysentialsConfig.channelFormatting) {
            val component: IChatComponent = UTextComponent(":globalchat: ")
                .appendSibling(
                    UTextComponent(
                        "&6$username"
                    ).setHover(
                        SHOW_TEXT,
                        "${rankObj.placeholder} $username"
                    )
                )
                .appendText("${(if (HysentialsConfig.hexColors) "<#fff1d4>: " else "§6:")}: $message")
            Minecraft.getMinecraft().thePlayer.addChatMessage(component)
            return
        }
        val component: IChatComponent = UTextComponent("&6Global > ")
            .appendSibling(
                UTextComponent(
                    "&6$username"
                ).setHover(
                    SHOW_TEXT,
                    "${rankObj.prefix} $username"
                )
            )
            .appendText("${(if (HysentialsConfig.hexColors && HysentialsConfig.fancyFormatting) "<#fff1d4>: " else "§6:")}: $message")
        Minecraft.getMinecraft().thePlayer.addChatMessage(component)
    }
}