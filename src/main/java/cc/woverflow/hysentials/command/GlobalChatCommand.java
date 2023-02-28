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

package cc.woverflow.hysentials.command;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.websocket.Socket;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class GlobalChatCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "globalchat";
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("glc", "glchat");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/globalchat <message>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (!HypixelUtils.INSTANCE.isHypixel()) {
            UChat.chat(HysentialsConfig.chatPrefix + "&cYou are not in a Hypixel server!");
            return;
        }
        if (!HysentialsConfig.globalChatEnabled) {
            UChat.chat(HysentialsConfig.chatPrefix + "&cGlobal chat is disabled!");
            return;
        }
        if (args.length == 0) {
            UChat.chat(HysentialsConfig.chatPrefix + "&cInvalid usage! /globalchat <message>");
            return;
        }

        String message = String.join(" ", args);
        JSONObject json = new JSONObject();
        json.put("method", "chat");
        json.put("message", message);
        json.put("username", sender.getName());
        json.put("server", false);
        json.put("displayName", sender.getDisplayName()); //This gets overwritten by the server lol!
        json.put("key", Socket.serverId);

        Socket.CLIENT.send(json.toString());
    }

}
