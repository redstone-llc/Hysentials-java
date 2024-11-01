package llc.redstone.hysentials.handlers.npc;

import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import kotlin.jvm.internal.ReflectionFactory;
import llc.redstone.hysentials.handlers.npc.lost.LostRidable;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
            Field locationSkin = ReflectionHelper.findField(NetworkPlayerInfo.class, "field_178865_e", "locationSkin");
            locationSkin.setAccessible(true);
            locationSkin.set(info, skin);
            Field playerInfo = ReflectionHelper.findField(AbstractClientPlayer.class, "field_175157_a", "playerInfo");
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
            Field gameProfile = ReflectionHelper.findField(EntityPlayer.class, "field_146106_i", "gameProfile");
            gameProfile.setAccessible(true);
            gameProfile.set(entity, profile);
            NetworkPlayerInfo info = new NetworkPlayerInfo(profile);
            ResourceLocation skin = new ResourceLocation(textureLocation);
            Field locationSkin = ReflectionHelper.findField(NetworkPlayerInfo.class, "field_178865_e", "locationSkin");
            locationSkin.setAccessible(true);
            locationSkin.set(info, skin);
            Field playerInfo = ReflectionHelper.findField(AbstractClientPlayer.class, "field_175157_a", "playerInfo");
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

    public static MovingObjectPosition getMouseOverExtended(float dist)
    {
        Minecraft mc = FMLClientHandler.instance().getClient();
        Entity theRenderViewEntity = mc.getRenderViewEntity();
        AxisAlignedBB theViewBoundingBox = new AxisAlignedBB(
            theRenderViewEntity.posX-0.5D,
            theRenderViewEntity.posY-0.0D,
            theRenderViewEntity.posZ-0.5D,
            theRenderViewEntity.posX+0.5D,
            theRenderViewEntity.posY+1.5D,
            theRenderViewEntity.posZ+0.5D
        );
        MovingObjectPosition returnMOP = null;
        if (mc.theWorld != null)
        {
            double var2 = dist;
            returnMOP = theRenderViewEntity.rayTrace(var2, 0);
            double calcdist = var2;
            Vec3 pos = theRenderViewEntity.getPositionEyes(0);
            var2 = calcdist;
            if (returnMOP != null)
            {
                calcdist = returnMOP.hitVec.distanceTo(pos);
            }

            Vec3 lookvec = theRenderViewEntity.getLook(0);
            Vec3 var8 = pos.addVector(lookvec.xCoord * var2,
                lookvec.yCoord * var2,
                lookvec.zCoord * var2);
            Entity pointedEntity = null;
            float var9 = 1.0F;
            @SuppressWarnings("unchecked")
            List<Entity> list = mc.theWorld.getEntitiesWithinAABBExcludingEntity(
                theRenderViewEntity,
                theViewBoundingBox.addCoord(
                    lookvec.xCoord * var2,
                    lookvec.yCoord * var2,
                    lookvec.zCoord * var2).expand(var9, var9, var9));
            double d = calcdist;

            for (Entity entity : list)
            {
                if (entity.canBeCollidedWith())
                {
                    float bordersize = entity.getCollisionBorderSize();
                    AxisAlignedBB aabb = new AxisAlignedBB(
                        entity.posX-entity.width/2,
                        entity.posY,
                        entity.posZ-entity.width/2,
                        entity.posX+entity.width/2,
                        entity.posY+entity.height,
                        entity.posZ+entity.width/2);
                    aabb.expand(bordersize, bordersize, bordersize);
                    MovingObjectPosition mop0 = aabb.calculateIntercept(pos, var8);

                    if (aabb.isVecInside(pos))
                    {
                        if (0.0D < d || d == 0.0D)
                        {
                            pointedEntity = entity;
                            d = 0.0D;
                        }
                    } else if (mop0 != null)
                    {
                        double d1 = pos.distanceTo(mop0.hitVec);

                        if (d1 < d || d == 0.0D)
                        {
                            pointedEntity = entity;
                            d = d1;
                        }
                    }
                }
            }

            if (pointedEntity != null && (d < calcdist || returnMOP == null))
            {
                returnMOP = new MovingObjectPosition(pointedEntity);
            }
        }
        return returnMOP;
    }

    public void LookAt(double px, double py, double pz , EntityPlayer me)
    {
        double dirx = me.posX - px;
        double diry = me.posY - py;
        double dirz = me.posZ - pz;

        double len = Math.sqrt(dirx*dirx + diry*diry + dirz*dirz);

        dirx /= len;
        diry /= len;
        dirz /= len;

        double pitch = Math.asin(diry);
        double yaw = Math.atan2(dirz, dirx);

        //to degree
        pitch = pitch * 180.0 / Math.PI;
        yaw = yaw * 180.0 / Math.PI;

        yaw += 90f;
        me.rotationPitch = (float)pitch;
        me.rotationYaw = (float)yaw;
        me.setRotationYawHead((float)yaw);
    }

    public EntityPlayer getClosestPlayer(double x, double y, double z, double distance) {
        double d0 = -1.0;
        EntityPlayer entityplayer = null;

        for(int i = 0; i < Minecraft.getMinecraft().theWorld.playerEntities.size(); ++i) {
            EntityPlayer entityplayer1 = (EntityPlayer)Minecraft.getMinecraft().theWorld.playerEntities.get(i);
            if (EntitySelectors.NOT_SPECTATING.apply(entityplayer1) && !entityplayer1.getUniqueID().equals(this.entity.getUniqueID())) {
                double d1 = entityplayer1.getDistanceSq(x, y, z);
                if ((distance < 0.0 || d1 < distance * distance) && (d0 == -1.0 || d1 < d0)) {
                    d0 = d1;
                    entityplayer = entityplayer1;
                }
            }
        }

        return entityplayer;
    }


    public BlockPos pos = null;
    public BlockPos checkPosition(int radius) {
        World world = Minecraft.getMinecraft().theWorld;
        int centerX = UMinecraft.getPlayer().getPosition().getX();
        int centerY = Minecraft.getMinecraft().thePlayer.getPosition().getY();
        int centerZ = Minecraft.getMinecraft().thePlayer.getPosition().getZ();

        Random random = new Random();

        int randomX = centerX + random.nextInt(radius * 2 + 1) - radius;
        int randomY = centerY + random.nextInt(radius * 2 + 1) - radius;
        int randomZ = centerZ + random.nextInt(radius * 2 + 1) - radius;

        boolean block = world.getBlockState(new BlockPos(randomX, randomY, randomZ)).getBlock().isBlockSolid(world, new BlockPos(randomX, randomY, randomZ), EnumFacing.UP);
        boolean air = world.isAirBlock(new BlockPos(randomX, randomY + 1, randomZ)) && world.isAirBlock(new BlockPos(randomX, randomY + 2, randomZ)) && world.isAirBlock(new BlockPos(randomX, randomY + 3, randomZ));

        if (!block || !air || !world.canSeeSky(new BlockPos(randomX, randomY + 3, randomZ))) {
            return checkPosition(radius);
        } else {
            return new BlockPos(randomX, randomY + 3, randomZ);
        }
    }
}
