package llc.redstone.hysentials.cosmetics.backpack;

import llc.redstone.hysentials.cosmetics.hats.cat.CatHat;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class LayerBackPack implements LayerRenderer<AbstractClientPlayer> {
    private final RenderPlayer playerRenderer;

    public LayerBackPack(RenderPlayer renderPlayer) {
        this.playerRenderer = renderPlayer;
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float scale) {
        BackpackCosmetic backpack = null;
        for (BackpackCosmetic bp : BackpackCosmetic.backpacks) {
            if (bp.canUse(entitylivingbaseIn)) {
                backpack = bp;
                break;
            }
        }
        if (backpack == null) {
            return;
        }
        if (entitylivingbaseIn.isInvisible()) return;

        this.playerRenderer.bindTexture(backpack.texture);
        GlStateManager.pushMatrix();

        boolean shifting = entitylivingbaseIn.isSneaking();
        GlStateManager.rotate(28.6f, (shifting ? 1F : 0), 0.0F, 0.0F);
        GlStateManager.translate(0.375F * (float) (0.5 * 2 - 1), -0.785F + (shifting ? 0.25F : 0F), (shifting ? 0.1F : 0.235F));
        float n = 1;
        GlStateManager.scale(n, n, n);
        if (backpack.model == null) {
            backpack.catModel.render(entitylivingbaseIn, f, g, h, i, j, scale);
        } else {
            backpack.model.render(entitylivingbaseIn, f, g, h, i, j, scale);
        }
        GlStateManager.popMatrix();

    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
