package cc.woverflow.hysentials.mixin;

import cc.woverflow.hysentials.Hysentials;
import cc.woverflow.hysentials.handlers.imageicons.ImageIcon;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.*;

import java.util.regex.Matcher;

import static cc.woverflow.hysentials.handlers.imageicons.ImageIcon.stringPattern;

@Mixin(value = Render.class, priority = Integer.MIN_VALUE)
public abstract class RenderMixin<T extends Entity> {

    @Final
    @Shadow
    protected RenderManager renderManager;

    @Shadow
    public FontRenderer getFontRendererFromRenderManager() {
        return this.renderManager.getFontRenderer();
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    protected void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance) {
        double d = entityIn.getDistanceSqToEntity(this.renderManager.livingPlayer);
        if (entityIn instanceof EntityPlayer) {
            if (Hysentials.INSTANCE.getOnlineCache().getPlayerDisplayNames().containsKey(entityIn.getUniqueID())) {
                str = str.replaceAll("\\[[A-Za-z§0-9+]+] " + entityIn.getName(), Hysentials.INSTANCE.getOnlineCache().getPlayerDisplayNames().get(entityIn.getUniqueID()));
            }

            if (Hysentials.INSTANCE.getOnlineCache().getOnlinePlayers().containsKey(entityIn.getUniqueID())) {
                str = "§r§a■ §r" + str;
            }
        }
        Matcher matcher = stringPattern.matcher(str);
        if (!(d > (double) (maxDistance * maxDistance))) {
            FontRenderer fontRenderer = this.getFontRendererFromRenderManager();
            float f = 1.6F;
            float g = 0.016666668F * f;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x + 0.0F, (float) y + entityIn.height + 0.5F, (float) z);
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
            int additional = 0;
            String length = str;
            while (matcher.find()) {
                String group = matcher.group(1);
                length = length.replace(":" + group + ":", "");
                additional += (ImageIcon.getIcon(group) != null) ? ImageIcon.getIcon(group).getWidth() : 0;
            }

            int j = fontRenderer.getStringWidth(length) / 2;
            j += additional / 2;


            GlStateManager.disableTexture2D();
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldRenderer.pos((double) (-j - 2), (double) (-1 + i), 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldRenderer.pos((double) (-j - 2), (double) (8 + i), 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldRenderer.pos((double) (j + 2), (double) (8 + i), 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldRenderer.pos((double) (j + 2), (double) (-1 + i), 0.0).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            ImageIcon.shiftRenderText(fontRenderer, str, (-fontRenderer.getStringWidth(length) / 2) - (additional / 2), i, 553648127, false);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            ImageIcon.shiftRenderText(fontRenderer, str, (-fontRenderer.getStringWidth(length) / 2) - (additional / 2), i, -1, false);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }

}
