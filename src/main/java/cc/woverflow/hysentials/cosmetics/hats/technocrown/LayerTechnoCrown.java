package cc.woverflow.hysentials.cosmetics.hats.technocrown;

import cc.woverflow.hysentials.Hysentials;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.ResourceLocation;

public class LayerTechnoCrown implements LayerRenderer<AbstractClientPlayer> {
    public static ResourceLocation texture = new ResourceLocation("hysentials:hats/technocrown.png");
    private final RenderPlayer playerRenderer;

    public LayerTechnoCrown(RenderPlayer renderPlayer) {
        this.playerRenderer = renderPlayer;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float scale) {
        if (!Hysentials.INSTANCE.technoCrown.canUse(entitylivingbaseIn)) {
            return;
        }
        this.playerRenderer.bindTexture(texture);
        float l = entitylivingbaseIn.prevRotationYaw + (entitylivingbaseIn.rotationYaw - entitylivingbaseIn.prevRotationYaw) * partialTicks - (entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks);
        float m = entitylivingbaseIn.prevRotationPitch + (entitylivingbaseIn.rotationPitch - entitylivingbaseIn.prevRotationPitch) * partialTicks;
        GlStateManager.pushMatrix();
        GlStateManager.rotate(l, 0.0F, 0.2F, 0.0F);
        GlStateManager.rotate(m, 0.2F, 0.0F, 0.0F);
        boolean flag = entitylivingbaseIn.isWearing(EnumPlayerModelParts.HAT);
        boolean shifting = entitylivingbaseIn.isSneaking();
        GlStateManager.translate(0.375F * (float) (0.5 * 2 - 1), -2.0F - (flag ? 0.04F : 0F) + 0.08 + (shifting ? 0.2F : 0F), 0.0F);

        float n = 1;
        GlStateManager.scale(n, n, n);
        Hysentials.INSTANCE.technoCrown.model.render(entitylivingbaseIn, f, g, h, i, j, scale);
        GlStateManager.popMatrix();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
