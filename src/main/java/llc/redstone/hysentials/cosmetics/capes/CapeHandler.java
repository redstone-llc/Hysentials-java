package llc.redstone.hysentials.cosmetics.capes;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import llc.redstone.hysentials.config.hysentialmods.CosmeticConfig;
import llc.redstone.hysentials.cosmetic.CosmeticManager;
import llc.redstone.hysentials.schema.HysentialsSchema;
import llc.redstone.hysentials.websocket.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CapeHandler {
    public static HashMap<UUID, Integer> tickMap = new HashMap<>();
    public static HashMap<String, ResourceLocation> resourceList = new HashMap<>();

    private Field capeLocationField;
    public CapeHandler() {
        try {
            capeLocationField = ReflectionHelper.findField(NetworkPlayerInfo.class, "locationCape", "field_178862_f");
            capeLocationField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @SubscribeEvent
    public void onTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (CosmeticConfig.disableCustomCapes) return;
        try {
            for (String id : Socket.cachedUsers.keySet()) {
                UUID uuid = UUID.fromString(id);
                boolean wearingCape = false;
                for (HysentialsSchema.Cosmetic cosmetic : CosmeticManager.getEquippedCosmetics(uuid)) {
                    if (cosmetic.getSubType() != null && cosmetic.getSubType().equals("cape")) {
                        wearingCape = true;
                        String name = cosmetic.getName();
                        if (CosmeticManager.hasCosmetic(uuid, name) || CosmeticManager.isPreviewing(uuid, name)) {
                            ResourceLocation location = new ResourceLocation(cosmetic.getResource());
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
                                String frameString = (frames > 1 ? "_" + frame : "");
                                if (!resourceList.containsKey(name + frameString)) {
                                    resourceList.put(name + frameString, Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation(name + frameString, texture));
                                }
                                ResourceLocation capeResource = resourceList.get(name + frameString);
                                if (Minecraft.getMinecraft().getNetHandler() == null || capeResource == null) return;

                                NetworkPlayerInfo info = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(uuid);
                                if (info != null) {
                                    ResourceLocation loc = (ResourceLocation) capeLocationField.get(info);
                                    capeLocationField.set(info, capeResource);
                                }

                                stream.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (!wearingCape) {
                    NetworkPlayerInfo info = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(uuid);
                    if (tickMap.containsKey(uuid)) {
                        tickMap.remove(uuid);
                        if (info == null) return;
                        Minecraft.getMinecraft().getSkinManager().loadProfileTextures(info.getGameProfile(), new SkinManager.SkinAvailableCallback() {
                            public void skinAvailable(MinecraftProfileTexture.Type typeIn, ResourceLocation location, MinecraftProfileTexture profileTexture) {
                                if (typeIn == MinecraftProfileTexture.Type.CAPE) {
                                    try {
                                        capeLocationField.set(info, location);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, true);
                    }
                }
            }
        } catch (Exception e) {
            //Most likely a concurrent modification exception
        }
    }
}
