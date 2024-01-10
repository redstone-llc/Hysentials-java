package cc.woverflow.hysentials.cosmetics.hats.cat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class LayerCatHat implements LayerRenderer<AbstractClientPlayer> {
    private final RenderPlayer playerRenderer;

    public LayerCatHat(RenderPlayer renderPlayer) {
        this.playerRenderer = renderPlayer;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float scale) {
        if (entitylivingbaseIn.isInvisible()) return;
        CatHat hat = null;
        for (CatHat catHat : CatHat.catHats) {
            if (catHat.canUse(entitylivingbaseIn)) {
                hat = catHat;
                break;
            }
        }
        if (hat == null) {
            return;
        }
        this.playerRenderer.bindTexture(hat.texture);
        GlStateManager.pushMatrix();
        if (entitylivingbaseIn.isSneaking()) {
            GlStateManager.translate(0.0F, 0.2F, 0.0F);
        }
        playerRenderer.getMainModel().bipedHead.postRender(0.0625F);

        boolean flag = entitylivingbaseIn.isWearing(EnumPlayerModelParts.HAT);

        GlStateManager.translate(0.375F * (float) (0.5 * 2 - 1), -2.0F - (flag ? 0.04F : 0F), 0.0F);
        float n = 1;
        GlStateManager.scale(n, n, n);
        hat.model.render(entitylivingbaseIn, f, g, h, i, j, scale);
        GlStateManager.popMatrix();

    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
