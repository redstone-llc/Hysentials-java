package cc.woverflow.hysentials.handlers.npc;

import cc.woverflow.hysentials.handlers.npc.lost.LostRidable;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class NPC {
    public static List<NPC> npcs = new ArrayList<>();
    public GameProfile profile;
    public boolean isAlive = false;
    public String textureLocation;
    public boolean shouldFollowPlayer = false;
    public EntityOtherPlayerMP entity;

    public List<String> hologram = new ArrayList<>();
    public List<EntityArmorStand> hologramEntities = new ArrayList<>();
    public NPC(GameProfile gameProfile, String textureLocation) {
        this.profile = gameProfile;
        this.textureLocation = textureLocation;
        this.shouldFollowPlayer = false;

        npcs.add(this);
    }

    public NPC(GameProfile gameProfile, String textureLocation, boolean shouldFollowPlayer) {
        this.profile = gameProfile;
        this.textureLocation = textureLocation;
        this.shouldFollowPlayer = shouldFollowPlayer;

        npcs.add(this);
    }

    public void onWorldLoad() {

    }

    public void onMessageRecieve(ClientChatReceivedEvent event) {

    }

    public abstract void onInteract(PlayerInteractEvent event, MovingObjectPosition obj);

    public void spawnNPC(int x, int y, int z) {
        try {
            isAlive = true;
            entity = new EntityOtherPlayerMP(Minecraft.getMinecraft().theWorld, profile);
            entity.setPosition(x + 0.5, y - 2, z + 0.5);
            entity.setAlwaysRenderNameTag(false);
            Minecraft.getMinecraft().theWorld.addEntityToWorld(entity.getEntityId(), entity);
            NetworkPlayerInfo info = new NetworkPlayerInfo(profile);
            ResourceLocation skin = new ResourceLocation(textureLocation);
            Field locationSkin = NetworkPlayerInfo.class.getDeclaredField("field_178865_e");
            locationSkin.setAccessible(true);
            locationSkin.set(info, skin);
            Field playerInfo = AbstractClientPlayer.class.getDeclaredField("field_175157_a");
            playerInfo.setAccessible(true);
            playerInfo.set(entity, info);

            for (int i = 0; i < hologram.size(); i++) {
                String holo = hologram.get(i);
                EntityArmorStand stand = new EntityArmorStand(Minecraft.getMinecraft().theWorld, x + 0.5, y - 2 + (i + 1)*0.2, z + 0.5);
                stand.setCustomNameTag(holo);
                stand.setAlwaysRenderNameTag(true);
                stand.setInvisible(true);
                stand.noClip = true;
                Minecraft.getMinecraft().theWorld.addEntityToWorld(stand.getEntityId(), stand);
                hologramEntities.add(stand);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setName(String name) {
        try {
            entity.setCustomNameTag(name);
            GameProfile profile = new GameProfile(UUID.randomUUID(), name);
            Field gameProfile = EntityPlayer.class.getDeclaredField("field_146106_i");
            gameProfile.setAccessible(true);
            gameProfile.set(entity, profile);
            NetworkPlayerInfo info = new NetworkPlayerInfo(profile);
            ResourceLocation skin = new ResourceLocation(textureLocation);
            Field locationSkin = NetworkPlayerInfo.class.getDeclaredField("field_178865_e");
            locationSkin.setAccessible(true);
            locationSkin.set(info, skin);
            Field playerInfo = AbstractClientPlayer.class.getDeclaredField("field_175157_a");
            playerInfo.setAccessible(true);
            playerInfo.set(entity, info);
            entity.refreshDisplayName();
        } catch (Exception e) {
        }
    }

    public void onWorldRender(RenderWorldLastEvent event) {
        if (entity == null) return;
        for (int i = 0; i < hologramEntities.size(); i++) {
            EntityArmorStand stand = hologramEntities.get(i);
            stand.posX = entity.posX;
            stand.posY = entity.posY + (i + 1)*0.2;
            stand.posZ = entity.posZ;
        }
    }
}
