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

package cc.woverflow.hysentials.handlers.groupchats;

import cc.woverflow.chatting.chat.ChatTab;
import cc.woverflow.chatting.chat.ChatTabs;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.handlers.chat.ChatSendModule;
import cc.woverflow.hysentials.util.BlockWAPIUtils;
import cc.woverflow.hysentials.websocket.Request;
import cc.woverflow.hysentials.websocket.Socket;
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
            for (BlockWAPIUtils.Group group : Hysentials.INSTANCE.getOnlineCache().groups) {
                if (group.getName().equalsIgnoreCase(tab.getName())) {
                    Socket.CLIENT.send(
                        new Request(
                            "method", "groupChat",
                            "name", group.getName(),
                            "username", Minecraft.getMinecraft().thePlayer.getName(),
                            "serverId", Socket.serverId,
                            "message", message
                        ).toString()
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
