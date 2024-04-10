package llc.redstone.hysentials.websocket.methods.club

import cc.polyfrost.oneconfig.libs.universal.UChat
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent
import com.google.gson.JsonObject
import llc.redstone.hysentials.config.HysentialsConfig
import llc.redstone.hysentials.websocket.Channel
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent

class ClubInvite: Channel("clubInvite") {
    override fun onReceive(obj: JsonObject) {
        val club = obj.getAsJsonObject("club")
        UChat.chat("&b-----------------------------------------------------")
        UTextComponent(
            "§eYou have been invited to join the §6" + club["name"].asString + " §eclub. " +
                    "Type §6`/club join " + club["name"].asString + "` §eto join!"
        )
            .setHover(HoverEvent.Action.SHOW_TEXT, "§eClick to join!")
            .setClick(ClickEvent.Action.RUN_COMMAND, "/club join " + club["name"].asString)
            .chat()
        UChat.chat(HysentialsConfig.chatPrefix + " §eThis invite will expire in 5 minutes.")
        UChat.chat("&b-----------------------------------------------------")
    }

}