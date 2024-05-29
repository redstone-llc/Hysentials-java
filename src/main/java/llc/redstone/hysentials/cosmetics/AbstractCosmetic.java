package llc.redstone.hysentials.cosmetics;

import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import llc.redstone.hysentials.config.hysentialmods.CosmeticConfig;
import llc.redstone.hysentials.util.BUtils;
import llc.redstone.hysentials.handlers.npc.NPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.lang.Thread.sleep;

public abstract class AbstractCosmetic <E extends Entity>{
    public static List<Cosmetic> cosmetics = new ArrayList<>();
    private final boolean selfOnly;

    public AbstractCosmetic(boolean selfOnly) {
        this.selfOnly = selfOnly;
    }

    public abstract Map<UUID, E> getEntities();
    public abstract boolean canUse(EntityPlayer player);
    public abstract void interact(E entity);
    public E getEntity(Entity entity) {
        return getEntities().values().stream().filter(e -> e.getUniqueID().equals(entity.getUniqueID())).findFirst().orElse(null);
    }

    @SubscribeEvent
    public void worldSwitch(WorldEvent.Load changeEvent) {
        getEntities().clear();
    }

    @SubscribeEvent()
    public void onEntityInteract(MouseEvent event) {
        if (event.button == 1) {
            MovingObjectPosition obj = NPC.getMouseOverExtended(4);
            if (obj == null) return;
            if (obj.entityHit == null) return;
            E entity = getEntity(obj.entityHit);
            if (entity != null) {
                interact(entity);
            }
        }
    }

    int ticks = 0;
    @SubscribeEvent
    public void onTick(TickEvent event) {
        WorldClient theWorld = Minecraft.getMinecraft().theWorld;
        if (theWorld == null) return;
        if (!BUtils.isHypixelOrSBX()) return;
        if (!CosmeticConfig.showPets && !BUtils.isSBX() && !(LocrawUtil.INSTANCE.getLocrawInfo() != null && LocrawUtil.INSTANCE.getLocrawInfo().getGameMode().equals("lobby"))) {
            for (EntityPlayer player : theWorld.playerEntities) {
                if (player == null) continue;
                if (player.isDead) continue;
                if (getEntities().containsKey(player.getUniqueID())) {
                    getEntities().get(player.getUniqueID()).setDead();
                    getEntities().remove(player.getUniqueID());
                }
            }
            return;
        }

        if (ticks++ % 20 == 0) {
            for (EntityPlayer player : theWorld.playerEntities) {
                if (player == null) continue;
                if (player.isDead) continue;
                boolean canUse = canUse(player);
                if (!getEntities().containsKey(player.getUniqueID())) {
                    if (canUse) {
                        spawnPet(player);
                    }
                } else {
                    if (!canUse) {
                        getEntities().get(player.getUniqueID()).setDead();
                        getEntities().remove(player.getUniqueID());
                    }
                }
            }
        }
    }

    public void spawnPet(EntityPlayer player) {

    };

}
