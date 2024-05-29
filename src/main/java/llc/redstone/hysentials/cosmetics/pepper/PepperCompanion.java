package llc.redstone.hysentials.cosmetics.pepper;

import cc.polyfrost.oneconfig.libs.universal.UChat;
import llc.redstone.hysentials.cosmetic.CosmeticGui;
import llc.redstone.hysentials.cosmetic.CosmeticManager;
import llc.redstone.hysentials.cosmetics.AbstractCosmetic;
import llc.redstone.hysentials.cosmetics.Cosmetic;
import llc.redstone.hysentials.cosmetics.cubit.EntityCubit;
import llc.redstone.hysentials.util.BUtils;
import llc.redstone.hysentials.websocket.Socket;
import llc.redstone.hysentials.cosmetic.CosmeticGui;
import llc.redstone.hysentials.cosmetics.AbstractCosmetic;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PepperCompanion extends AbstractCosmetic<EntityPepper> implements Cosmetic {
    private Map<UUID, EntityPepper> peppers = new HashMap<>();
    private PepperModel model = new PepperModel();

    public PepperCompanion() {
        super(false);
        AbstractCosmetic.cosmetics.add(this);
    }

    @Override
    public Map<UUID, EntityPepper> getEntities() {
        return peppers;
    }

    @Override
    public boolean canUse(EntityPlayer player) {
        return CosmeticManager.equippedCosmetic(player.getUniqueID(), "pepper")
            && CosmeticManager.hasCosmetic(player.getUniqueID(), "pepper")
            && !player.isInvisible();
    }

    @Override
    public ModelBase getModel() {
        return model;
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation("hysentials:pets/pepper.png");
    }

    @Override
    public String getName() {
        return "pepper";
    }

    public static long cooldown = 0;
    @Override
    public void interact(EntityPepper entity) {

    }

    @Override
    public EntityPepper getEntity(Entity entity) {
        if (entity instanceof EntityArmorStand) {
            return peppers.values().stream().filter(e -> e.armorStand.getUniqueID().equals(entity.getUniqueID())).findFirst().orElse(null);
        }
        return super.getEntity(entity);
    }

    public void spawnPet(EntityPlayer player) {
        if (!canUse(player)) return;
        WorldClient theWorld = Minecraft.getMinecraft().theWorld;
        EntityPepper hamster = new EntityPepper(theWorld, player.getGameProfile().getName());
        hamster.setPosition(player.posX, player.posY, player.posZ);
        hamster.setOwnerId(player.getUniqueID().toString());
        hamster.setCustomNameTag(player.getName() + "'s Pepper Companion");
        hamster.setAlwaysRenderNameTag(true);
        theWorld.spawnEntityInWorld(hamster);
        peppers.put(player.getUniqueID(), hamster);
    }
}
