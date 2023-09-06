package cc.woverflow.hysentials.cosmetics.miya;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderMiya extends RenderLiving<EntityMiya> {
    private final ResourceLocation hamsterTexture = new ResourceLocation("hysentials:pets/miya.png");

    public RenderMiya(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new MiyaModel(), 0.2f);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityMiya entity) {
        return hamsterTexture;
    }

    @Override
    protected void preRenderCallback(EntityMiya entitylivingbaseIn, float partialTickTime) {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }
}
