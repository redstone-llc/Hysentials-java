package llc.redstone.hysentials.cosmetics.backpack;

import llc.redstone.hysentials.cosmetic.CosmeticGui;
import llc.redstone.hysentials.cosmetic.CosmeticManager;
import llc.redstone.hysentials.cosmetics.AbstractCosmetic;
import llc.redstone.hysentials.cosmetics.Cosmetic;
import llc.redstone.hysentials.cosmetics.hats.cat.CatHat;
import llc.redstone.hysentials.cosmetics.hats.cat.CatModel;
import llc.redstone.hysentials.cosmetic.CosmeticGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class BackpackCosmetic implements Cosmetic {
    public static List<BackpackCosmetic> backpacks = new ArrayList<>();
    boolean catpack;
    BackpackModel model;
    CatPackModel catModel;
    String name;
    ResourceLocation texture;
    public BackpackCosmetic(String name, boolean catpack) {
        this.catpack = catpack;
        if (catpack) {
            catModel = new CatPackModel();
        } else {
            model = new BackpackModel();
        }
        this.name = name;
        texture = new ResourceLocation("hysentials:backpacks/" + name + ".png");
        AbstractCosmetic.cosmetics.add(this);
    }
    public boolean canUse(EntityPlayer player) {
        return CosmeticManager.equippedCosmetic(player.getUniqueID(), name)
            && (CosmeticManager.hasCosmetic(player.getUniqueID(), name) || CosmeticManager.isPreviewing(player.getUniqueID(), name));
    }

    public ModelBase getModel() {
        return catpack ? catModel : model;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    @Override
    public String getName() {
        return name;
    }

    public static void loadBackpacks() {
        backpacks.add(new BackpackCosmetic("redstonebackpack", false));
        backpacks.add(new BackpackCosmetic("goldbackpack", false));
        backpacks.add(new BackpackCosmetic("diamondbackpack", false));
        backpacks.add(new BackpackCosmetic("thorcatpack", true));
        backpacks.add(new BackpackCosmetic("boncukcatpack", true));
        backpacks.add(new BackpackCosmetic("reesecatpack", true));
        backpacks.add(new BackpackCosmetic("peppercatpack", true));
    }
}
