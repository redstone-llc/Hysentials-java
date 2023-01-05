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

package cc.woverflow.hysentials.websocket;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.handlers.groupchats.GroupChat;
import cc.woverflow.hysentials.util.DuoVariable;
import cc.woverflow.hytils.config.HytilsConfig;
import com.mojang.authlib.exceptions.AuthenticationException;
import net.minecraft.client.Minecraft;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import scala.util.parsing.json.JSON;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

public class Socket {
    public static WebSocketClient CLIENT;
    public static String serverId;
    public static boolean linking = false;
    public static JSONObject data = null;
    public static List<DuoVariable<String, Consumer<JSONObject>>> awaiting = new ArrayList<>();

    public static void createSocket() {
        try {
            serverId = UUID.randomUUID().toString().replace("-", "");
            Minecraft.getMinecraft().getSessionService().joinServer(Minecraft.getMinecraft().getSession().getProfile(), Minecraft.getMinecraft().getSession().getToken(), serverId);

            WebSocketClient ws = new WebSocketClient(new URI("ws://localhost:8080")) {
            //WebSocketClient ws = new WebSocketClient(new URI("ws://137.184.54.174:8080")) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("Connected to websocket server");
                    JSONObject obj = new JSONObject();
                    obj.put("method", "login");
                    obj.put("username", Minecraft.getMinecraft().getSession().getUsername());
                    obj.put("key", serverId);
                    send(obj.toString());
                }

                @Override
                public void onMessage(String message) {
                    JSONObject json = new JSONObject(message);
                    if (json.has("success") && json.has("authenticated") && json.getBoolean("success") && json.getBoolean("authenticated")) {
                        UChat.chat(HysentialsConfig.chatPrefix + " §aLogged in successfully!");
                        CLIENT = this;
                    }
                    if (json.has("method")) {
                        switch (json.getString("method")) {
                            case "chat": {
                                UChat.chat((HytilsConfig.shortChannelNames ? "&6Gl > " : "&6Global > ")
                                    + json.getString("displayName")
                                    + "&f: "
                                    + json.getString("message"));
                                break;
                            }

                            case "link": {
                                UChat.chat(HysentialsConfig.chatPrefix + " §fA link request has been made, please type §6`/hysentials link` §fto link your account. §fThis will expire in 5 minutes. If this was not you, please ignore this!");
                                linking = true;
                                data = json;

                                Multithreading.schedule(() -> {
                                    linking = false;
                                    data = null;
                                }, 5, TimeUnit.MINUTES);
                            }

                            case "groupChat": {
                                GroupChat.chat(json);
                                break;
                            }

                            case "groupInvite": {
                                GroupChat.invite(json);
                                break;
                            }
                        }
                    }

                    for (int i = 0, awaitingSize = awaiting.size(); i < awaitingSize; i++) {
                        DuoVariable<String, Consumer<JSONObject>> value = awaiting.get(i);
                        if (json.has("method") && json.getString("method").equals(value.getFirst())) {
                            value.getSecond().accept(json);
                            awaiting.remove(i);
                        }
                    }


                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    linking = false;
                    data = null;
                    UChat.chat(HysentialsConfig.chatPrefix + " §cDisconnected from websocket server. Attempting to reconnect in 5 seconds");
                    Multithreading.schedule(Socket::createSocket, 5, TimeUnit.SECONDS);
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };
            ws.connect();
        } catch (URISyntaxException | AuthenticationException e) {
            e.printStackTrace();
        }
    }
}
