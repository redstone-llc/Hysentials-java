/*
 * Hytils Reborn - Hypixel focused Quality of Life mod.
 * Copyright (C) 2022  W-OVERFLOW
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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