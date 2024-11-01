package llc.redstone.hysentials.handlers.lobby;

import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import llc.redstone.hysentials.config.hysentialmods.LobbyConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

public class HousingLagReducer {
    int tick;
    List<EntityArmorStand> hiddenArmorStands = new ArrayList<>();
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        final LocrawInfo locraw = LocrawUtil.INSTANCE.getLocrawInfo();
        if (event.phase != TickEvent.Phase.START || UMinecraft.getPlayer() == null || !HypixelUtils.INSTANCE.isHypixel() || this.tick >= 20 || locraw == null) {
            return;
        }
        if (++this.tick == 10) {
            if (!LobbyConfig.housingLagReducer) {
                hiddenArmorStands.clear();
                this.tick = 0;
                return;
            }
            if (locraw.getGameType().equals(LocrawInfo.GameType.HOUSING) && locraw.getGameMode().equals("lobby")) {
                hiddenArmorStands.clear();
                for (Entity entity : Minecraft.getMinecraft().theWorld.loadedEntityList) {
                    if (entity instanceof EntityArmorStand && entity.getDistanceToEntity(UMinecraft.getPlayer()) > 20) {
                        hiddenArmorStands.add((EntityArmorStand) entity);
                    }
                }
            }
            this.tick = 0;
        }
    }

    @SubscribeEvent
    public void cancelRendering(RenderLivingEvent.Pre<? extends EntityLivingBase> event) {
        EntityLivingBase entity = event.entity;
        if (entity instanceof EntityArmorStand && hiddenArmorStands.contains(entity)) {
            event.setCanceled(true);
        }
    }



    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        this.tick = 0;
    }
}
