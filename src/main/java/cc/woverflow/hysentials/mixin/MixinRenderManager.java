package cc.woverflow.hysentials.mixin;

import cc.woverflow.hysentials.pets.cubit.EntityCubit;
import cc.woverflow.hysentials.pets.cubit.RenderCubit;
import cc.woverflow.hysentials.pets.hamster.EntityHamster;
import cc.woverflow.hysentials.pets.hamster.RenderHamster;
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

import java.util.Map;

@Mixin(value = RenderManager.class)
public class MixinRenderManager {
    @Shadow
    private Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderMap;
    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectEntities(TextureManager renderEngineIn, RenderItem itemRendererIn, CallbackInfo ci) {
        entityRenderMap.put(EntityHamster.class, new RenderHamster((RenderManager) (Object) this));
        entityRenderMap.put(EntityCubit.class, new RenderCubit((RenderManager) (Object) this));
    }
}
