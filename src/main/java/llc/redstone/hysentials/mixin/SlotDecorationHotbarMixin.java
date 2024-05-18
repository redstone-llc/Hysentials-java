package llc.redstone.hysentials.mixin;

import llc.redstone.hysentials.util.RenderItemUtils;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiIngame.class)
public class SlotDecorationHotbarMixin {
    @Inject(method = "renderHotbarItem", at = @At(value = "HEAD"))
    public void renderHotbarItem(int index, int i, int j, float partialTicks, EntityPlayer player, CallbackInfo ci) {
        ItemStack itemstack = player.inventory.mainInventory[index];
        RenderItemUtils.drawSlotDecoration(i, j, itemstack, true);
    }
}
