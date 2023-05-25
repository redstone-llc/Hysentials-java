package cc.woverflow.hysentials.mixin;

import cc.woverflow.hysentials.handlers.imageicons.ImageIcon;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = SimpleReloadableResourceManager.class)
public class SimpleReloadableResourceManagerMixin {
    @Inject(method = "reloadResources", at = @At(shift = At.Shift.AFTER, value = "INVOKE", target = "Lnet/minecraft/client/resources/SimpleReloadableResourceManager;notifyReloadListeners()V"))
    public void onReloadResourcesInjectAfter(List<IResourcePack> resourcesPacksList, CallbackInfo ci) {
        System.out.println("Reloading image icons...");
        ImageIcon.reloadIcons();
    }
}
