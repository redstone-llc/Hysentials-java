package cc.woverflow.hysentials.mixin;

import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(ItemMonsterPlacer.class)
public class ItemMonsterPlacerMixin {
    @Shadow
    private static EntityList.EntityEggInfo getEggInfo(ItemStack stack) {
        return null;
    }
    /**
     * @author
     * @reason
     */
    @SideOnly(Side.CLIENT)
    @Overwrite()
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        EntityList.EntityEggInfo entitylist$entityegginfo = getEggInfo(stack);
        return entitylist$entityegginfo != null ? (renderPass == 0 ? entitylist$entityegginfo.primaryColor : entitylist$entityegginfo.secondaryColor) : 16777215;
    }
}
