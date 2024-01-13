package llc.redstone.hysentials.cosmetics.hats.technocrown;

import llc.redstone.hysentials.cosmetic.CosmeticGui;
import net.minecraft.entity.player.EntityPlayer;

public class TechnoCrown {
    TechnoCrownModel model;
    public TechnoCrown() {
        model = new TechnoCrownModel();
    }
    public boolean canUse(EntityPlayer player) {
        return CosmeticGui.Companion.equippedCosmetic(player.getUniqueID(), "techno crown")
            && CosmeticGui.Companion.hasCosmetic(player.getUniqueID(), "techno crown");
    }
}
