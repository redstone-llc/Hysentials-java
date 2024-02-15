package llc.redstone.hysentials.cosmetics.hats.technocrown;

import llc.redstone.hysentials.cosmetic.CosmeticGui;
import llc.redstone.hysentials.cosmetic.CosmeticUtilsKt;
import net.minecraft.entity.player.EntityPlayer;

public class TechnoCrown {
    TechnoCrownModel model;
    public TechnoCrown() {
        model = new TechnoCrownModel();
    }
    public boolean canUse(EntityPlayer player) {
        return CosmeticUtilsKt.equippedCosmetic(player.getUniqueID(), "techno crown")
            && CosmeticUtilsKt.hasCosmetic(player.getUniqueID(), "techno crown");
    }
}
