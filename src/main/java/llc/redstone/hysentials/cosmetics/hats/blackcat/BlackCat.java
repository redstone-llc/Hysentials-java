package llc.redstone.hysentials.cosmetics.hats.blackcat;

import llc.redstone.hysentials.cosmetic.CosmeticGui;
import llc.redstone.hysentials.cosmetic.CosmeticManager;
import llc.redstone.hysentials.cosmetics.AbstractCosmetic;
import llc.redstone.hysentials.cosmetics.Cosmetic;
import llc.redstone.hysentials.cosmetics.hats.technocrown.TechnoCrownModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class BlackCat implements Cosmetic {
    BlackCatModel model;
    public BlackCat() {
        model = new BlackCatModel();
        AbstractCosmetic.cosmetics.add(this);
    }
    public boolean canUse(EntityPlayer player) {
        return CosmeticManager.equippedCosmetic(player.getUniqueID(), "black cat")
            && CosmeticManager.hasCosmetic(player.getUniqueID(), "black cat");
    }

    public BlackCatModel getModel() {
        return model;
    }

    public ResourceLocation getTexture() {
        return new ResourceLocation("hysentials:hats/blackcat.png");
    }

    @Override
    public String getName() {
        return "black cat";
    }
}
