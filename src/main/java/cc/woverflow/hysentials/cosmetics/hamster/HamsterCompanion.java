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

package cc.woverflow.hysentials.cosmetics.hamster;

import cc.woverflow.hysentials.cosmetic.CosmeticGui;
import cc.woverflow.hysentials.cosmetics.AbstractCosmetic;
import cc.woverflow.hysentials.event.InvokeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.*;

public class HamsterCompanion extends AbstractCosmetic<EntityHamster> {
    private Map<UUID, EntityHamster> hamsters = new HashMap<>();

    public HamsterCompanion() {
        super(false);
    }


    public void spawnPet(EntityPlayer player) {
        if (!canUse(player)) return;
        WorldClient theWorld = Minecraft.getMinecraft().theWorld;
        EntityHamster hamster = new EntityHamster(theWorld, player.getName());
        hamster.setPosition(player.posX, player.posY, player.posZ);
        hamster.setOwnerId(player.getUniqueID().toString());
        theWorld.spawnEntityInWorld(hamster);
        hamsters.put(player.getUniqueID(), hamster);
    }

    @Override
    public EntityHamster getEntity(Entity entity) {
        if (entity instanceof EntityArmorStand) {
            return hamsters.values().stream().filter(e -> e.armorStand.getUniqueID().equals(entity.getUniqueID())).findFirst().orElse(null);
        }
        return super.getEntity(entity);
    }

    @Override
    public Map<UUID, EntityHamster> getEntities() {
        return hamsters;
    }

    @Override
    public boolean canUse(EntityPlayer player) {
        return CosmeticGui.Companion.equippedCosmetic(player.getUniqueID(), "hamster")
            && CosmeticGui.Companion.hasCosmetic(player.getUniqueID(), "hamster");
    }

    @Override
    public void interact(EntityHamster entity) {

    }
}
