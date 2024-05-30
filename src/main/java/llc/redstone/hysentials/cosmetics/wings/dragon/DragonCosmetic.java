package llc.redstone.hysentials.cosmetics.wings.dragon;

import llc.redstone.hysentials.cosmetic.CosmeticManager;
import llc.redstone.hysentials.cosmetics.AbstractCosmetic;
import llc.redstone.hysentials.cosmetics.Cosmetic;
import llc.redstone.hysentials.cosmetics.wings.tdarth.TdarthModel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class DragonCosmetic implements Cosmetic {
    ResourceLocation texture;
    DragonWingsModel model = new DragonWingsModel();
    public DragonCosmetic() {
        texture = new ResourceLocation("hysentials:wings/dragon.png");
        AbstractCosmetic.cosmetics.add(this);
    }
    public boolean canUse(EntityPlayer player) {
        return CosmeticManager.equippedCosmetic(player.getUniqueID(), "dragon")
            && (CosmeticManager.hasCosmetic(player.getUniqueID(), "dragon") || CosmeticManager.isPreviewing(player.getUniqueID(), "dragon"));
    }

    @Override
    public ModelBase getModel() {
        return model;
    }

    @Override
    public ResourceLocation getTexture() {
        return texture;
    }

    @Override
    public String getName() {
        return "dragon";
    }
}
