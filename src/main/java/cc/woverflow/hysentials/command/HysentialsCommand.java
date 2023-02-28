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
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.commands.annotations.*;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.util.HypixelAPIUtils;
import cc.woverflow.hysentials.websocket.Socket;
import cc.woverflow.hytils.HytilsReborn;
import cc.woverflow.hytils.config.HytilsConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @SubCommand(description = "test command", aliases = "test")
    private static void test(String command, @Greedy String args) {
        if (Minecraft.getMinecraft().thePlayer.getName().equals("EndKloon") || Minecraft.getMinecraft().thePlayer.getName().equals("Sin_ender")) {
            switch (command.toLowerCase()) {
                case "socket": {
                    Hysentials.INSTANCE.sendMessage("§aSocket is " + (Socket.CLIENT.isOpen() ? "connected" : "disconnected"));
                    break;
                }
                case "size": {
                    Hysentials.INSTANCE.sendMessage("§aSize: " + Minecraft.getMinecraft().fontRendererObj.getStringWidth(args));
                    break;
                }
                case "rankicon2": {
                    Hysentials.INSTANCE.sendMessage("§aTranslated Message: " + Hysentials.INSTANCE.getOnlineCache().playerDisplayNames.get(UUID.fromString(args)));
                    break;
                }
                case "alluuids": {
                    Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap().forEach(playerInfo -> {
                        UTextComponent textComponent = new UTextComponent("§a" + playerInfo.getGameProfile().getName() + ": " + playerInfo.getGameProfile().getId());
                        textComponent.setClick(ClickEvent.Action.SUGGEST_COMMAND, playerInfo.getGameProfile().getId().toString());
                        UChat.chat(textComponent);
                    });
                    break;
                }

                case "averagefps": {
                    Hysentials.INSTANCE.sendMessage("&aGetting average FPS...");
                    Multithreading.runAsync(() -> {
                        int total = 0;
                        for (int i = 0; i < 100; i++) {
                            total += Minecraft.getDebugFPS();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Hysentials.INSTANCE.sendMessage("&aAverage FPS: " + total / 100);
                    });
                    break;
                }

                case "locraw": {
                    Hysentials.INSTANCE.sendMessage("&a" + LocrawUtil.INSTANCE.getLocrawInfo().toString());
                    break;
                }
            }
        }
    }

    @SubCommand(aliases = "reconnect")
    private static void reconnect() {
        if (Socket.CLIENT != null && Socket.CLIENT.isOpen())
            Socket.CLIENT.close();
        Socket.createSocket();
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
                Socket.CLIENT.send(response.toString());
                Socket.linking = false;
                Socket.data = null;
                UChat.chat(HysentialsConfig.chatPrefix + " §aSuccessfully linked your discord account to your minecraft account!");
            });
        }
    }
}
