package cc.woverflow.hysentials.pets.hamster;

import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.woverflow.hysentials.event.InvokeEvent;
import cc.woverflow.hysentials.event.render.RenderEntitiesEvent;
import cc.woverflow.hysentials.event.render.RenderPlayerEvent;
import cc.woverflow.hysentials.event.world.WorldChangeEvent;
import cc.woverflow.hysentials.pets.AbstractCosmetic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.*;

public class HamsterCompanion extends AbstractCosmetic {
    private List<EntityPlayer> toAdd = new ArrayList<>();
    private Map<UUID, EntityHamster> hamsters = new HashMap<>();

    public HamsterCompanion() {
        super(false);
    }

//    @InvokeEvent
    public void renderEntities(RenderEntitiesEvent entitiesEvent) {
        renderPlayer(new RenderPlayerEvent(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().getRenderManager(), 0, 0, 0,
            entitiesEvent.getPartialTicks()));
    }

//    @InvokeEvent
    public void renderPlayer(RenderPlayerEvent e) {
        if (Minecraft.getMinecraft().theWorld == null) return;
        UUID uuid = e.getEntity().getUniqueID();

        toAdd.add(e.getEntity());
    }

//    @InvokeEvent
    public void onTick(TickEvent e) {
        WorldClient theWorld = Minecraft.getMinecraft().theWorld;
        if (theWorld == null) return;

        toAdd.forEach(this::spawnPet);
        toAdd.clear();

        Iterator<Map.Entry<UUID, EntityHamster>> ite = hamsters.entrySet().iterator();

        while (ite.hasNext()) {
            Map.Entry<UUID, EntityHamster> next = ite.next();

            if (!worldHasEntityWithUUID(theWorld, next.getKey())) {
                theWorld.unloadEntities(Collections.singletonList(next.getValue()));
                ite.remove();
            }
        }
    }

//    @InvokeEvent
    public void onWorldChange(WorldChangeEvent e) {
        hamsters.clear();
    }

    public boolean worldHasEntityWithUUID(World world, UUID id) {
        return world.loadedEntityList.stream().anyMatch(entity -> entity.getUniqueID().equals(id));
    }

    @Override
    public Map<UUID, EntityHamster> getEntities() {
        return hamsters;
    }

    @Override
    public boolean canUse(EntityPlayer player) {
        return false;
    }

    @Override
    public void interact(Entity entity) {

    }

    public void spawnPet(EntityPlayer player) {
        WorldClient theWorld = Minecraft.getMinecraft().theWorld;
        EntityHamster hamster = new EntityHamster(theWorld);
        hamster.setPosition(player.posX, player.posY, player.posZ);
        hamster.setOwnerId(player.getUniqueID().toString());
        theWorld.spawnEntityInWorld(hamster);
        hamsters.put(player.getUniqueID(), hamster);
    }
}
