package cc.woverflow.hysentials.cosmetics.hats.cat;

import cc.woverflow.hysentials.cosmetic.CosmeticGui;
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
        return CosmeticGui.Companion.equippedCosmetic(player.getUniqueID(), name)
            && CosmeticGui.Companion.hasCosmetic(player.getUniqueID(), name);
    }

    public static void loadCatHats() {
        catHats.add(new CatHat("boncuk"));
        catHats.add(new CatHat("reese"));
        catHats.add(new CatHat("thor"));
        catHats.add(new CatHat("charlie"));
    }
}
