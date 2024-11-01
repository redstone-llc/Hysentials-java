package llc.redstone.hysentials.cosmetics;

import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import java.util.ArrayList;
import java.util.List;

public interface Cosmetic {
    boolean canUse(EntityPlayer player);

    ModelBase getModel();

    ResourceLocation getTexture();

    String getName();

    default void renderPreview(int x, int y, int ticks) {
        EntityPlayer player = UMinecraft.getPlayer();
        Minecraft mc = Minecraft.getMinecraft();
        float zLevel = 350;
//        if (!canUse(player)) return;
        GlStateManager.pushMatrix();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.translate(x, y, 0.0F);
        Project.gluPerspective(90.0F, 1.3333334F, 9.0F, zLevel);
        float f = 1.0F;
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.translate(0.0F, 3.3F, -16.0F);
        GlStateManager.scale(f, f, f);
        float g = 5.0F;
        GlStateManager.scale(g, g, g);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        mc.getTextureManager().bindTexture(getTexture());
        GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
//        float h = this.field_147076_A + (this.field_147080_z - this.field_147076_A) * ticks;
//        GlStateManager.translate((1.0F - h) * 0.2F, (1.0F - h) * 0.1F, (1.0F - h) * 0.25F);
//        GlStateManager.rotate(-(1.0F - h) * 90.0F - 90.0F, 0.0F, 1.0F, 0.0F);
//        GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
//        float k = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * ticks + 0.25F;
//        float l = this.field_147069_w + (this.field_147071_v - this.field_147069_w) * ticks + 0.75F;
//        k = (k - (float) MathHelper.truncateDoubleToInt((double)k)) * 1.6F - 0.3F;
//        l = (l - (float)MathHelper.truncateDoubleToInt((double)l)) * 1.6F - 0.3F;
//        if (k < 0.0F) {
//            k = 0.0F;
//        }
//
//        if (l < 0.0F) {
//            l = 0.0F;
//        }
//
//        if (k > 1.0F) {
//            k = 1.0F;
//        }
//
//        if (l > 1.0F) {
//            l = 1.0F;
//        }

        GlStateManager.enableRescaleNormal();
        //2d rendering
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        getModel().render(null, 0.0F, 0, 0, 0, 0.0F, 0.0625F);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.matrixMode(5889);
        GlStateManager.viewport(0, 0, mc.displayWidth, mc.displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
    }

    default float toRadians(float degrees) {
        return (float) (degrees * (Math.PI / 180));
    }
}
