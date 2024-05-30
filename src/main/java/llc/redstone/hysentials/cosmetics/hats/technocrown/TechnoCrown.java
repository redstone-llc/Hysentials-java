package llc.redstone.hysentials.cosmetics.hats.technocrown;

import llc.redstone.hysentials.cosmetic.CosmeticGui;
import llc.redstone.hysentials.cosmetic.CosmeticManager;
import llc.redstone.hysentials.cosmetics.AbstractCosmetic;
import llc.redstone.hysentials.cosmetics.Cosmetic;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class TechnoCrown implements Cosmetic {
    TechnoCrownModel model;
    public TechnoCrown() {
        model = new TechnoCrownModel();
        AbstractCosmetic.cosmetics.add(this);
    }
    public boolean canUse(EntityPlayer player) {
        return CosmeticManager.equippedCosmetic(player.getUniqueID(), "techno crown")
            && (CosmeticManager.hasCosmetic(player.getUniqueID(), "techno crown") || CosmeticManager.isPreviewing(player.getUniqueID(), "techno crown"));
    }

    @Override
    public ModelBase getModel() {
        return model;
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation("hysentials:hats/technocrown.png");
    }

    @Override
    public String getName() {
        return "techno crown";
    }
}
