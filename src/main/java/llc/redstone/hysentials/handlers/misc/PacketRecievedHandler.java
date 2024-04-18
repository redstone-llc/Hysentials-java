package llc.redstone.hysentials.handlers.misc;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import llc.redstone.hysentials.event.CancellableEvent;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.serializer.PacketSerializer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

public class PacketRecievedHandler {
    @SubscribeEvent
    public void onNetworkEvent(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        event.manager.channel().pipeline()
            .addAfter("fml:packet_handler", "HS_packet_handler", new ChannelDuplexHandler() {
                @Override
                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                    CancellableEvent packetReceivedEvent = new CancellableEvent();

                    if (msg instanceof S3FPacketCustomPayload) {
                        S3FPacketCustomPayload packet = (S3FPacketCustomPayload) msg;

                        HypixelModAPI.getInstance().handle(
                            packet.getChannelName(),
                            new PacketSerializer(packet.getBufferData())
                        );
                    }

                    if (!packetReceivedEvent.isCancelled())
                        ctx.fireChannelRead(msg);
                }

                @Override
                public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                    CancellableEvent packetSentEvent = new CancellableEvent();

                    if (msg instanceof Packet) {

                    }

                    if (!packetSentEvent.isCancelled())
                        ctx.write(msg, promise);
                }
            });
    }
}
