package llc.redstone.hysentials.cosmetics.hats.cat;

import llc.redstone.hysentials.cosmetic.CosmeticGui;
import llc.redstone.hysentials.cosmetic.CosmeticUtilsKt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class CatHat {
    public static List<CatHat> catHats = new ArrayList<>();
    CatModel model;
    String name;
    ResourceLocation texture;
    public CatHat(String name) {
        model = new CatModel();
        this.name = name;
        texture = new ResourceLocation("hysentials:hats/" + name + ".png");
    }
    public boolean canUse(EntityPlayer player) {
        return CosmeticUtilsKt.equippedCosmetic(player.getUniqueID(), name)
            && CosmeticUtilsKt.hasCosmetic(player.getUniqueID(), name);
    }

    public static void loadCatHats() {
        catHats.add(new CatHat("boncuk"));
        catHats.add(new CatHat("reese"));
        catHats.add(new CatHat("thor"));
        catHats.add(new CatHat("charlie"));
    }
}
