package llc.redstone.hysentials.cosmetics.hats.blackcat;

import llc.redstone.hysentials.cosmetic.CosmeticGui;
import llc.redstone.hysentials.cosmetic.CosmeticUtilsKt;
import llc.redstone.hysentials.cosmetics.hats.technocrown.TechnoCrownModel;
import net.minecraft.entity.player.EntityPlayer;

public class BlackCat {
    BlackCatModel model;
    public BlackCat() {
        model = new BlackCatModel();
    }
    public boolean canUse(EntityPlayer player) {
        return CosmeticUtilsKt.equippedCosmetic(player.getUniqueID(), "black cat")
            && CosmeticUtilsKt.hasCosmetic(player.getUniqueID(), "black cat");
    }
}
