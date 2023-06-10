package cc.woverflow.hysentials.handlers.chat.modules.bwranks;

import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.handlers.chat.ChatReceiveModule;
import cc.woverflow.hysentials.handlers.redworks.BwRanksUtils;
import cc.woverflow.hysentials.util.BlockWAPIUtils;
import cc.woverflow.hysentials.util.C;
import cc.woverflow.hysentials.util.HypixelRanks;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.Sys;

public class FriendReplace implements ChatReceiveModule {
    public static boolean collecting = false;
    @Override
    public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
        if (!HypixelUtils.INSTANCE.isHypixel()) return;
        if (event.type != 0 && event.type != 1) return;
        String message = event.message.getFormattedText().replaceAll("§r", "");
        String[] split = message.split("\n");
        if (split.length < 4) return;
        if (C.removeColor(split[1]).replace(" ", "").startsWith("Friends(Page") || C.removeColor(split[1]).replace(" ", "").startsWith("<<Friends(Page")) {
            collecting = true;
        }


        if (!collecting) return;
        for (int i = 2; i < split.length - 1; i++) {
            String s = split[i];
            String name = s.split(" ")[0];
            name = name.replace("§9", "");
            for (HypixelRanks rank : HypixelRanks.values()) {
                if (rank.getColor().equals(name.substring(0, 2))) {
                    message = message.replaceAll(name, rank.getNametag() + name.substring(2));
                }
            }
        }
        UChat.chat(message);
        event.setCanceled(true);
        collecting = false;
    }
}
