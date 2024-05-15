package llc.redstone.hysentials.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = Render.class, priority = 1001)
public abstract class RenderMixin<T extends Entity> {
    @Final
    @Shadow
    protected RenderManager renderManager;

    @Redirect(method = "renderLivingLabel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/Render;getFontRendererFromRenderManager()Lnet/minecraft/client/gui/FontRenderer;"))
    public FontRenderer drawString(Render instance) {
        return Minecraft.getMinecraft().fontRendererObj;
    }
}
