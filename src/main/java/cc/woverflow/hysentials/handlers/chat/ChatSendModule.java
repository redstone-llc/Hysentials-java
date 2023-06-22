package cc.woverflow.hysentials.handlers.chat;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Since Forge lacks a {@link ClientChatReceivedEvent} for <em>sending</em> messages, this interface
 * is used to implement such an event.
 * <p>
 * Register your class in {@link cc.woverflow.hysentials.handlers.chat.ChatHandler#ChatHandler()} and it will be executed whenever the user sends a chat message.
 *
 * @see cc.woverflow.hysentials.handlers.chat.ChatModule
 * @see ChatHandler
 */
public interface ChatSendModule extends cc.woverflow.hysentials.handlers.chat.ChatModule {

    /**
     * Place your code here. Called when the user sends a chat message.
     *
     * @param message message that the user sent
     * @return the optionally modified message, or {@code null} if the message is to be cancelled
     */
    @Nullable
    String onMessageSend(@NotNull String message);

}
