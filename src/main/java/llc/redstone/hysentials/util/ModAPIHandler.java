package llc.redstone.hysentials.util;

import io.netty.buffer.Unpooled;
import net.hypixel.modapi.handler.ClientboundPacketHandler;
import net.hypixel.modapi.packet.HypixelPacket;
import net.hypixel.modapi.serializer.PacketSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class ModAPIHandler implements ClientboundPacketHandler {
    HashMap<String, Consumer<HypixelPacket>> packetAwaiters = new HashMap<>();
    @Override
    public void handle(HypixelPacket packet) {
        packetAwaiters.forEach((identifier, consumer) -> {
            if (packet.getIdentifier().equals(identifier)) {
                consumer.accept(packet);
            }
        });
    }

    public void sendPacket(Function<PacketSerializer, HypixelPacket> packet, PacketBuffer buffer) {
        PacketBuffer defaultPacketBuffer = new PacketBuffer(Unpooled.buffer(1).writeByte(1));
        HypixelPacket contains = packet.apply(new PacketSerializer(defaultPacketBuffer));
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(
            new C17PacketCustomPayload(
                contains.getIdentifier(),
                (buffer == null ? new PacketBuffer(
                    defaultPacketBuffer
                ) : buffer)
            )
        );
    }

    public void sendPacket(Function<PacketSerializer, HypixelPacket> packet) {
        sendPacket(packet, (PacketBuffer) null);
    }

    public void sendPacket(Function<PacketSerializer, HypixelPacket> packet, Consumer<HypixelPacket> awaiter) {
        PacketBuffer defaultPacketBuffer = new PacketBuffer(Unpooled.buffer(1).writeByte(1));
        HypixelPacket contains = packet.apply(new PacketSerializer(defaultPacketBuffer));
        packetAwaiters.put(contains.getIdentifier(), awaiter);
        sendPacket(packet);
    }
}
