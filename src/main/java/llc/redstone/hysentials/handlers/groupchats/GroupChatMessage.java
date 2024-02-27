package llc.redstone.hysentials.handlers.groupchats;

import org.json.JSONObject;
import org.polyfrost.chatting.chat.ChatTab;
import org.polyfrost.chatting.chat.ChatTabs;
import llc.redstone.hysentials.handlers.chat.ChatSendModule;
import llc.redstone.hysentials.websocket.Request;
import llc.redstone.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GroupChatMessage implements ChatSendModule {
    @Override
    public @Nullable String onMessageSend(@NotNull String message) {
        try {
            if (ChatTabs.INSTANCE.getCurrentTabs() == null) return message;
            if (message.startsWith("/")) return message;
            ChatTab tab = ChatTabs.INSTANCE.getCurrentTabs().get(0);
            if (tab == null) return message;
            if (tab.getName().equals("GLOBAL")) {
                JSONObject json = new JSONObject();
                json.put("method", "chat");
                json.put("message", message);
                json.put("username", Minecraft.getMinecraft().thePlayer.getName());
                json.put("uuid", Minecraft.getMinecraft().thePlayer.getUniqueID().toString());
                json.put("server", false);
                json.put("displayName", Minecraft.getMinecraft().thePlayer.getDisplayName().getFormattedText()); //This gets overwritten by the server lol!
                json.put("key", Socket.serverId);
                Socket.CLIENT.sendText(json.toString());
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
