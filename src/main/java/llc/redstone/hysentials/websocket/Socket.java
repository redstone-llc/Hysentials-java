package llc.redstone.hysentials.websocket;

import com.google.gson.JsonObject;
import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.HysentialsUtilsKt;
import llc.redstone.hysentials.config.hysentialMods.ChatConfig;
import llc.redstone.hysentials.config.hysentialMods.FormattingConfig;
import llc.redstone.hysentials.guis.misc.HysentialsLevel;
import llc.redstone.hysentials.handlers.chat.modules.bwranks.BWSReplace;
import llc.redstone.hysentials.schema.HysentialsSchema;
import llc.redstone.hysentials.util.*;
import cc.polyfrost.oneconfig.libs.universal.wrappers.message.UTextComponent;
import cc.polyfrost.oneconfig.utils.Multithreading;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.websocket.methods.*;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.neovisionaries.ws.client.*;
import kotlin.random.Random;
import llc.redstone.hysentials.websocket.methods.club.ClubAccept;
import llc.redstone.hysentials.websocket.methods.club.ClubInvite;
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
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static llc.redstone.hysentials.guis.actionLibrary.ActionViewer.toList;

public class Socket {
    public static List<WebSocket> sockets = new ArrayList<>(); //This shouldn't be needed, but can be kept as a precaution
    public static WebSocket CLIENT;
    public static String serverId;


    //Client's authenticated data
    public static HysentialsSchema.AuthUser user;
    //Client's player data (levels, ranks, etc)
    public static HysentialsSchema.User cachedUser = null;
    //All online hysentials users player data may be costly moving forward (probably a good idea to remove unnecessary data)
    public static HashMap<String, HysentialsSchema.User> cachedUsers = new HashMap<>();
    public static List<HysentialsSchema.Group> cachedGroups = new ArrayList<>();
    public static JSONObject cachedRewards = new JSONObject();
    public static HysentialsSchema.ServerData cachedServerData = null;

    public static boolean linking = false;
    public static boolean linked = false;
    public static JsonObject linkingData = null; //Could probably move to its own class and stuff

    public static boolean banned = false;
    public static String banReason = "";

    // This is a list of awaiting responses from the websocket server
    public static List<DuoVariable<String, Consumer<JsonObject>>> awaiting = new ArrayList<>();

    public static int relogAttempts = 0;

    public static boolean manualDisconnect = false; // Not sure if this is needed
    public static ScheduledFuture<?> future;

    public static void init() {
        new ClubAccept();
        new ClubInvite();
        new Chat();
        new DoorbellAuthenticate();
        new Group();
        new LinkRequest();
        new Login();
        new Message();
        new UpdateCache();
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

            socket.addListener(new HysentialsListener());

            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
            relogAttempts++;
            if (relogAttempts > 2) {
                MUtils.chat(HysentialsConfig.chatPrefix + " §cFailed to connect to websocket server. This is probably because it is offline. Please try again later with `/hs reconnect`.");
                return;
            }
            MUtils.chat(HysentialsConfig.chatPrefix + " §cDisconnected from websocket server. Attempting to reconnect in 20 seconds");
            future = Multithreading.submitScheduled(Socket::createSocket, 20, TimeUnit.SECONDS);
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
