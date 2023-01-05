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
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.commands.annotations.Command;
import cc.polyfrost.oneconfig.utils.commands.annotations.Description;
import cc.polyfrost.oneconfig.utils.commands.annotations.Main;
import cc.polyfrost.oneconfig.utils.commands.annotations.SubCommand;
import cc.woverflow.chatting.chat.ChatTab;
import cc.woverflow.chatting.chat.ChatTabs;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.util.HypixelAPIUtils;
import cc.woverflow.hysentials.websocket.Socket;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Command(value = "hysentials", aliases = {"hs"})
public class HysentialsCommand {
    public static boolean collecting = false;
    public static List<String> messages = new ArrayList<>();

    static {

    }

    @Main
    private void handleDefault() {
        Hysentials.INSTANCE.getConfig().openGui();
    }

    @SubCommand(description = "open config", aliases = "config")
    private static void config() {
        Hysentials.INSTANCE.getConfig().openGui();
    }

    @SubCommand(description = "Sets your API key.", aliases = "setkey")
    private static void key(@Description("API Key") String apiKey) {
        Multithreading.runAsync(() -> {
            if (HypixelAPIUtils.isValidKey(apiKey)) {
                HytilsConfig.apiKey = apiKey;
                HytilsReborn.INSTANCE.getConfig().save();
                Hysentials.INSTANCE.sendMessage(EnumChatFormatting.GREEN + "Saved API key successfully!");
            } else {
                Hysentials.INSTANCE.sendMessage(EnumChatFormatting.RED + "Invalid API key! Please try again.");
            }
        });
    }

    @SubCommand(description = "", aliases = "collect")
    private static void collect() {
        collecting = !collecting;
        if (!collecting) {
            UChat.chat("Collected data: " + ArrayUtils.toString(messages));
        }
        if (collecting) {
            messages = new ArrayList<>();
        }
        //disabled enabled message
        UChat.chat(EnumChatFormatting.GRAY + "Collecting " + (collecting ? EnumChatFormatting.GREEN + "enabled" : EnumChatFormatting.RED + "disabled"));
    }

    @SubCommand(description = "Link your discord account to your minecraft account.", aliases = "link")
    private static void link() {
        if (Socket.linking) {
            Multithreading.runAsync(() -> {
                JSONObject response = Socket.data;
                response.put("server", false);
                System.out.println(response);
                Socket.CLIENT.send(response.toString());
                Socket.linking = false;
                Socket.data = null;
                UChat.chat(HysentialsConfig.chatPrefix + " §aSuccessfully linked your discord account to your minecraft account!");
            });
        }
    }
}
