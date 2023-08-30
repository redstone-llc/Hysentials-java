package cc.woverflow.hysentials.cosmetics.hats.technocrown;

import cc.woverflow.hysentials.cosmetic.CosmeticGui;
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
