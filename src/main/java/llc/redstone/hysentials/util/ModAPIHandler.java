package llc.redstone.hysentials.util;

import io.netty.buffer.Unpooled;
import net.hypixel.modapi.handler.ClientboundPacketHandler;
import net.hypixel.modapi.packet.HypixelPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

import java.util.HashMap;
import java.util.function.Consumer;

public class ModAPIHandler implements ClientboundPacketHandler {
    public void sendPacket(HypixelPacketType packet, PacketBuffer buffer) {
        Minecraft.getMinecraft().getNetHandler().addToSendQueue(
            new C17PacketCustomPayload(
                packet.getIdentifier(),
                (buffer == null ? new PacketBuffer(
                    Unpooled.buffer(1).writeByte(1)
                ) : buffer)
            )
        );
    }
}
