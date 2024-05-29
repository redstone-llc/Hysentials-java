package llc.redstone.hysentials.cosmetics.wings.tdarth;

import llc.redstone.hysentials.Hysentials;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public class LayerTdarthWings implements LayerRenderer<AbstractClientPlayer> {
    private final RenderPlayer playerRenderer;

    public LayerTdarthWings(RenderPlayer renderPlayer) {
        this.playerRenderer = renderPlayer;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float scale) {
        TdarthCosmetic wings = Hysentials.INSTANCE.tdarthCosmetic;
        if (wings == null) {
            return;
        }
        if (!wings.canUse(entitylivingbaseIn)) {
            return;
        }
        if (entitylivingbaseIn.isInvisible()) return;

        if (TdarthCosmetic.textureMap.containsKey(entitylivingbaseIn.getUniqueID())) {
            GlStateManager.bindTexture(TdarthCosmetic.textureMap.get(entitylivingbaseIn.getUniqueID()).getGlTextureId());
        }
        GlStateManager.pushMatrix();

        boolean shifting = entitylivingbaseIn.isSneaking();
        GlStateManager.rotate(28.6f, (shifting ? 1F : 0), 0.0F, 0.0F);
        GlStateManager.translate(0.0, (shifting ? 0.25F : 0F), 0.00f);
        float n = 1;
        GlStateManager.scale(n, n, n);
        wings.model.render(entitylivingbaseIn, f, g, h, i, j, scale);
        GlStateManager.popMatrix();

    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
