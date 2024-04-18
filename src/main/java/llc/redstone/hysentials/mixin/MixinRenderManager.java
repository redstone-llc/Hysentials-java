package llc.redstone.hysentials.mixin;

import llc.redstone.hysentials.cosmetics.cubit.EntityCubit;
import llc.redstone.hysentials.cosmetics.cubit.RenderCubit;
import llc.redstone.hysentials.cosmetics.hamster.EntityHamster;
import llc.redstone.hysentials.cosmetics.hamster.RenderHamster;
import llc.redstone.hysentials.cosmetics.miya.EntityMiya;
import llc.redstone.hysentials.cosmetics.miya.RenderMiya;
import llc.redstone.hysentials.cosmetics.pepper.EntityPepper;
import llc.redstone.hysentials.cosmetics.pepper.RenderPepper;
import llc.redstone.hysentials.renderer.plusStand.PlusStandEntity;
import llc.redstone.hysentials.renderer.plusStand.RenderPlusStand;
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
        entityRenderMap.put(EntityMiya.class, new RenderMiya((RenderManager) (Object) this));
        entityRenderMap.put(PlusStandEntity.class, new RenderPlusStand((RenderManager) (Object) this));
    }

    @Inject(method = "getFontRenderer", at = @At("HEAD"), cancellable = true)
    private void getFontRenderer(CallbackInfoReturnable<FontRenderer> cir) {
        cir.setReturnValue(Minecraft.getMinecraft().fontRendererObj);
    }
}
