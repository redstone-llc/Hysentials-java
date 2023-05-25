package cc.woverflow.hysentials.mixin;

import cc.woverflow.hysentials.event.events.GuiMouseClickEvent;
import cc.woverflow.hysentials.handlers.sbb.SbbRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiScreen.class)
public class GuiScreenMixin {
    @Inject(method = "handleMouseInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;mouseClicked(III)V"), cancellable = true)
    public void handleMouseInput(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new GuiMouseClickEvent((int) SbbRenderer.getMouseX(), (int) SbbRenderer.getMouseY(), Mouse.getEventButton(), ci));
    }
}
