package cc.woverflow.hysentials.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = GuiUtilRenderComponents.class)
public class GuiUtilRCMixin {
    @Redirect(method = "splitText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;getStringWidth(Ljava/lang/String;)I"))
    private static int onGetStringWidth(FontRenderer fontRenderer, String text) {
        return Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
    }
}
