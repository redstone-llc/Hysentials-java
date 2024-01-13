package llc.redstone.hysentials.mixin;

import llc.redstone.hysentials.capes.CustomCapeRenderLayer;
import llc.redstone.hysentials.cosmetics.backpack.LayerBackPack;
import llc.redstone.hysentials.cosmetics.hats.blackcat.LayerBlackCatHat;
import llc.redstone.hysentials.cosmetics.hats.cat.LayerCatHat;
import llc.redstone.hysentials.cosmetics.hats.technocrown.LayerTechnoCrown;
import llc.redstone.hysentials.cosmetics.kzero.KzeroBundle;
import llc.redstone.hysentials.cosmetics.kzero.KzeroLayer;
import llc.redstone.hysentials.cosmetics.hats.blackcat.LayerBlackCatHat;
import llc.redstone.hysentials.cosmetics.hats.cat.LayerCatHat;
import llc.redstone.hysentials.cosmetics.hats.technocrown.LayerTechnoCrown;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;

@Mixin(RenderPlayer.class)
public abstract class PlayerRendererMixin extends RendererLivingEntity<AbstractClientPlayer> {
    @Shadow
    private boolean smallArms;
    @Unique
    private ModelPlayer smallArmsModel;
    @Unique
    private ModelPlayer regularModel;

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
    }

    @Inject(method = "getEntityTexture(Lnet/minecraft/client/entity/AbstractClientPlayer;)Lnet/minecraft/util/ResourceLocation;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;getLocationSkin()Lnet/minecraft/util/ResourceLocation;"), cancellable = true)
    protected void getEntityTexture(AbstractClientPlayer entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (KzeroBundle.canUse(entity, KzeroBundle.Type.BUNDLE)) {
            if (smallArmsModel == null) {
                smallArmsModel = new ModelPlayer(0.0F, true);
            }
            mainModel = smallArmsModel;
            cir.setReturnValue(new ResourceLocation("hysentials:kzero_skin.png"));
        } else {
            if (regularModel == null) {
                regularModel = new ModelPlayer(0.0F, smallArms);
            }
            mainModel = regularModel;
            cir.setReturnValue(entity.getLocationSkin());
        }
        mainModel.isChild = entity.isChild();
    }


}
