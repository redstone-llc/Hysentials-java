package llc.redstone.hysentials.cosmetics;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public interface Cosmetic {
    boolean canUse(EntityPlayer player);
    ModelBase getModel();
    ResourceLocation getTexture();
    String getName();

    default void renderPreview(int x, int y, int ticks) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (!canUse(player)) return;
        Minecraft.getMinecraft().getTextureManager().bindTexture(getTexture());
        GlStateManager.pushMatrix();

        GlStateManager.rotate(toRadians(ticks /20f), 0f, 1.0F, 0.0F);
//        GlStateManager.translate(x, y, 0);
        float n = 1;
        GlStateManager.scale(n, n, n);

        getModel().render(player, 0, 0, 0, 0, 0, n);

        GlStateManager.popMatrix();
    }

    default float toRadians(float degrees) {
        return (float) (degrees * (Math.PI / 180));
    }
}
