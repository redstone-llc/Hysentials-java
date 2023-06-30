package cc.woverflow.hysentials.handlers.npc;

import cc.woverflow.hysentials.util.MUtils;
import cc.polyfrost.oneconfig.utils.Multithreading;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import cc.woverflow.hysentials.util.C;
import cc.woverflow.hysentials.util.ScoreboardWrapper;
import cc.woverflow.hysentials.util.WaypointUtil;
import cc.woverflow.hysentials.websocket.Request;
import cc.woverflow.hysentials.websocket.Socket;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class QuestNPC {
    public static GameProfile profile;
    public static EntityOtherPlayerMP player;
    public static boolean isSpawned = false;
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
//        Multithreading.schedule(() -> {
//            if (Minecraft.getMinecraft().theWorld != null) {
//                if ((!Socket.cachedData.has("questNPC") || !Socket.cachedData.getJSONObject("questNPC").has("x"))) {
//                    pos = checkPosition();
//                    spawnNPC();
//                } else {
//                    JSONObject questNPC = Socket.cachedData.getJSONObject("questNPC");
//                    if (questNPC.getString("title").equals(C.removeColor(ScoreboardWrapper.getTitle()))) {
//                        pos = new BlockPos(questNPC.getInt("x"), questNPC.getInt("y"), questNPC.getInt("z"));
//                        spawnNPC();
//                    }
//                }
//            }
//        }, 5, TimeUnit.SECONDS);
    }

    public void spawnNPC() {
        try {
            profile = createGameProfileWithSkin(
                "Quest NPC"
            );
            isSpawned = true;
            player = new EntityOtherPlayerMP(Minecraft.getMinecraft().theWorld, profile);
            player.setPosition(QuestNPC.pos.getX() + 0.5, QuestNPC.pos.getY() - 2, QuestNPC.pos.getZ() + 0.5);
            Minecraft.getMinecraft().theWorld.addEntityToWorld(player.getEntityId(), player);
            NetworkPlayerInfo info = new NetworkPlayerInfo(profile);
            ResourceLocation skin = new ResourceLocation("textures/npc/708d06fa5114ec9c25a1c22a054a44e6b28334c2d7ad581afd635138d3982094.png");
            Field locationSkin = NetworkPlayerInfo.class.getDeclaredField("field_178865_e");
            locationSkin.setAccessible(true);
            locationSkin.set(info, skin);
            Field playerInfo = AbstractClientPlayer.class.getDeclaredField("field_175157_a");
            playerInfo.setAccessible(true);
            playerInfo.set(player, info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        if (QuestNPC.pos != null) {
            WaypointUtil.renderWayPoint("Spawn NPC", QuestNPC.pos, event.partialTicks);
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        if (Minecraft.getMinecraft().theWorld == null) return;
        if (QuestNPC.player == null) return;
        EntityPlayer player = getClosestPlayer(QuestNPC.player.posX, QuestNPC.player.posY, QuestNPC.player.posZ, 5);
        if (player != null) {
            LookAt(player.posX, player.posY, player.posZ, QuestNPC.player);
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_AIR)) {
            MovingObjectPosition obj = getMouseOverExtended(4);
            if (obj == null) return;
            if (obj.entityHit == null || QuestNPC.player == null || obj.entityHit.getUniqueID() == null) return;
            if (obj.entityHit.getUniqueID().equals(QuestNPC.player.getUniqueID())) {
                if (!Socket.cachedData.has("questNPC")) {
                    MUtils.chat("§e[Quest NPC] &fHello adventurer! You managed to find me congratulations!");
                    Multithreading.schedule(() -> MUtils.chat("§e[Quest NPC] &fThis won't be the last time you will have to find me though."), 2, TimeUnit.SECONDS);
                    Multithreading.schedule(() -> MUtils.chat("§e[Quest NPC] &fI will be hiding in a different location each time you complete a quest."), 4, TimeUnit.SECONDS);
                    Multithreading.schedule(() -> MUtils.chat("§e[Quest NPC] &fClick me again to recieve your first "), 6, TimeUnit.SECONDS);

                    JSONObject obj1 = new JSONObject();
                    obj1.put("interacted", true);
                    Socket.CLIENT.sendText(new Request(
                        "method", "updateData",
                        "uuid", Minecraft.getMinecraft().thePlayer.getUniqueID().toString(),
                        "questNPC", obj1.toString()
                    ).toString());
                    Socket.cachedData.put("questNPC", obj1);
                } else {

                }

            }
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

    public static void LookAt(double px, double py, double pz , EntityPlayer me)
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
            if (EntitySelectors.NOT_SPECTATING.apply(entityplayer1) && !entityplayer1.getUniqueID().equals(QuestNPC.player.getUniqueID())) {
                double d1 = entityplayer1.getDistanceSq(x, y, z);
                if ((distance < 0.0 || d1 < distance * distance) && (d0 == -1.0 || d1 < d0)) {
                    d0 = d1;
                    entityplayer = entityplayer1;
                }
            }
        }

        return entityplayer;
    }

    public static GameProfile createGameProfileWithSkin(String playerName) {
        return new GameProfile(UUID.randomUUID(), playerName);
    }

    public static BlockPos pos = null;
    public static BlockPos checkPosition() {
        World world = Minecraft.getMinecraft().theWorld;
        int centerX = Minecraft.getMinecraft().thePlayer.getPosition().getX();
        int centerY = Minecraft.getMinecraft().thePlayer.getPosition().getY();
        int centerZ = Minecraft.getMinecraft().thePlayer.getPosition().getZ();

        int radius = 200; // Maximum distance from the given coordinates

        Random random = new Random();

        int randomX = centerX + random.nextInt(radius * 2 + 1) - radius;
        int randomY = centerY + random.nextInt(radius * 2 + 1) - radius;
        int randomZ = centerZ + random.nextInt(radius * 2 + 1) - radius;

        boolean block = world.getBlockState(new BlockPos(randomX, randomY, randomZ)).getBlock().isBlockSolid(world, new BlockPos(randomX, randomY, randomZ), EnumFacing.UP);
        boolean air = world.isAirBlock(new BlockPos(randomX, randomY + 1, randomZ)) && world.isAirBlock(new BlockPos(randomX, randomY + 2, randomZ)) && world.isAirBlock(new BlockPos(randomX, randomY + 3, randomZ));

        if (!block || !air || !world.canSeeSky(new BlockPos(randomX, randomY + 3, randomZ))) {
            return checkPosition();
        } else {
            MUtils.chat("§a" + randomX + " " + randomY + " " + randomZ);
            return new BlockPos(randomX, randomY + 3, randomZ);
        }
    }
}
