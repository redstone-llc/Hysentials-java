package llc.redstone.hysentials.mixin;

import llc.redstone.hysentials.GuiIngameHysentials;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.macrowheel.overlay.MacroWheelOverlayKt;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.MouseHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Redirect(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/MouseHelper;mouseXYChange()V"))
    private void updateCameraAndRenderInject(MouseHelper instance) {
        if (!HysentialsConfig.macroWheelKeyBind.isActive() && !MacroWheelOverlayKt.getStopped() && GuiIngameHysentials.cooldown < System.currentTimeMillis()) {
            instance.mouseXYChange();
        }
    }
}
