package llc.redstone.hysentials.mixin;

import llc.redstone.hysentials.util.SplashProgress;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Semaphore;

@Mixin(value=FMLClientHandler.class, remap = false)
public class FMLClientHandlerMixin {
    @Redirect(method = "beginMinecraftLoading", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/SplashProgress;start()V"))
    private void rdStart() {
        SplashProgress.start();
    }

    @Redirect(method = "onInitializationComplete", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/SplashProgress;finish()V"))
    private void rdFinish3() {
        SplashProgress.finish();
    }

    @Redirect(method = "haltGame", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/SplashProgress;finish()V"))
    private void rdFinish() {
        SplashProgress.finish();
    }

    @Redirect(method = "finishMinecraftLoading", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/SplashProgress;finish()V"))
    private void rdFinish2() {
        SplashProgress.finish();
    }

    @Redirect(method = "processWindowMessages", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/Semaphore;tryAcquire()Z"))
    private boolean rdTryAcquire(Semaphore instance) {
        return SplashProgress.mutex.tryAcquire();
    }

    @Redirect(method = "processWindowMessages", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/Semaphore;release()V"))
    private void rdRelease(Semaphore instance) {
        SplashProgress.mutex.release();
    }
}
