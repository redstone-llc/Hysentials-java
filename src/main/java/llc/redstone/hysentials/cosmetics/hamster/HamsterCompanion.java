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

package llc.redstone.hysentials.cosmetics.hamster;

import llc.redstone.hysentials.cosmetic.CosmeticGui;
import llc.redstone.hysentials.cosmetic.CosmeticManager;
import llc.redstone.hysentials.cosmetics.AbstractCosmetic;
import llc.redstone.hysentials.cosmetics.Cosmetic;
import llc.redstone.hysentials.event.InvokeEvent;
import llc.redstone.hysentials.cosmetic.CosmeticGui;
import llc.redstone.hysentials.cosmetics.AbstractCosmetic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.*;

public class HamsterCompanion extends AbstractCosmetic<EntityHamster> implements Cosmetic {
    private Map<UUID, EntityHamster> hamsters = new HashMap<>();
    private HamsterModel model = new HamsterModel();

    public HamsterCompanion() {
        super(false);
        AbstractCosmetic.cosmetics.add(this);
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
        return CosmeticManager.equippedCosmetic(player.getUniqueID(), "hamster")
            && CosmeticManager.hasCosmetic(player.getUniqueID(), "hamster")
            && !player.isInvisible();
    }

    @Override
    public ModelBase getModel() {
        return model;
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation("hysentials:pets/hamsterbrown.png");
    }

    @Override
    public String getName() {
        return "hamster";
    }

    @Override
    public void interact(EntityHamster entity) {

    }
}
