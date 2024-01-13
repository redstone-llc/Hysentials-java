package llc.redstone.hysentials.cosmetics.hats.blackcat;

import llc.redstone.hysentials.Hysentials;
import llc.redstone.hysentials.cosmetics.hats.cat.CatHat;
import llc.redstone.hysentials.util.Material;
import llc.redstone.hysentials.Hysentials;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static net.minecraft.client.renderer.block.model.ItemCameraTransforms.*;

public class LayerBlackCatHat implements LayerRenderer<AbstractClientPlayer> {
    public static ResourceLocation texture = new ResourceLocation("hysentials:hats/blackcat.png");
    public static BlackCat hat;
    private final RenderPlayer playerRenderer;
    public LayerBlackCatHat(RenderPlayer renderPlayer) {
        this.playerRenderer = renderPlayer;
        if (Hysentials.INSTANCE == null) {
            return;
        }
        hat = new BlackCat();
        hat.model.init(renderPlayer);
    }

    @Override
    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float f, float g, float partialTicks, float h, float i, float j, float scale) {
        if (entitylivingbaseIn.isInvisible()) return;
        BlackCat hat = Hysentials.INSTANCE.blackCat;
        if (hat == null) {
            return;
        }
        if (!hat.canUse(entitylivingbaseIn)) {
            return;
        }
        if (entitylivingbaseIn.isInvisible()) return;

        this.playerRenderer.bindTexture(texture);
        Minecraft minecraft = Minecraft.getMinecraft();
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
