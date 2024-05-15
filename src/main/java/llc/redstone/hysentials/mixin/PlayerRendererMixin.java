package llc.redstone.hysentials.mixin;

import llc.redstone.hysentials.cosmetics.capes.CustomCapeRenderLayer;
import llc.redstone.hysentials.cosmetics.backpack.LayerBackPack;
import llc.redstone.hysentials.cosmetics.hats.blackcat.LayerBlackCatHat;
import llc.redstone.hysentials.cosmetics.hats.cat.LayerCatHat;
import llc.redstone.hysentials.cosmetics.hats.technocrown.LayerTechnoCrown;
import llc.redstone.hysentials.cosmetics.kzero.KzeroLayer;
import llc.redstone.hysentials.cosmetics.wings.dragon.LayerDragonWings;
import llc.redstone.hysentials.cosmetics.wings.tdarth.LayerTdarthWings;
import net.minecraft.client.model.ModelPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;

@Mixin(RenderPlayer.class)
public abstract class PlayerRendererMixin extends RendererLivingEntity<AbstractClientPlayer> {
    @Shadow
    private boolean smallArms;

    @Shadow
    public abstract ModelPlayer getMainModel();

    public PlayerRendererMixin(RenderManager p_i46156_1_, ModelBase p_i46156_2_, float p_i46156_3_) {
        super(p_i46156_1_, p_i46156_2_, p_i46156_3_);
    }

    @Inject(method = "<init>*", at = @At("RETURN"))
    public void onCreate(CallbackInfo info) {
        addLayer(new CustomCapeRenderLayer((RenderPlayer)(Object)this, getMainModel()));
        addLayer(new LayerTechnoCrown((RenderPlayer)(Object)this));
        addLayer(new LayerCatHat((RenderPlayer)(Object)this));
        addLayer(new LayerBackPack((RenderPlayer)(Object)this));
        addLayer(new LayerBlackCatHat((RenderPlayer)(Object)this));
        addLayer(new KzeroLayer((RenderPlayer)(Object)this));
        addLayer(new LayerTdarthWings((RenderPlayer)(Object)this));
        addLayer(new LayerDragonWings((RenderPlayer)(Object)this));
    }
}
