package cc.woverflow.hysentials.capes;

import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.cosmetic.CosmeticGui;
import cc.woverflow.hysentials.schema.HysentialsSchema;
import cc.woverflow.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

public class CapeHandler {
    public static HashMap<UUID, Integer> tickMap = new HashMap<>();
    public static HashMap<UUID, DynamicTexture> textureMap = new HashMap<>();
    public static HashMap<UUID, String> resourceMap = new HashMap<>();
    @SubscribeEvent
    public void onTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        try {
            for (String id : Socket.cachedUsersNew.keySet()) {
                UUID uuid = UUID.fromString(id);
                boolean wearingCape = false;
                for (HysentialsSchema.Cosmetic cosmetic : CosmeticGui.Companion.getEquippedCosmetics(uuid)) {
                    if (cosmetic.getSubType() != null && cosmetic.getSubType().equals("cape")) {
                        wearingCape = true;
                        String name = cosmetic.getName();
                        if (CosmeticGui.Companion.hasCosmetic(uuid, name)) {
                            ResourceLocation location = new ResourceLocation(cosmetic.getResource());
                            resourceMap.put(uuid, cosmetic.getResource());
                            try {
                                InputStream stream = Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(location);
                                if (stream == null) {
                                    throw new RuntimeException("ImageIcon " + name + " does not exist in the resource pack!");
                                }
                                BufferedImage image = javax.imageio.ImageIO.read(stream);
                                int frames = image.getHeight() / 32;
                                int tick = tickMap.getOrDefault(uuid, 0);
                                int timePerTick = 20 / (cosmetic.getFramerate() != null ? cosmetic.getFramerate() : 4);
                                if (++tick >= frames * timePerTick) {
                                    tick = 0;
                                }
                                int frame = tick / timePerTick;
                                if (frame >= frames) {
                                    frame = 0;
                                }
                                tickMap.put(uuid, tick);
                                BufferedImage frameImage = image.getSubimage(0, frame * 32, 64, 32);
                                DynamicTexture texture = new DynamicTexture(frameImage);
                                textureMap.put(uuid, texture);
                                stream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (!wearingCape) {
                    tickMap.remove(uuid);
                    textureMap.remove(uuid);
                    resourceMap.remove(uuid);
                }
            }
        } catch (Exception e) {
            //Most likely a concurrent modification exception
        }
    }
}
