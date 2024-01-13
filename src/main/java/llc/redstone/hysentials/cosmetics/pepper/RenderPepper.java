package llc.redstone.hysentials.cosmetics.pepper;

import llc.redstone.hysentials.cosmetics.cubit.CubitModel;
import llc.redstone.hysentials.cosmetics.cubit.EntityCubit;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderPepper extends RenderLiving<EntityPepper> {
    private final ResourceLocation hamsterTexture = new ResourceLocation("hysentials:pets/pepper.png");

    public RenderPepper(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new PepperModel(), 0.2f);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityPepper entity) {
        return hamsterTexture;
    }

    @Override
    protected void preRenderCallback(EntityPepper entitylivingbaseIn, float partialTickTime) {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }
}
