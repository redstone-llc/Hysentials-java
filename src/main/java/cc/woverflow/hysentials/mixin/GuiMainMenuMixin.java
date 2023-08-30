package cc.woverflow.hysentials.mixin;

import cc.woverflow.hysentials.gui.RequestUpdateGui;
import cc.woverflow.hysentials.gui.UpdateChecker;
import cc.woverflow.hysentials.gui.UpdateGui;
import cc.woverflow.hysentials.hook.MainMenuHook;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenRealmsProxy;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.List;

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
        RequestUpdateGui.Companion.getInstance().mouseClickExternal(mouseX, mouseY, mouseButton);
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
