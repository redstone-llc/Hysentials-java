package llc.redstone.hysentials.cosmetics.hats.ponjo;

import llc.redstone.hysentials.cosmetic.CosmeticManager;
import llc.redstone.hysentials.cosmetics.AbstractCosmetic;
import llc.redstone.hysentials.cosmetics.Cosmetic;
import llc.redstone.hysentials.cosmetics.hats.technocrown.TechnoCrownModel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class PonjoHelmet implements Cosmetic {
    PonjoHelmetModel model;
    public PonjoHelmet() {
        model = new PonjoHelmetModel();
        AbstractCosmetic.cosmetics.add(this);
    }
    public boolean canUse(EntityPlayer player) {
        return CosmeticManager.equippedCosmetic(player.getUniqueID(), "ponjo")
            && (CosmeticManager.hasCosmetic(player.getUniqueID(), "ponjo") || CosmeticManager.isPreviewing(player.getUniqueID(), "ponjo"));
    }

    @Override
    public ModelBase getModel() {
        return model;
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation("hysentials:hats/ponjo.png");
    }

    @Override
    public String getName() {
        return "ponjo";
    }
}
