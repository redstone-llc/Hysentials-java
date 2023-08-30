package cc.woverflow.hysentials.mixin;

import cc.woverflow.hysentials.cosmetics.cubit.EntityCubit;
import cc.woverflow.hysentials.cosmetics.cubit.RenderCubit;
import cc.woverflow.hysentials.cosmetics.hamster.EntityHamster;
import cc.woverflow.hysentials.cosmetics.hamster.RenderHamster;
import cc.woverflow.hysentials.cosmetics.pepper.EntityPepper;
import cc.woverflow.hysentials.cosmetics.pepper.RenderPepper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(value = RenderManager.class)
public class MixinRenderManager {
    @Shadow
    public Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderMap;
    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectEntities(TextureManager renderEngineIn, RenderItem itemRendererIn, CallbackInfo ci) {
        entityRenderMap.put(EntityCubit.class, new RenderCubit((RenderManager) (Object) this));
        entityRenderMap.put(EntityPepper.class, new RenderPepper((RenderManager) (Object) this));
        entityRenderMap.put(EntityHamster.class, new RenderHamster((RenderManager) (Object) this));
    }

    @Inject(method = "getFontRenderer", at = @At("HEAD"), cancellable = true)
    private void getFontRenderer(CallbackInfoReturnable<FontRenderer> cir) {
        cir.setReturnValue(Minecraft.getMinecraft().fontRendererObj);
    }
}
