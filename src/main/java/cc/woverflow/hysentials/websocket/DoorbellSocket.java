package cc.woverflow.hysentials.websocket;

import cc.woverflow.hysentials.HysentialsUtilsKt;
import cc.woverflow.hysentials.util.SSLStore;
import com.neovisionaries.ws.client.*;
import net.minecraft.client.Minecraft;

import javax.net.ssl.SSLContext;
import java.util.List;
import java.util.Map;

public class DoorbellSocket {
    public static WebSocket CLIENT;

    public static void createSocket(String token) {
        try {
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
                @Override
                public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {

                }

                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    System.out.println("Connected to doorbell websocket server");
                    socket.sendText(
                        new Request(
                            "method", "login",
                            "token", token
                        ).toString());
                }

                @Override
                public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {

                }

                @Override
                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {

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

                }

                @Override
                public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {

                }

                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {

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

                public void send(String message) {
                    socket.sendText(message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
