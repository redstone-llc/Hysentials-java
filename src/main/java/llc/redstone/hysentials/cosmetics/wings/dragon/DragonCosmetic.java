package llc.redstone.hysentials.cosmetics.wings.dragon;

import llc.redstone.hysentials.cosmetic.CosmeticUtilsKt;
import llc.redstone.hysentials.cosmetics.wings.tdarth.TdarthModel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class DragonCosmetic {
    ResourceLocation texture;
    DragonWingsModel model = new DragonWingsModel();
    public DragonCosmetic() {
        texture = new ResourceLocation("hysentials:wings/dragon.png");
    }
    public boolean canUse(EntityPlayer player) {
        return CosmeticUtilsKt.equippedCosmetic(player.getUniqueID(), "dragon")
            && CosmeticUtilsKt.hasCosmetic(player.getUniqueID(), "dragon");
    }
}
