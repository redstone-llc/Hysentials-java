package llc.redstone.hysentials.mixin;

import llc.redstone.hysentials.config.hysentialMods.FormattingConfig;
import llc.redstone.hysentials.util.ImageIconRenderer;
import llc.redstone.hysentials.util.RenderItemUtils;
import llc.redstone.hysentials.util.Renderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

import static net.minecraftforge.fml.client.config.GuiUtils.drawGradientRect;

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
