package cc.woverflow.hysentials.cosmetics.kzero;

import cc.woverflow.hysentials.cosmetic.CosmeticGui;
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
        return CosmeticGui.Companion.equippedCosmetic(player.getUniqueID(), "kzero " + type.name().toLowerCase())
            && CosmeticGui.Companion.hasCosmetic(player.getUniqueID(), "kzero " + type.name().toLowerCase());
    }

    public enum Type {
        HAIR,
        ROBE,
        SLIPPER,
        BUNDLE
    }
}
