package cc.woverflow.hysentials.cosmetics.miya;

import cc.woverflow.hysentials.cosmetic.CosmeticGui;
import cc.woverflow.hysentials.cosmetics.AbstractCosmetic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MiyaCompanion extends AbstractCosmetic<EntityMiya> {
    private Map<UUID, EntityMiya> miyas = new HashMap<>();

    public MiyaCompanion() {
        super(false);
    }

    @Override
    public Map<UUID, EntityMiya> getEntities() {
        return miyas;
    }

    @Override
    public boolean canUse(EntityPlayer player) {
        return CosmeticGui.Companion.equippedCosmetic(player.getUniqueID(), "miya")
            && CosmeticGui.Companion.hasCosmetic(player.getUniqueID(), "miya")
            && !player.isInvisible();
    }

    public static long cooldown = 0;
    @Override
    public void interact(EntityMiya entity) {

    }

    @Override
    public EntityMiya getEntity(Entity entity) {
        if (entity instanceof EntityArmorStand) {
            return miyas.values().stream().filter(e -> e.armorStand.getUniqueID().equals(entity.getUniqueID())).findFirst().orElse(null);
        }
        return super.getEntity(entity);
    }

    public void spawnPet(EntityPlayer player) {
        if (!canUse(player)) return;
        WorldClient theWorld = Minecraft.getMinecraft().theWorld;
        EntityMiya hamster = new EntityMiya(theWorld, player.getGameProfile().getName());
        hamster.setPosition(player.posX, player.posY, player.posZ);
        hamster.setOwnerId(player.getUniqueID().toString());
        hamster.setCustomNameTag(player.getName() + "'s Miya Companion");
        hamster.setAlwaysRenderNameTag(true);
        theWorld.spawnEntityInWorld(hamster);
        miyas.put(player.getUniqueID(), hamster);
    }
}
