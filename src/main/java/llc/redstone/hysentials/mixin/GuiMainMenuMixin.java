package llc.redstone.hysentials.mixin;

import llc.redstone.hysentials.updateGui.RequestUpdateGui;
import llc.redstone.hysentials.updateGui.UpdateChecker;
import llc.redstone.hysentials.updateGui.UpdateGui;
import llc.redstone.hysentials.hook.MainMenuHook;
import llc.redstone.hysentials.hook.MainMenuHook;
import net.minecraft.client.gui.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(GuiMainMenu.class)
public class GuiMainMenuMixin implements MainMenuHook {

    @Inject(method = "initGui", at = @At("RETURN"))
    private void injectPopup(CallbackInfo ci) {
        if (UpdateChecker.Companion.getUpdateGetter().getUpdateObj() == null) return;
        if (UpdateGui.Companion.getComplete()) return;
        new RequestUpdateGui(false, true).initGui();
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreen;drawScreen(IIF)V", ordinal = 0), cancellable = true)
    public void drawString(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (UpdateChecker.Companion.getUpdateGetter().getUpdateObj() == null) return;
        if (UpdateGui.Companion.getComplete()) return;
        if (RequestUpdateGui.Companion.getInstance() == null) return;

        ci.cancel();
        RequestUpdateGui.Companion.getInstance().drawScreenExternal(mouseX, mouseY, partialTicks);
    }


    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton, CallbackInfo ci) throws IOException {
        if (UpdateChecker.Companion.getUpdateGetter().getUpdateObj() == null || UpdateGui.Companion.getComplete() || RequestUpdateGui.Companion.getInstance() == null) {
            return;
        }
        ci.cancel();
    }

    @Shadow
    private void drawPanorama(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
        // NO-OP
    }

    @Override
    public GuiScreen getScreen() {
        return (GuiScreen) (Object) this;
    }

    @Override
    public void drawPanoramA(int p_73970_1_, int p_73970_2_, float p_73970_3_) {
        drawPanorama(p_73970_1_, p_73970_2_, p_73970_3_);
    }
}
