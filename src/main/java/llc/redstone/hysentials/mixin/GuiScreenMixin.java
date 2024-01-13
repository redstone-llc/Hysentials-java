package llc.redstone.hysentials.mixin;

import llc.redstone.hysentials.event.events.GuiKeyboardEvent;
import llc.redstone.hysentials.event.events.GuiMouseClickEvent;
import llc.redstone.hysentials.handlers.sbb.SbbRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiScreen.class, priority = Integer.MAX_VALUE)
public class GuiScreenMixin {
    @Inject(method = "handleMouseInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;mouseClicked(III)V"), cancellable = true)
    public void handleMouseInput(CallbackInfo ci) {
        MinecraftForge.EVENT_BUS.post(new GuiMouseClickEvent((int) SbbRenderer.getMouseX(), (int) SbbRenderer.getMouseY(), Mouse.getEventButton(), ci));
    }

    @Inject(method = "handleKeyboardInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;keyTyped(CI)V"), cancellable = true)
    public void handleKeyboardInput(CallbackInfo ci) {
        GuiKeyboardEvent event = new GuiKeyboardEvent(Keyboard.getEventCharacter(), Keyboard.getEventKey(), Minecraft.getMinecraft().currentScreen);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            ci.cancel();
        }
    }
    @Shadow
    public void drawWorldBackground(int tint) {}

    @Inject(method = "drawDefaultBackground", at = @At("HEAD"), cancellable = true)
    public void drawDefaultBackground(CallbackInfo ci) {
        this.drawWorldBackground(0);

        MinecraftForge.EVENT_BUS.post(new GuiScreenEvent.BackgroundDrawnEvent((GuiScreen) (Object) this));
        ci.cancel();
    }
}
