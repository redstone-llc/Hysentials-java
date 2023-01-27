package cc.woverflow.hysentials.handlers.chat.modules.misc;

import cc.woverflow.hysentials.command.HypixelChatCommand;
import cc.woverflow.hysentials.handlers.chat.ChatReceiveModule;
import cc.woverflow.hysentials.handlers.chat.ChatSendModule;
import cc.woverflow.hysentials.user.Player;
import cc.woverflow.hysentials.websocket.Request;
import cc.woverflow.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GlobalChatStuff {
    public static class GlobalInChannel implements ChatReceiveModule {
        @Override
        public void onMessageReceived(@NotNull ClientChatReceivedEvent event) {
            if (HypixelChatCommand.isInGlobalChat) {
                if (getStrippedMessage(event.message).equals("You're already in this channel!")) {
                    event.setCanceled(true);
                    HypixelChatCommand.isInGlobalChat = false;
                }
            }
        }
    }

    public static class GlobalSendMessage implements ChatSendModule {

        @Override
        public @Nullable String onMessageSend(@NotNull String message) {
            if (message.startsWith("/")) return message;
            if (HypixelChatCommand.isInGlobalChat) {
                Socket.CLIENT.send(new Request(
                    "method", "chat",
                    "message", message,
                    "username", Minecraft.getMinecraft().thePlayer.getName(),
                    "server", false,
                    "displayName", Minecraft.getMinecraft().thePlayer.getName(),
                    "key", Socket.serverId
                    ).toString());
                return null;
            }
            return message;
        }
    }
}
