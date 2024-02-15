package llc.redstone.hysentials.cosmetics.kzero;

import llc.redstone.hysentials.cosmetic.CosmeticGui;
import llc.redstone.hysentials.cosmetic.CosmeticGui;
import llc.redstone.hysentials.cosmetic.CosmeticUtilsKt;
import net.minecraft.client.entity.AbstractClientPlayer;

public class KzeroBundle {
    public static HairModel hairModel;
    public static KatanaModel katanaModel;
    public static RobeModel robeModel;
    public static SlipperModel slipperModel;

    public KzeroBundle() {
        hairModel = new HairModel();
        katanaModel = new KatanaModel();
        robeModel = new RobeModel();
        slipperModel = new SlipperModel();
    }

    public static boolean canUse(AbstractClientPlayer player, Type type) {
        return CosmeticUtilsKt.equippedCosmetic(player.getUniqueID(), "kzero " + type.name().toLowerCase())
            && CosmeticUtilsKt.hasCosmetic(player.getUniqueID(), "kzero " + type.name().toLowerCase());
    }

    public enum Type {
        HAIR,
        ROBE,
        SLIPPER,
        BUNDLE
    }
}
