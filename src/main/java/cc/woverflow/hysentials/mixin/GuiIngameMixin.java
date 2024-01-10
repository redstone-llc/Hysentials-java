package cc.woverflow.hysentials.mixin;

import cc.woverflow.hysentials.GuiIngameHysentials;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = Minecraft.class, priority = 1)
public class GuiIngameMixin {
    @Redirect(method = "startGame", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;ingameGUI:Lnet/minecraft/client/gui/GuiIngame;"))
    private void startGame(Minecraft instance, GuiIngame value) {
        instance.ingameGUI = new GuiIngameHysentials(instance);
    }
}
