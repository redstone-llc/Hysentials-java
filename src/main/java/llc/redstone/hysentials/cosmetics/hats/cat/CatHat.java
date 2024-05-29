package llc.redstone.hysentials.cosmetics.hats.cat;

import llc.redstone.hysentials.cosmetic.CosmeticGui;
import llc.redstone.hysentials.cosmetic.CosmeticManager;
import llc.redstone.hysentials.cosmetics.AbstractCosmetic;
import llc.redstone.hysentials.cosmetics.Cosmetic;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class CatHat implements Cosmetic {
    public static List<CatHat> catHats = new ArrayList<>();
    CatModel model;
    String name;
    ResourceLocation texture;
    public CatHat(String name) {
        model = new CatModel();
        this.name = name;
        texture = new ResourceLocation("hysentials:hats/" + name + ".png");
        AbstractCosmetic.cosmetics.add(this);
    }
    public boolean canUse(EntityPlayer player) {
        return CosmeticManager.equippedCosmetic(player.getUniqueID(), name)
            && CosmeticManager.hasCosmetic(player.getUniqueID(), name);
    }

    @Override
    public ModelBase getModel() {
        return model;
    }

    @Override
    public ResourceLocation getTexture() {
        return texture;
    }

    @Override
    public String getName() {
        return name;
    }

    public static void loadCatHats() {
        catHats.add(new CatHat("boncuk"));
        catHats.add(new CatHat("reese"));
        catHats.add(new CatHat("thor"));
        catHats.add(new CatHat("charlie"));
    }
}
