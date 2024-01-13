package llc.redstone.hysentials.cosmetics.backpack;

import llc.redstone.hysentials.cosmetic.CosmeticGui;
import llc.redstone.hysentials.cosmetics.AbstractCosmetic;
import llc.redstone.hysentials.cosmetics.hats.cat.CatHat;
import llc.redstone.hysentials.cosmetics.hats.cat.CatModel;
import llc.redstone.hysentials.cosmetic.CosmeticGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class BackpackCosmetic {
    public static List<BackpackCosmetic> backpacks = new ArrayList<>();
    BackpackModel model;
    CatPackModel catModel;
    String name;
    ResourceLocation texture;
    public BackpackCosmetic(String name, boolean catpack) {
        if (catpack) {
            catModel = new CatPackModel();
        } else {
            model = new BackpackModel();
        }
        this.name = name;
        texture = new ResourceLocation("hysentials:backpacks/" + name + ".png");
    }
    public boolean canUse(EntityPlayer player) {
        return CosmeticGui.Companion.equippedCosmetic(player.getUniqueID(), name)
            && CosmeticGui.Companion.hasCosmetic(player.getUniqueID(), name);
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
