package llc.redstone.hysentials.renderer.plusstand;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderPlusStand extends RenderLiving<PlusStandEntity> {
    private final ResourceLocation location = new ResourceLocation("hysentials:model/plusStand.png");

    public RenderPlusStand(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new PlusStandModel(), 0f);
    }

    @Override
    protected ResourceLocation getEntityTexture(PlusStandEntity entity) {
        return location;
    }

    @Override
    protected void preRenderCallback(PlusStandEntity entitylivingbaseIn, float partialTickTime) {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
    }
}
