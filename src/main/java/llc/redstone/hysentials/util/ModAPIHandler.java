package llc.redstone.hysentials.util;

import io.netty.buffer.Unpooled;
import net.hypixel.modapi.handler.ClientboundPacketHandler;
import net.hypixel.modapi.packet.HypixelPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPartyInfoPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPingPacket;
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPlayerInfoPacket;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.hypixel.modapi.serializer.PacketSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

import java.util.HashMap;
import java.util.function.Consumer;

public class ModAPIHandler implements ClientboundPacketHandler {
    private static HashMap<String, Consumer<HypixelPacket>> packetAwaiters = new HashMap<>();

    public static boolean sendPacket(HypixelPacket packet) {
        NetHandlerPlayClient netHandler = Minecraft.getMinecraft().getNetHandler();
        if (netHandler == null) {
            return false;
        }

        PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
        PacketSerializer serializer = new PacketSerializer(buf);
        packet.write(serializer);
        netHandler.addToSendQueue(new C17PacketCustomPayload(packet.getIdentifier(), buf));
        return true;
    }

    public static void sendPacket(HypixelPacket serverBound, Consumer<HypixelPacket> consumer) {
        packetAwaiters.put(serverBound.getIdentifier(), consumer);
        sendPacket(serverBound);
    }

    @Override
    public void onLocationEvent(ClientboundLocationPacket packet) {
        LocrawUtil.INSTANCE.update(packet);

        if (packetAwaiters.containsKey(packet.getIdentifier())) {
            packetAwaiters.get(packet.getIdentifier()).accept(packet);
            packetAwaiters.remove(packet.getIdentifier());
        }
    }

    @Override
    public void onPartyInfoPacket(ClientboundPartyInfoPacket packet) {
        if (packetAwaiters.containsKey(packet.getIdentifier())) {
            packetAwaiters.get(packet.getIdentifier()).accept(packet);
            packetAwaiters.remove(packet.getIdentifier());
        }
    }

    @Override
    public void onPingPacket(ClientboundPingPacket packet) {
        if (packetAwaiters.containsKey(packet.getIdentifier())) {
            packetAwaiters.get(packet.getIdentifier()).accept(packet);
            packetAwaiters.remove(packet.getIdentifier());
        }
    }

    @Override
    public void onPlayerInfoPacket(ClientboundPlayerInfoPacket packet) {
        if (packetAwaiters.containsKey(packet.getIdentifier())) {
            packetAwaiters.get(packet.getIdentifier()).accept(packet);
            packetAwaiters.remove(packet.getIdentifier());
        }
    }
}
