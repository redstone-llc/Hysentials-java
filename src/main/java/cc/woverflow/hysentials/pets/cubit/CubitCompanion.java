/*
 *       Copyright (C) 2018-present Hyperium <https://hyperium.cc/>
 *
 *       This program is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU Lesser General Public License as published
 *       by the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU Lesser General Public License for more details.
 *
 *       You should have received a copy of the GNU Lesser General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cc.woverflow.hysentials.pets.cubit;

import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.woverflow.hysentials.event.InvokeEvent;
import cc.woverflow.hysentials.event.render.RenderEntitiesEvent;
import cc.woverflow.hysentials.event.render.RenderPlayerEvent;
import cc.woverflow.hysentials.event.world.WorldChangeEvent;
import cc.woverflow.hysentials.pets.AbstractCosmetic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.*;

public class CubitCompanion extends AbstractCosmetic {
    private List<EntityPlayer> toAdd = new ArrayList<>();
    private Map<UUID, EntityCubit> hamsters = new HashMap<>();

    public CubitCompanion() {
        super(false);
    }

    @InvokeEvent
    public void renderEntities(RenderEntitiesEvent entitiesEvent) {
        renderPlayer(new RenderPlayerEvent(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().getRenderManager(), 0, 0, 0,
            entitiesEvent.getPartialTicks()));
    }

    @InvokeEvent
    public void renderPlayer(RenderPlayerEvent e) {
        if (Minecraft.getMinecraft().theWorld == null) return;
        UUID uuid = e.getEntity().getUniqueID();

        toAdd.add(e.getEntity());
    }

    @InvokeEvent
    public void onTick(TickEvent e) {
        WorldClient theWorld = Minecraft.getMinecraft().theWorld;
        if (theWorld == null) return;

        toAdd.forEach(this::spawnPet);
        toAdd.clear();

        Iterator<Map.Entry<UUID, EntityCubit>> ite = hamsters.entrySet().iterator();

        while (ite.hasNext()) {
            Map.Entry<UUID, EntityCubit> next = ite.next();

            if (!worldHasEntityWithUUID(theWorld, next.getKey())) {
                theWorld.unloadEntities(Collections.singletonList(next.getValue()));
                ite.remove();
            }
        }
    }

    @InvokeEvent
    public void onWorldChange(WorldChangeEvent e) {
        hamsters.clear();
    }

    public boolean worldHasEntityWithUUID(World world, UUID id) {
        return world.loadedEntityList.stream().anyMatch(entity -> entity.getUniqueID().equals(id));
    }

    @Override
    public void spawnPet(EntityPlayer player) {
        WorldClient theWorld = Minecraft.getMinecraft().theWorld;
        EntityCubit hamster = new EntityCubit(theWorld);
        hamster.setPosition(player.posX, player.posY, player.posZ);
        hamster.setOwnerId(player.getUniqueID().toString());
        hamster.setCustomNameTag("§b" + player.getName() + "'s Cubit");
        hamster.setAlwaysRenderNameTag(true);
        theWorld.spawnEntityInWorld(hamster);
        hamsters.put(player.getUniqueID(), hamster);
    }
}
