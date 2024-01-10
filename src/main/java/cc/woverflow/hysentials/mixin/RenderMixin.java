package cc.woverflow.hysentials.mixin;

import cc.woverflow.hysentials.handlers.redworks.BwRanksUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = Render.class, priority = Integer.MIN_VALUE)
public abstract class RenderMixin<T extends Entity> {
    @Final
    @Shadow
    protected RenderManager renderManager;

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance) {
        double d = entityIn.getDistanceSqToEntity(this.renderManager.livingPlayer);
        if (!(d > (double)(maxDistance * maxDistance))) {
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
            float f = 1.6F;
            float g = 0.016666668F * f;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x + 0.0F, (float)y + entityIn.height + 0.5F, (float)z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-g, -g, g);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            int i = 0;
            if (str.equals("deadmau5")) {
                i = -10;
            }

            int j = fontRenderer.getStringWidth(str) / 2;
            GlStateManager.disableTexture2D();
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldRenderer.pos((double)(-j - 1), (double)(-1 + i), 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldRenderer.pos((double)(-j - 1), (double)(8 + i), 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldRenderer.pos((double)(j + 1), (double)(8 + i), 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldRenderer.pos((double)(j + 1), (double)(-1 + i), 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            fontRenderer.drawString(str, -fontRenderer.getStringWidth(str) / 2, i, 553648127);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            fontRenderer.drawString(str, -fontRenderer.getStringWidth(str) / 2, i, -1);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }
}
