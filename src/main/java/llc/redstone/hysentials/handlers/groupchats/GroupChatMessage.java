package llc.redstone.hysentials.handlers.groupchats;

import cc.woverflow.chatting.chat.ChatTab;
import cc.woverflow.chatting.chat.ChatTabs;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.handlers.chat.ChatSendModule;
import llc.redstone.hysentials.util.BlockWAPIUtils;
import llc.redstone.hysentials.websocket.Request;
import llc.redstone.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.Sys;

public class GroupChatMessage implements ChatSendModule {
    @Override
    public @Nullable String onMessageSend(@NotNull String message) {
        try {
            if (ChatTabs.INSTANCE.getCurrentTab() == null) return message;
            if (message.startsWith("/")) return message;
            ChatTab tab = ChatTabs.INSTANCE.getCurrentTab();
            if (tab.getName().equals("GLOBAL")) {
                Socket.CLIENT.sendText(new Request(
                    "method", "chat",
                    "message", message,
                    "username", Minecraft.getMinecraft().thePlayer.getName(),
                    "server", false,
                    "displayName", Minecraft.getMinecraft().thePlayer.getName(),
                    "key", Socket.serverId
                ).toString());
                return null;
            }
//            for (BlockWAPIUtils.Group group : Hysentials.INSTANCE.getOnlineCache().groups) {
//                if (group.getName().equalsIgnoreCase(tab.getName())) {
//                    Socket.CLIENT.sendText(
//                        new Request(
//                            "method", "groupChat",
//                            "name", group.getName(),
//                            "username", Minecraft.getMinecraft().thePlayer.getName(),
//                            "serverId", Socket.serverId,
//                            "message", message
//                        ).toString()
//                    );
//                    return null;
//                }
//            }
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            return message;
        }
    }
}
