package llc.redstone.hysentials.mixin;

import llc.redstone.hysentials.util.RenderItemUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiContainer.class, priority = 9001)
public abstract class SlotDecorationMixin {
    @Inject(method = "drawSlot", at = @At(value = "HEAD"))
    public void drawSlot(Slot slotIn, CallbackInfo ci) {
        ItemStack itemstack = slotIn.getStack();
        int i = slotIn.xDisplayPosition;
        int j = slotIn.yDisplayPosition;
        RenderItemUtils.drawSlotDecoration(i, j, itemstack, false);
    }
}
