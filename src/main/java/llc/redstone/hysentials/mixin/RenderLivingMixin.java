package llc.redstone.hysentials.mixin;

import llc.redstone.hysentials.handlers.redworks.BwRanksUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = RendererLivingEntity.class)
public abstract class RenderLivingMixin<T extends EntityLivingBase> extends Render<T> {
    protected RenderLivingMixin(RenderManager renderManager) {
        super(renderManager);
    }

    @Redirect(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/IChatComponent;getFormattedText()Ljava/lang/String;"))
    public String renderName(IChatComponent instance, T entity, double x, double y, double z) {
        if (entity instanceof EntityPlayer) {
            NetworkPlayerInfo playerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo((entity.getUniqueID()));
            return BwRanksUtils.getPlayerName(playerInfo, false);
        }
        return entity.getDisplayName().getFormattedText();
    }

    @Redirect(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I"))
    public int getFontRendererFromRenderManager(FontRenderer instance, String text, int x, int y, int color) {
        return Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, color);
    }

    @Redirect(method = "renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;getStringWidth(Ljava/lang/String;)I"))
    public int getStringWidth(FontRenderer instance, String text) {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
    }
}
