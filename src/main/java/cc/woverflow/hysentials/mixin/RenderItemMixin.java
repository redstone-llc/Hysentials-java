package cc.woverflow.hysentials.mixin;

import cc.woverflow.hysentials.event.events.RenderItemInGuiEvent;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderItem.class)
public class RenderItemMixin {
    @Inject(method = "renderItemAndEffectIntoGUI", at = @At("HEAD"), cancellable = true)
    public void renderItemAndEffectIntoGUI(ItemStack stack, int xPosition, int yPosition, CallbackInfo ci) {
        RenderItemInGuiEvent event = new RenderItemInGuiEvent(stack, xPosition, yPosition);
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isCanceled()) {
            ci.cancel();
        }
    }
}
