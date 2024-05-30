package llc.redstone.hysentials.cosmetics.wings.tdarth;

import llc.redstone.hysentials.cosmetic.CosmeticManager;
import llc.redstone.hysentials.cosmetics.Cosmetic;
import llc.redstone.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.UUID;

public class TdarthCosmetic implements Cosmetic {
    ResourceLocation texture;
    TdarthModel model = new TdarthModel();
    public static HashMap<UUID, DynamicTexture> textureMap = new HashMap<>();
    public static HashMap<UUID, Integer> tickMap = new HashMap<>();
    public TdarthCosmetic() {
        texture = new ResourceLocation("hysentials:wings/tdarth.png");
    }

    @SubscribeEvent
    public void onTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        try {
            for (String id : Socket.cachedUsers.keySet()) {
                UUID uuid = UUID.fromString(id);
                if (!canUse(uuid)) continue;
                try {
                    InputStream stream = Minecraft.getMinecraft().mcDefaultResourcePack.getInputStream(texture);
                    if (stream == null) {
                        throw new RuntimeException("Tdarth wings does not exist in the resource pack!");
                    }
                    BufferedImage image = javax.imageio.ImageIO.read(stream);
                    int frames = image.getHeight() / 64;
                    int tick = tickMap.getOrDefault(uuid, 0);
                    int timePerTick = 20 / 2;
                    if (++tick >= frames * timePerTick) {
                        tick = 0;
                    }
                    int frame = tick / timePerTick;
                    if (frame >= frames) {
                        frame = 0;
                    }
                    tickMap.put(uuid, tick);
                    BufferedImage frameImage = image.getSubimage(0, frame * 64, 64, 64);
                    DynamicTexture texture = new DynamicTexture(frameImage);
                    textureMap.put(uuid, texture);
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ignored) {}
    }
    public boolean canUse(UUID uuid) {
        return CosmeticManager.equippedCosmetic(uuid, "tdarth")
            && (CosmeticManager.hasCosmetic(uuid, "tdarth") || CosmeticManager.isPreviewing(uuid, "tdarth"));
    }

    @Override
    public boolean canUse(EntityPlayer player) {
        return canUse(player.getUniqueID());
    }

    @Override
    public ModelBase getModel() {
        return model;
    }

    @Override
    public ResourceLocation getTexture() {
        return texture;
    }

    @Override
    public String getName() {
        return "tdarth";
    }
}
