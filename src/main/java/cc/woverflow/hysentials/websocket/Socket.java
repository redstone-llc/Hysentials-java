package cc.woverflow.hysentials.websocket;

import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.HysentialsUtilsKt;
import cc.woverflow.hysentials.config.hysentialMods.ChatConfig;
import cc.woverflow.hysentials.config.hysentialMods.FormattingConfig;
import cc.woverflow.hysentials.guis.misc.HysentialsLevel;
import cc.woverflow.hysentials.handlers.chat.modules.bwranks.BWSReplace;
import cc.woverflow.hysentials.schema.HysentialsSchema;
import cc.woverflow.hysentials.util.*;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.woverflow.hysentials.config.HysentialsConfig;
import cc.woverflow.hysentials.handlers.groupchats.GroupChat;
import cc.woverflow.hysentials.websocket.methods.DoorbellAuthenticate;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.neovisionaries.ws.client.*;
import kotlin.random.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.IChatComponent;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static cc.woverflow.hysentials.guis.actionLibrary.ActionViewer.toList;

public class Socket {
    public static List<WebSocket> sockets = new ArrayList<>();
    public static WebSocket CLIENT;

    //Client's authenticated data
    public static HysentialsSchema.AuthUser user;
    //Client's player data (levels, ranks, etc)
    public static HysentialsSchema.User cachedUser = null;
    //All online hysentials users
    public static List<JSONObject> cachedUsers = new ArrayList<>();
    public static HashMap<String, HysentialsSchema.User> cachedUsersNew = new HashMap<>();
    public static JSONObject cachedRewards = new JSONObject();
    public static JSONObject cachedServerData = new JSONObject();
    public static String serverId;
    public static boolean linking = false;
    public static boolean linked = false;
    public static JSONObject data = null;
    public static boolean banned = false;
    public static String banReason = "";
    public static List<DuoVariable<String, Consumer<JSONObject>>> awaiting = new ArrayList<>();

    public static int relogAttempts = 0;

    public static boolean manualDisconnect = false;

    public static void init() {
        new DoorbellAuthenticate();
    }

    public static void createSocket() {
        if (relogAttempts > 2) {
            MUtils.chat("&cAn error occurred whilst connecting to the Hysentials websocket. Please contact @sinender on Discord if this issue persists.");
            return;
        }
        try {
            serverId = randomString(Random.Default.nextInt(3, 16));
            String hash = hash("Hysentialss_" + serverId);

            Minecraft.getMinecraft().getSessionService().joinServer(
                Minecraft.getMinecraft().getSession().getProfile(),
                Minecraft.getMinecraft().getSession().getToken(),
                hash
            );
            Gson gson = new Gson();
            WebSocketFactory factory = new WebSocketFactory();
            SSLStore store = new SSLStore();
            store.load("/ssl/socket.der");
            SSLContext context = store.finish();
            factory.setSSLContext(context);
            factory.setServerName("socket.redstone.llc");
            factory.getProxySettings().setSocketFactory(context.getSocketFactory());
            factory.getProxySettings().setServerName("socket.redstone.llc");
            factory.getProxySettings().setPort(443);
            factory.getProxySettings().setSSLContext(context);
            WebSocket socket = factory.createSocket(HysentialsUtilsKt.getWEBSOCKET());

            socket.addListener(new WebSocketListener() {
                public void send(String message) {
                    socket.sendText(message);
                }

                @Override
                public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {

                }

                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    System.out.println("Connected to websocket server");
                    JSONObject obj = new JSONObject();
                    obj.put("method", "login");
                    obj.put("username", Minecraft.getMinecraft().getSession().getUsername());
                    obj.put("key", serverId);
                    send(obj.toString());
                }

                @Override
                public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {

                }

                @Override
                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                    if (manualDisconnect) {
                        manualDisconnect = false;
                        relogAttempts = 0;
                        return;
                    }

                    linking = false;
                    data = null;
                    relogAttempts++;
                    if (relogAttempts > 2) {
                        MUtils.chat(HysentialsConfig.chatPrefix + " §cFailed to connect to websocket server. This is probably because it is offline. Please try again later with `/hs reconnect`.");
                        return;
                    }
                    MUtils.chat(HysentialsConfig.chatPrefix + " §cDisconnected from websocket server. Attempting to reconnect in 20 seconds");
                    Multithreading.schedule(Socket::createSocket, 20, TimeUnit.SECONDS);
                }

                @Override
                public void onFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onContinuationFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onBinaryFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onCloseFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onPingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                    websocket.sendPong();
                }

                @Override
                public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                }

                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {
                    JSONObject json = new JSONObject(text);
                    if (json.has("method")) {
                        switch (json.getString("method")) {
                            case "login": {
                                relogAttempts = 0;
                                if (json.has("success") && !json.getBoolean("success")) {
                                    banned = true;
                                    banReason = json.getString("status");
                                    relogAttempts = 3;
                                }

                                if (json.has("success") && json.getBoolean("success")) {
                                    MUtils.chat(HysentialsConfig.chatPrefix + " §aLogged in successfully!");
                                    CLIENT = websocket;
                                    sockets.add(websocket);
                                    if (sockets.size() > 1) {
                                        for (int i = 0; i < sockets.size(); i++) {
                                            WebSocket socket = sockets.get(i);
                                            if (i != sockets.size() - 1) {
                                                socket.disconnect();
                                                sockets.remove(socket);
                                            }
                                        }
                                    }
                                    Multithreading.runAsync(() -> {
                                        BlockWAPIUtils.getOnline();
                                        String levelRewards = NetworkUtils.getString(HysentialsUtilsKt.getHYSENTIALS_API() + "/rewards");
                                        if (levelRewards != null) {
                                            JSONObject rewards = new JSONObject(levelRewards);
                                            if (rewards.has("rewards")) {
                                                cachedRewards = rewards.getJSONObject("rewards");
                                            }
                                        }
                                    });
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
//                                cachedData = json.getJSONObject("data");
                                JsonParser jsonParser = new JsonParser();
                                cachedUser = HysentialsSchema.User.Companion.deserialize(jsonParser.parse(json.getJSONObject("data").toString()).getAsJsonObject());
                                cachedServerData = json.getJSONObject("server");
                                List<HysentialsSchema.User> users = new ArrayList<>();
                                for (Object o : toList(json.getJSONArray("users"))) {
                                    cachedUsersNew.put(((JSONObject) o).getString("uuid"), HysentialsSchema.User.Companion.deserialize(jsonParser.parse(o.toString()).getAsJsonObject()));
                                }

                                cachedUsers = new ArrayList<>();
                                for (Object o : toList(json.getJSONArray("users"))) {
                                    cachedUsers.add((JSONObject) o);
                                }
                                break;
                            }
                            case "chat": {
                                if (ChatConfig.globalChat && Hysentials.INSTANCE.getConfig().chatConfig.enabled) {
                                    if (json.getString("username").equals("HYPIXELCONSOLE") && !json.has("uuid")) {
                                        MUtils.chat(HysentialsConfig.chatPrefix + " §c" + json.getString("message"));
                                        break;
                                    }

                                    BlockWAPIUtils.Rank rank = BlockWAPIUtils.getRank(json.getString("uuid"));
                                    String username = json.getString("username");
                                    if (!ChatConfig.globalChatSuffix) {
                                        username = username.split(" ")[0];
                                    }
                                    String hex = "&6";
                                    if (FormattingConfig.fancyRendering()) {
                                        hex = "<#fff1d4>";
                                    }
                                    IChatComponent comp = new UTextComponent(ChatConfig.globalPrefix)
                                        .appendSibling(
                                            new UTextComponent(
                                                "&6" + username
                                            ).setHover(HoverEvent.Action.SHOW_TEXT, (rank.getPrefixCheck() + BlockWAPIUtils.getUsername(UUID.fromString(json.getString("uuid")))))
                                        )
                                        .appendSibling(
                                            new UTextComponent(
                                                hex + ": " + json.getString("message")
                                            )
                                        );
                                    Minecraft.getMinecraft().thePlayer.addChatMessage(comp);
                                }
                                return;
                            }

                            case "link": {
                                MUtils.chat(HysentialsConfig.chatPrefix + " §fA link request has been made, please type §6`/hysentials link` §fto link your account. §fThis will expire in 5 minutes. If this was not you, please ignore this!");
                                linking = true;
                                data = json;

                                Multithreading.schedule(() -> {
                                    linking = false;
                                    data = null;
                                }, 5, TimeUnit.MINUTES);
                                break;
                            }

                            case "diagnose": {
                                JSONObject data = new JSONObject().put("ram", Runtime.getRuntime().maxMemory() / 1024 / 1024).put("cpu", Runtime.getRuntime().availableProcessors()).put("diagnoses", BWSReplace.diagnostics);
                                json.put("data", data);
                                send(json.toString());
                                break;
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

                            case "message": {
                                if (json.has("type")) {
                                    switch (json.getString("type")) {
                                        case "level": {
                                            HysentialsLevel.checkLevel(json);
                                            break;
                                        }
                                    }
                                } else if (json.has("message")) {
                                    MUtils.chat(json.getString("message"));
                                }
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

                    for (Channel channel : Channel.Companion.getChannels().values()) {
                        if (channel.getName().equals(json.getString("method"))) {
                            channel.onReceive(new JsonParser().parse(json.toString()).getAsJsonObject());
                        }
                    }
                }

                @Override
                public void onTextMessage(WebSocket websocket, byte[] data) throws Exception {

                }

                @Override
                public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {

                }

                @Override
                public void onSendingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onFrameUnsent(WebSocket websocket, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onThreadCreated(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {

                }

                @Override
                public void onThreadStarted(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {

                }

                @Override
                public void onThreadStopping(WebSocket websocket, ThreadType threadType, Thread thread) throws Exception {

                }

                @Override
                public void onError(WebSocket websocket, WebSocketException cause) throws Exception {

                }

                @Override
                public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception {

                }

                @Override
                public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) throws Exception {

                }

                @Override
                public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {

                }

                @Override
                public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {

                }

                @Override
                public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {

                }

                @Override
                public void onSendingHandshake(WebSocket websocket, String requestLine, List<String[]> headers) throws Exception {

                }
            });

            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
            relogAttempts++;
            createSocket();
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
