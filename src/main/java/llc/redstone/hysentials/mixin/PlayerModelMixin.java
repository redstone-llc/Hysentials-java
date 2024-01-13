package llc.redstone.hysentials.mixin;

import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.cosmetics.kzero.KzeroBundle;
import llc.redstone.hysentials.config.HysentialsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.ModelPlayer;

@Mixin(value =  ModelPlayer.class)
public class PlayerModelMixin {

    @Inject(method = "renderCape", at = @At("HEAD"), cancellable = true)
    public void renderCloak(float p_renderCape_1_, CallbackInfo info) {
        if (!HysentialsConfig.disableCustomCapes) info.cancel();

    }
}
