package llc.redstone.hysentials.handlers.misc;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
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
        event.manager.channel().pipeline().addBefore("packet_handler", "hysentials_mod_api_packet_handler", HypixelPacketHandler.INSTANCE);
    }

    public static class HypixelPacketHandler extends SimpleChannelInboundHandler<Packet<?>>{
        private static final HypixelPacketHandler INSTANCE = new HypixelPacketHandler();

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Packet<?> msg) {
            ctx.fireChannelRead(msg);

            if (!(msg instanceof S3FPacketCustomPayload)) {
                return;
            }

            S3FPacketCustomPayload packet = (S3FPacketCustomPayload) msg;
            String identifier = packet.getChannelName();
            if (!HypixelModAPI.getInstance().getRegistry().isRegistered(identifier)) {
                return;
            }

            try {
                HypixelModAPI.getInstance().handle(identifier, new PacketSerializer(packet.getBufferData()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
