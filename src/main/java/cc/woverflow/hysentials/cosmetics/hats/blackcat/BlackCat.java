package cc.woverflow.hysentials.cosmetics.hats.blackcat;

import cc.woverflow.hysentials.cosmetic.CosmeticGui;
import cc.woverflow.hysentials.cosmetics.hats.technocrown.TechnoCrownModel;
import net.minecraft.entity.player.EntityPlayer;

public class BlackCat {
    BlackCatModel model;
    public BlackCat() {
        model = new BlackCatModel();
    }
    public boolean canUse(EntityPlayer player) {
        return CosmeticGui.Companion.equippedCosmetic(player.getUniqueID(), "black cat")
            && CosmeticGui.Companion.hasCosmetic(player.getUniqueID(), "black cat");
    }
}
