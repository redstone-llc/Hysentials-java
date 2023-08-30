package cc.woverflow.hysentials.cosmetics.cubit;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderCubit extends RenderLiving<EntityCubit> {
    private final ResourceLocation hamsterTexture = new ResourceLocation("hysentials:pets/cubit.png");

    public RenderCubit(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new CubitModel(), 0.2f);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityCubit entity) {
        return hamsterTexture;
    }

    @Override
    protected void preRenderCallback(EntityCubit entitylivingbaseIn, float partialTickTime) {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }
}
