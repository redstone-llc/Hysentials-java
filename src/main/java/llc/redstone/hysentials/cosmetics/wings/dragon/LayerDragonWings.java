package llc.redstone.hysentials.cosmetics.wings.dragon;

import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.cosmetics.wings.tdarth.TdarthCosmetic;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class LayerDragonWings implements LayerRenderer<AbstractClientPlayer> {
    private final RenderPlayer playerRenderer;

    public LayerDragonWings(RenderPlayer renderPlayer) {
        this.playerRenderer = renderPlayer;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float scale) {
        DragonCosmetic wings = Hysentials.INSTANCE.dragonCosmetic;
        if (wings == null) {
            return;
        }
        if (!wings.canUse(entitylivingbaseIn)) {
            return;
        }
        if (entitylivingbaseIn.isInvisible()) return;

        GlStateManager.pushMatrix();
        this.playerRenderer.bindTexture(wings.texture);

        boolean shifting = entitylivingbaseIn.isSneaking();
        GlStateManager.rotate(28.6f, (shifting ? 1F : 0), 0.0F, 0.0F);
        GlStateManager.translate(0.0f, 0.0 + (shifting ? 0.25F : 0F), 0.06F);
        float n = 0.5f;
        GlStateManager.scale(n, n, n);
        wings.model.render(entitylivingbaseIn, f, g, h, i, j, scale);
        GlStateManager.popMatrix();

    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
