package cc.woverflow.hysentials.cosmetics.kzero;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;

public class KzeroLayer implements LayerRenderer<AbstractClientPlayer> {
    public static ResourceLocation texture = new ResourceLocation("hysentials:kzero_bundle.png");

    private final RenderPlayer playerRenderer;

    public KzeroLayer(RenderPlayer renderPlayer) {
        this.playerRenderer = renderPlayer;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float scale) {
//        if (!KzeroBundle.canUse(entitylivingbaseIn, KzeroBundle.Type.HAIR)) {
//            return;
//        }
        if (entitylivingbaseIn.isInvisible()) return;
        this.playerRenderer.bindTexture(texture);

        float n = 1;
        if (KzeroBundle.canUse(entitylivingbaseIn, KzeroBundle.Type.HAIR)) {
            GlStateManager.pushMatrix();
            if (entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            playerRenderer.getMainModel().bipedHead.postRender(0.0625F);

            GlStateManager.translate(0.0F, -0.04, 0F);
            GlStateManager.scale(n, n, n);
            KzeroBundle.hairModel.render(entitylivingbaseIn, f, g, h, i, j, scale);
            GlStateManager.popMatrix();

        }

        if (KzeroBundle.canUse(entitylivingbaseIn, KzeroBundle.Type.ROBE)) {
            GlStateManager.pushMatrix();
            if (entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            playerRenderer.getMainModel().bipedBody.postRender(0.0625F);
            GlStateManager.translate(0.0F, -0.04, 0F);
            GlStateManager.scale(n, n, n);
            KzeroBundle.robeModel.render(entitylivingbaseIn, f, g, h, i, j, scale);
            GlStateManager.popMatrix();
        }

        if (KzeroBundle.canUse(entitylivingbaseIn, KzeroBundle.Type.BUNDLE)) {
            GlStateManager.pushMatrix();
            if (entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            playerRenderer.getMainModel().bipedBody.postRender(0.0625F);
            GlStateManager.translate(0.0F, -0.04, 0F);
            GlStateManager.scale(n, n, n);
            KzeroBundle.katanaModel.render(entitylivingbaseIn, f, g, h, i, j, scale);
            GlStateManager.popMatrix();
        }

        if (KzeroBundle.canUse(entitylivingbaseIn, KzeroBundle.Type.SLIPPER)) {
            GlStateManager.pushMatrix();
            if (entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            playerRenderer.getMainModel().bipedBody.postRender(0.0625F);
            GlStateManager.translate(0.0F, -0.04, 0F);
            GlStateManager.scale(n, n, n);
            KzeroBundle.slipperModel.render(entitylivingbaseIn, f, g, h, i, j, scale);
            GlStateManager.popMatrix();
        }

//        GlStateManager.pushMatrix();
//        if (entitylivingbaseIn.isSneaking()) {
//            GlStateManager.translate(0.0F, 0.2F, 0.0F);
//        }
//        playerRenderer.getMainModel().bipedBody.postRender(0.0625F);
//        GlStateManager.translate(0.0F, -0.04, 0F);
//        GlStateManager.scale(n, n, n);
//        KzeroBundle.slipperModel.render(entitylivingbaseIn, f, g, h, i, j, scale);
//        GlStateManager.popMatrix();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
