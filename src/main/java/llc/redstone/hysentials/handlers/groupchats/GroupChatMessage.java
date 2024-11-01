package llc.redstone.hysentials.handlers.groupchats;

import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.schema.HysentialsSchema;
import org.json.JSONObject;
import org.polyfrost.chatting.chat.ChatTab;
import org.polyfrost.chatting.chat.ChatTabs;
import llc.redstone.hysentials.handlers.chat.ChatSendModule;
import llc.redstone.hysentials.websocket.Request;
import llc.redstone.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static llc.redstone.hysentials.command.GroupChatCommand.sendAction;

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
                json.put("username", UMinecraft.getPlayer().getName());
                json.put("uuid", UMinecraft.getPlayer().getUniqueID().toString());
                json.put("server", false);
                json.put("displayName", UMinecraft.getPlayer().getDisplayName().getFormattedText()); //This gets overwritten by the server lol!
                json.put("key", Socket.serverId);
                Socket.CLIENT.sendText(json.toString());
                return null;
            }
            for (HysentialsSchema.Group group : Socket.cachedGroups) {
                if (group.getName().equalsIgnoreCase(tab.getName())) {
                    sendAction("message",
                        "groupId", group.getId(),
                        "message", message
                    );
                    return null;
                }
            }
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            return message;
        }
    }
}
