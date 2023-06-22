package cc.woverflow.hysentials.websocket;

import cc.woverflow.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UMessage;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.handlers.groupchats.GroupChat;
import cc.woverflow.hysentials.handlers.redworks.BwRanksUtils;
import cc.woverflow.hysentials.util.BlockWAPIUtils;
import cc.woverflow.hysentials.util.DuoVariable;
import com.mojang.authlib.exceptions.AuthenticationException;
import kotlin.random.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Socket {
    public static WebSocketClient CLIENT;
    public static JSONObject cachedData = new JSONObject();
    public static JSONObject cachedServerData = new JSONObject();
    public static String serverId;
    public static boolean linking = false;
    public static boolean linked = false;
    public static JSONObject data = null;
    public static List<DuoVariable<String, Consumer<JSONObject>>> awaiting = new ArrayList<>();

    public static void createSocket() {
        try {
            serverId = randomString(Random.Default.nextInt(3, 16));
            String hash = hash("Hysentials_" + serverId);

            Minecraft.getMinecraft().getSessionService().joinServer(
                Minecraft.getMinecraft().getSession().getProfile(),
                Minecraft.getMinecraft().getSession().getToken(),
                hash
            );

//            WebSocketClient ws = new WebSocketClient(new URI("ws://localhost:8443")) {
            WebSocketClient ws = new WebSocketClient(new URI("ws://5.161.201.11:8443")) {
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
                    if (json.has("method")) {
                        switch (json.getString("method")) {
                            case "login": {
                                if (json.has("success") && json.getBoolean("success")) {
                                    MUtils.chat(HysentialsConfig.chatPrefix + " §aLogged in successfully!");
                                    CLIENT = this;
                                    Multithreading.runAsync(BlockWAPIUtils::getOnline);
                                }
                                if (!json.getBoolean("linked")) {
                                    Socket.linked = false;
                                    MUtils.chat(HysentialsConfig.chatPrefix + " §cYou are not linked to a discord account! Some features will not work.");
                                } else {
                                    Socket.linked = true;
                                }
                                break;
                            }
                            case "data": {
                                cachedData = json.getJSONObject("data");
                                cachedServerData = json.getJSONObject("server");
                                break;
                            }
                            case "chat": {
                                if (HysentialsConfig.globalChatEnabled) {
                                    if (json.getString("username").equals("HYPIXELCONSOLE") && !json.has("uuid")) {
                                        MUtils.chat(HysentialsConfig.chatPrefix + " §c" + json.getString("message"));
                                        break;
                                    }
                                    MUtils.chat(":globalchat: "
                                        + "&6" + json.getString("username")
                                        + "<#fff1d4>: "
                                        + json.getString("message"));
                                }
                                break;
                            }

                            case "link": {
                                MUtils.chat(HysentialsConfig.chatPrefix + " §fA link request has been made, please type §6`/hysentials link` §fto link your account. §fThis will expire in 5 minutes. If this was not you, please ignore this!");
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

                            case "clubInvite": {
                                JSONObject club = json.getJSONObject("club");
                                MUtils.chat("&b-----------------------------------------------------");
                                new UTextComponent("§eYou have been invited to join the §6" + club.getString("name") + " §eclub. Type §6`/club join " + club.getString("name") + "` §eto join!")
                                    .setHover(HoverEvent.Action.SHOW_TEXT, "§eClick to join!")
                                    .setClick(ClickEvent.Action.RUN_COMMAND, "/club join " + club.getString("name"))
                                    .chat();
                                MUtils.chat(HysentialsConfig.chatPrefix + " §eThis invite will expire in 5 minutes.");
                                MUtils.chat("&b-----------------------------------------------------");
                                break;
                            }

                            case "clubAccept": {
                                if (json.getBoolean("success")) {
                                    MUtils.chat(HysentialsConfig.chatPrefix + " §aSuccessfully joined club!");
                                } else {
                                    MUtils.chat(HysentialsConfig.chatPrefix + " §cFailed to join club!");
                                }
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
                    MUtils.chat(HysentialsConfig.chatPrefix + " §cDisconnected from websocket server. Attempting to reconnect in 5 seconds");
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

    private static String randomString(int size) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int random = (int) (Math.random() * chars.length());
            sb.append(chars.charAt(random));
        }
        return sb.toString();
    }

    public static String hash(String str) {
        try {
            byte[] digest = digest(str, "SHA-1");
            return new BigInteger(digest).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] digest(String str, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
        return md.digest(strBytes);
    }
}
