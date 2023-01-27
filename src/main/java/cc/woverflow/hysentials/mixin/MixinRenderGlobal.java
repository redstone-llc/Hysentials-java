package cc.woverflow.hysentials.mixin;

import cc.woverflow.hysentials.event.EventBus;
import cc.woverflow.hysentials.event.render.RenderEntitiesEvent;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {
    @Inject(method = "renderEntities", at = @At(value = "HEAD", target = "Lnet/minecraft/client/renderer/RenderHelper;disableStandardItemLighting()V"))
    private void renderEnt(Entity renderViewEntity, ICamera camera, float partialTicks, CallbackInfo info) {
        EventBus.INSTANCE.post(new RenderEntitiesEvent(partialTicks));
    }
}
