package llc.redstone.hysentials.websocket.methods

import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent
import com.google.gson.JsonObject
import llc.redstone.hysentials.Hysentials
import llc.redstone.hysentials.config.HysentialsConfig
import llc.redstone.hysentials.config.hysentialMods.ChatConfig
import llc.redstone.hysentials.config.hysentialMods.FormattingConfig
import llc.redstone.hysentials.util.BlockWAPIUtils
import llc.redstone.hysentials.util.MUtils
import llc.redstone.hysentials.websocket.Channel
import net.minecraft.client.Minecraft
import net.minecraft.event.HoverEvent
import java.util.*

class Chat : Channel("chat") {
    override fun onReceive(obj: JsonObject) {
        var username = obj["username"].asString
        var uuid = obj["uuid"]?.asString
        var message = obj["message"].asString

        if (!ChatConfig.globalChat || !Hysentials.INSTANCE.getConfig().chatConfig.enabled) return
        if (username == "HYPIXELCONSOLE" && uuid == null) { // Console message, really shouldnt be HYPIXELCONSOLE but whatever
            MUtils.chat(HysentialsConfig.chatPrefix + " §c" + message)
            return
        }
        if (uuid == null) return
        val rank: BlockWAPIUtils.Rank = BlockWAPIUtils.getRank(uuid)

        // Create the component keeping in mind that the global prefix may be a fancy string
        var component = if (!ChatConfig.globalPrefix.contains(Regex(":(.+):")) || !FormattingConfig.fancyRendering()) {
            UTextComponent("&6Global > ")
        } else {
            UTextComponent(ChatConfig.globalPrefix)
        }

        // Much more reliable way to get the username
        var hoverUsername = username;
        if (rank != BlockWAPIUtils.Rank.DEFAULT) {
            hoverUsername = BlockWAPIUtils.getUsername(UUID.fromString(uuid))
        }

        // Remove the suffix if it's disabled
        if (!ChatConfig.globalChatSuffix) {
            username = username.split(" ")[0]
        }

        component.appendSibling(
            UTextComponent(
                "&6$username"
            ).setHover(HoverEvent.Action.SHOW_TEXT, (rank.prefixCheck + hoverUsername)) // Hover over the username to see the rank
        ).appendSibling(
            UTextComponent(
                " ${if (FormattingConfig.hexRendering()) "<#fff1d4>" else "&6"}" + // Color of the message
                        ": $message" // The message
            )
        )

        Minecraft.getMinecraft().thePlayer.addChatMessage(component)
    }
}