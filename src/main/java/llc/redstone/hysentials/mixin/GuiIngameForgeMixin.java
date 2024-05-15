package llc.redstone.hysentials.mixin;

import cc.polyfrost.oneconfig.libs.universal.UGraphics;
import cc.polyfrost.oneconfig.libs.universal.UMinecraft;
import cc.polyfrost.oneconfig.libs.universal.UResolution;
import cc.polyfrost.oneconfig.renderer.NanoVGHelper;
import cc.polyfrost.oneconfig.utils.InputHandler;
import llc.redstone.hysentials.config.HysentialsConfig;
import llc.redstone.hysentials.guis.hsplayerlist.GuiOnlineList;
import llc.redstone.hysentials.macrowheel.MacroWheelData;
import llc.redstone.hysentials.macrowheel.overlay.MacroWheelOverlay;
import llc.redstone.hysentials.macrowheel.overlay.MacroWheelOverlayKt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.annotation.ElementType;

import static net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType.BOSSHEALTH;


@Mixin(value = GuiIngameForge.class, priority = 9001)
public class GuiIngameForgeMixin {
    @Unique
    private GuiOnlineList hysentials$hysentialsOnlineList;
    @Shadow
    protected void renderPlayerList(int width, int height) {
    }

    @Shadow
    private boolean pre(RenderGameOverlayEvent.ElementType type) {
        return false;
    }

    @Shadow
    private void post(RenderGameOverlayEvent.ElementType type) {
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(CallbackInfo ci) {
        hysentials$hysentialsOnlineList = new GuiOnlineList();
    }

    @Redirect(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/GuiIngameForge;renderPlayerList(II)V"))
    public void hysentials$renderPlayerList(GuiIngameForge instance, int width, int height) {
        if (!hysentials$renderHysentialsPlayerList(width, height)) renderPlayerList(width, height);
    }

    @Inject(method = "renderRecordOverlay", at = @At("HEAD"), cancellable = true, remap = false)
    private void cancelActionBar(int width, int height, float partialTicks, CallbackInfo ci) {
        if (HysentialsConfig.actionBarHUD.isEnabled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderToolHightlight", at = @At("HEAD"), cancellable = true, remap = false)
    private void cancelSelectedItem(CallbackInfo ci) {
        if (HysentialsConfig.heldItemTooltipHUD.isEnabled()) {
            ci.cancel();
        }
    }

    @Inject(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;enableAlpha()V", shift = At.Shift.AFTER))
    public void hysentials$renderMacroWheel(float partialTicks, CallbackInfo ci) {
        if (HysentialsConfig.macroWheelKeyBind.isActive() && Minecraft.getMinecraft().currentScreen == null && MacroWheelData.MacroWheel.getCooldown() < System.currentTimeMillis()) {
            if (!MacroWheelOverlayKt.getStopped()) {
                MacroWheelOverlay overlay = MacroWheelOverlay.Companion.newI();
                InputHandler inputHandler = new InputHandler();
                inputHandler.scale(UResolution.getScaleFactor(), UResolution.getScaleFactor());
                if (!MacroWheelData.MacroWheel.getWasMacroWheelActive()) {
                    Minecraft.getMinecraft().mouseHelper.ungrabMouseCursor();
                    Minecraft.getMinecraft().mouseHelper.deltaX = 0;
                    Minecraft.getMinecraft().mouseHelper.deltaY = 0;
                    MacroWheelData.MacroWheel.setWasMacroWheelActive(true);
                }
                hysentials$drawMacroWheel(overlay, partialTicks, inputHandler);
                final ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
                int i1 = scaledresolution.getScaledWidth();
                int j1 = scaledresolution.getScaledHeight();
                final int k1 = Mouse.getX() * i1 / Minecraft.getMinecraft().displayWidth;
                final int l1 = j1 - Mouse.getY() * j1 / Minecraft.getMinecraft().displayHeight - 1;
                hysentials$drawPostMacroWheel(overlay, new GuiScreenEvent.DrawScreenEvent.Post(
                    null, k1, l1, partialTicks
                ), inputHandler);
            }
        } else if (MacroWheelData.MacroWheel.getWasMacroWheelActive() || MacroWheelOverlayKt.getStopped()) {
            MacroWheelOverlayKt.setStopped(false);
            MacroWheelData.MacroWheel.setWasMacroWheelActive(false);
            if (Minecraft.getMinecraft().currentScreen == null) {
                Minecraft.getMinecraft().mouseHelper.grabMouseCursor();
            }
        }
    }

    @Unique
    public void hysentials$drawMacroWheel(MacroWheelOverlay overlay, float partialTicks, InputHandler inputHandler) {
        NanoVGHelper.INSTANCE.setupAndDraw(true, (vg) -> {
            overlay.draw(vg, partialTicks, inputHandler);
        });
    }

    @Unique
    public void hysentials$drawPostMacroWheel(MacroWheelOverlay overlay, GuiScreenEvent.DrawScreenEvent.Post event, InputHandler inputHandler) {
        overlay.drawPost(event, inputHandler);
    }


    @Unique
    protected boolean hysentials$renderHysentialsPlayerList(int width, int height)
    {
        if (HysentialsConfig.onlinePlayersKeyBind.isActive() && Minecraft.getMinecraft().currentScreen == null) {
            hysentials$hysentialsOnlineList.updatePlayerList(true);
            ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
            hysentials$hysentialsOnlineList.renderPlayerlist(res.getScaledWidth());
            return true;
        }
        return false;
    }
}
